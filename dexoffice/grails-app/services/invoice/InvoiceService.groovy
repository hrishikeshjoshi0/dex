package invoice

import grails.transaction.Transactional
import party.Party
import product.Product
import tax.TaxRate
import application.commandobject.ChangeInvoiceStatusCommand;
import application.commandobject.InvoiceCommand
import application.commandobject.InvoiceItemCommand
import core.Status
import core.StatusType

@Transactional
class InvoiceService {
	
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

			//Check if taxPercentage is given.
			if(taxRate && taxRate?.taxPercentage) {
				def taxPercentage = taxRate?.taxPercentage
				return taxPercentage
			}
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
			
			def taxPercentage = calculateTaxOnProduct(product)
			if(taxPercentage) {
				taxItem.amount = taxPercentage * item.amount * 0.01
			}
			
			taxItem.invoiceItemType = InvoiceItemType.findByName("ITM_SERVICE_TAX")
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
		invoice.invoiceNumber = invoiceCommand.invoiceNumber
		
		invoice.save(flush:true)
		
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
    }
	
	def changeInvoiceStatus(ChangeInvoiceStatusCommand status) {
		
	}
}
