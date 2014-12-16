package invoice

import party.Party
import payment.PaymentApplication
import product.Product
import tax.TaxRate
import application.commandobject.ChangeInvoiceStatusCommand
import application.commandobject.InvoiceCommand
import application.commandobject.InvoiceItemCommand
import application.commandobject.TaxReportCommand
import application.commandobject.TaxReportItem
import core.Status
import core.StatusType
import exceptions.InvoiceCreationException
import grails.transaction.Transactional

@Transactional
class InvoiceService {
	
	def settingService
	
	def getLatestInvoiceCalculation(Invoice invoice) {
		def c = InvoiceCalculation.createCriteria()
		def invoiceCalculations = c.list {
			eq("invoice",invoice)
			order 'calculationDate', 'desc'
			maxResults(1)
		}
		
		if(invoiceCalculations) {
			return invoiceCalculations[0]
		}
	}
	
	def getInvoicesForParty(Party party) {
		def c = Invoice.createCriteria()
		
		def invoices = c.list {
			eq("party",party)
		}
		
		return invoices
	}
	
	def getInvoiceTotalAmount(Invoice invoice) {
		if(!invoice) {
			return null
		}
		
		def c1 = InvoiceItem.createCriteria()
		def invoiceAmount = c1.get {
			projections {
				sum('amount')
			}
			eq("invoice",invoice)
		}
		
		if(!invoiceAmount) {
			invoiceAmount = 0.0
		}
		
		invoiceAmount
	}
	
	def getPaidAmountForInvoice(Invoice invoice) {
		if(!invoice) {
			return null
		}
		
		def c = PaymentApplication.createCriteria()
		def sumOfPayments = c.get {
			projections {
				sum('amountApplied')
			}
			eq("invoice",invoice)
		}
		
		if(!sumOfPayments) {
			sumOfPayments = 0.0
		}
		
		return sumOfPayments
	}
	
	def getUnpaidAmountForInvoice(Invoice invoice) {
		if(!invoice) {
			return null
		}
		
		def c = PaymentApplication.createCriteria()
		def sumOfPayments = c.get {
			projections {
				sum('amountApplied')
			}
			eq("invoice",invoice)
		}
		
		if(!sumOfPayments) {
			sumOfPayments = 0.0
		}
		
		def c1 = InvoiceItem.createCriteria()
		def invoiceAmount = getInvoiceTotalAmount(invoice)
		
		return invoiceAmount - sumOfPayments
	}
	
	def calculateTaxOnProduct(Product product) {
		if(!product) {
			return null
		}

		def taxCategory = product.taxCategory
		if(taxCategory) {
			def now = new Date()

			def c = TaxRate.createCriteria()
			def taxRates = c.list {
				eq("taxCategory",taxCategory)
				le("fromDate",now)
				or {
					isNull("thruDate")
					ge("thruDate",now)
				}
			}

			def taxRate = null
			if(taxRates && taxRates.size() != 0) {
				taxRate = taxRates.get(0)
			}
			
			return taxRate
		}
	}
	
	
	def addInvoiceItem(Invoice invoice, InvoiceItemCommand cmd) {
		def item = new InvoiceItem()
		item.invoice = invoice
		item.description = cmd.description
		
		item.quantity = cmd.quantity
		item.unitPrice = cmd.unitPrice
		item.amount = item.quantity * item.unitPrice  
		
		def product = Product.get(cmd.productId)
		item.product = product
		
		if(product?.productType?.name == "SERVICE" || product?.productType?.name == "GOOD") {
			item.invoiceItemType = InvoiceItemType.findByName("INV_PROD_ITEM")
		}
		
		item.save(flush:true)
		
		//If item has tax associated with it, create a new line to record the tax
		if(cmd.tax) {
			def taxItem = new InvoiceItem()
			taxItem.invoice = invoice
			taxItem.description = "Tax for " + item.description
			
			def taxRate = calculateTaxOnProduct(product)
			
			//Check if taxPercentage is given.
			def taxPercentage
			if(taxRate && taxRate?.taxPercentage) {
				taxPercentage = taxRate?.taxPercentage
			}
			
			if(taxPercentage) {
				taxItem.amount = taxPercentage * item.amount * 0.01
			}
			
			def invoiceItemType = ''
			if(taxRate.taxType?.name == 'SERVICE_TAX') {
				invoiceItemType = "ITM_SERVICE_TAX"
			}
			
			taxItem.invoiceItemType = InvoiceItemType.findByName(invoiceItemType)
			taxItem.parent = item
			
			taxItem.validate()
			
			taxItem.save(flush:true)
		}
	}

