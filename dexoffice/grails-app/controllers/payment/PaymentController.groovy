package payment

import grails.transaction.Transactional
import invoice.Invoice

import org.springframework.http.HttpStatus

import party.Organization
import party.Party
import application.commandobject.RecordPaymentCommand

@Transactional(readOnly = true)
class PaymentController {
	
	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		def q = params.q
		def result = Payment.list(params)
        respond result, [status: HttpStatus.OK]
    }
	
	@Transactional
	def recordPaymentForInvoice(RecordPaymentCommand cmd) {
		if (cmd == null) {
			render status: HttpStatus.NOT_FOUND
			return
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
		def paymentApplication = new PaymentApplication()
		paymentApplication.invoice = Invoice.get(cmd.invoiceId)
		paymentApplication.amountApplied = cmd.amount
		paymentApplication.payment = paymentInstance
		paymentApplication.save(flush:true)
		
		paymentInstance.addToPaymentApplications(paymentApplication)
		paymentInstance.save(flush:true)
		
		respond cmd, [status: HttpStatus.CREATED]
	}
}
