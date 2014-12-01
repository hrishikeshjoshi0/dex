package payment

import grails.transaction.Transactional

import org.springframework.http.HttpStatus

import application.commandobject.RecordPaymentCommand

@Transactional(readOnly = true)
class PaymentController {
	
	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def paymentService

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
		
		//Record payment for an invoice
		def paymentInstance = paymentService.recordPaymentForInvoice(cmd)
		
		if(paymentInstance.hasErrors()) {
			respond cmd, [status: HttpStatus.BAD_REQUEST]
		} 
		
		if(paymentInstance && !paymentInstance.hasErrors()) {
			respond cmd, [status: HttpStatus.CREATED]
		}
		
	}
}