    def createInvoice(InvoiceCommand invoiceCommand) {
		def invoice = new Invoice()
		if(invoiceCommand.partyFromId) {
			invoice.partyFrom = Party.get(invoiceCommand.partyFromId)
		}
		invoice.party = Party.get(invoiceCommand.partyId)
		invoice.invoiceDate = invoiceCommand.invoiceDate
		invoice.dueDate = invoiceCommand.dueDate
		invoice.message = invoiceCommand.message
		
		def invoiceSequenceNumber = settingService.getSettingValueFromCode("INVOICE_SEQUENCE_NUMBER","INVOICE_SETTINGS")
		
		def invoiceNumberMask = settingService.getSettingValueFromCode("INVOICE_NUMBER_MASK","INVOICE_SETTINGS")
		
		def invoiceNumber = getInvoiceNumber(invoiceNumberMask, invoiceSequenceNumber)
		
		invoice.invoiceNumber = invoiceNumber
		
		if(invoice.save(flush:true)) {
			//Update Settings
			updateInvoiceSettings(invoiceSequenceNumber)
		}
		
		if(!invoice.errors) {
			invoiceCommand.errors = invoice.errors
			return
		}
				
		//Status
		def status = Status.findByStatusCodeAndStatusType(invoiceCommand.currentInvoiceStatus,StatusType.findByDescription("INVOICE_STATUS"))
		
		def invoiceStatus = new InvoiceStatus()
		invoiceStatus.status = status
		invoiceStatus.invoice = invoice
		invoiceStatus.save(flush:true)

		invoice.currentInvoiceStatus = invoiceStatus
		invoice.save(flush:true)
		
		def items = invoiceCommand.items
		items?.each {
			addInvoiceItem(invoice,it)			
		}
		
		
		//The invoice creation is complete so triggering a calculation.
		def invoiceCalculation = new InvoiceCalculation()
		invoiceCalculation.calculationDate = new Date()
		invoiceCalculation.invoiceGrandTotal = getInvoiceTotalAmount(invoice)
		invoiceCalculation.currentReceivedAmount = getPaidAmountForInvoice(invoice)
		invoiceCalculation.currentReceivableAmount = getUnpaidAmountForInvoice(invoice)
		invoiceCalculation.invoice = invoice
		invoiceCalculation.save(flush:true)
		
		invoice.currentInvoiceCalculation = invoiceCalculation
		invoice.save(flush:true)
		
		invoiceCommand.id = invoice.id
    }
	
	def getInvoiceNumber(String invoiceNumberMask, String invoiceSequenceNumber) {
		if(!invoiceNumberMask) {
			throw new InvoiceCreationException("Invoice settings are not configured. Please check the same.")
		}
		
		if(!invoiceSequenceNumber) {
			throw new InvoiceCreationException("Invoice settings are not configured. Please check the same.")
		}
		
		def invoiceNumber = String.format(invoiceNumberMask, invoiceSequenceNumber?.toInteger())
		invoiceNumber
	}
	
	def updateInvoiceSettings(String invoiceSequenceNumber) {
		def nextSequenceNumber = invoiceSequenceNumber.toInteger() + 1;
		settingService.updateSetting("INVOICE_SEQUENCE_NUMBER","INVOICE_SETTINGS",nextSequenceNumber?.toString())
	}
	
	def getTaxesOnInvoice(TaxReportCommand cmd) {
		def c = InvoiceItem.createCriteria()
		
		def now = new Date()
		
		def results = c.list {
			projections {
				sum("amount")
				sum("currentInvoiceCalculation.invoiceGrandTotal")
			}
			
			groupProperty("taxCategory.description")
			
			createAlias("invoiceItemType","invoiceItemType")
			createAlias("invoice","invoice")
			createAlias("parent","parent")
			createAlias("parent.product","product")
			createAlias("product.taxCategory","taxCategory")
			createAlias("invoice.currentInvoiceCalculation","currentInvoiceCalculation")
			
			isNotNull("parent")
			
			if(cmd.mode == 'CASH_BASIS') {
				isNull("invoice.paidDate")
			}
			
			if(cmd.fromDate && cmd.thruDate) {
				between("invoice.invoiceDate",cmd.fromDate,cmd.thruDate)
			}
			
		}
		
		results?.each { it ->
			println it
			def tax = new TaxReportItem()
			tax.taxAmount = it[0]
			tax.totalAmount = it[1]
			tax.taxType = it[2]
			
			cmd.taxes << tax
		}
		
		cmd
	}
	
	def changeInvoiceStatus(ChangeInvoiceStatusCommand status) {
		
	}
}
