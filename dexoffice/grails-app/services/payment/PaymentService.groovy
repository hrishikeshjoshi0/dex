package payment

import grails.transaction.Transactional
import invoice.Invoice
import invoice.InvoiceStatus
import party.Organization
import party.Party
import application.commandobject.RecordPaymentCommand
import core.Status
import core.StatusType

@Transactional
class PaymentService {
	
	def invoiceService

	def recordPaymentForInvoice(RecordPaymentCommand cmd) {
		if (cmd == null) {
			return null
		}
		
		def paymentInstance = new Payment()
		paymentInstance.paymentType = PaymentType.findByCode("CUSTOMER_PAYMENT")
		
		//Create payment method
		def paymentMethod = new PaymentMethod()
		paymentMethod.payment = paymentInstance
		paymentMethod.paymentMethodType = PaymentMethodType.findByCode(cmd.paymentMethodType)
		
		paymentInstance.partyFrom = Party.get(cmd.partyFromId)
		paymentInstance.partyTo = Organization.findByName("-COMPANY-")
		paymentInstance.effectiveDate = cmd.effectiveDate
		paymentInstance.comments = cmd.comments
		paymentInstance.amount = cmd.amount
		
		//Create payment application
		def invoice = Invoice.get(cmd.invoiceId)
		
		def paymentApplication = new PaymentApplication()
		paymentApplication.invoice = invoice
		paymentApplication.amountApplied = cmd.amount
		paymentApplication.payment = paymentInstance
		
		paymentInstance.addToPaymentApplications(paymentApplication)
		
		//Payment Status
		def paymentStatusType = StatusType.findByDescription("PAYMENT_STATUS")
		
		def paymentStatus = new PaymentStatus()
		paymentStatus.payment = paymentInstance
		paymentStatus.status = Status.findByStatusCodeAndStatusType("RECEIVED",paymentStatusType)
		paymentInstance.addToStatuses(paymentStatus)
		paymentInstance.currentStatus = paymentStatus
		
		paymentInstance.save(flush:true)
		
		//Invoice Status.
		def unpaidAmount = invoiceService.getUnpaidAmountForInvoice(invoice)
		if(unpaidAmount > 0) {
			def status = Status.findByStatusCodeAndStatusType("PARTIALLY_PAID",
				StatusType.findByDescription("INVOICE_STATUS"))
			
			def invoiceStatus = new InvoiceStatus()
			invoiceStatus.status = status
			invoiceStatus.invoice = invoice
			invoiceStatus.save(flush:true)
			invoice.currentInvoiceStatus = invoiceStatus
			invoice.addToStatuses(invoiceStatus)
			invoice.save(flush:true)
		} else if(unpaidAmount <= 0) {
			def status = Status.findByStatusCodeAndStatusType("PAID",
				StatusType.findByDescription("INVOICE_STATUS"))
			
			def invoiceStatus = new InvoiceStatus()
			invoiceStatus.status = status
			invoiceStatus.invoice = invoice
			invoiceStatus.save(flush:true)
			invoice.currentInvoiceStatus = invoiceStatus
			invoice.addToStatuses(invoiceStatus)
			invoice.save(flush:true)
			
			//TODO
			//If -ve unpaid balance adjust the credit.
		}
		
		return paymentInstance
	}
}
