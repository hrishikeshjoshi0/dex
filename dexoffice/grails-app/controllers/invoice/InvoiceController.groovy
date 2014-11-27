package invoice

import grails.transaction.Transactional
import groovy.json.JsonBuilder

import org.springframework.http.HttpStatus

import application.commandobject.InvoiceCommand
import core.Status

class InvoiceController {

	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def invoiceService

	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		def result = Invoice.list(params)
		respond result, [status: HttpStatus.OK]
	}
	
	def invoiceStatusTypes() {
		def c = Status.createCriteria()
		def invoiceStatusTypes = c.list {
			createAlias("statusType","statusType")
			eq("statusType.description","INVOICE_STATUS")
		}
		respond invoiceStatusTypes,[status:HttpStatus.OK]
	}
	
	def unpaidAmount(InvoiceCommand cmd) {
		if (cmd == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		def result = invoiceService.getUnpaidAmountForInvoice(Invoice.get(cmd.id))
		
		def exp = new Expando(unpaidAmount : result)
		
		respond exp, [status: HttpStatus.OK]
	}
	
	@Transactional
	def save(InvoiceCommand cmd) {
		if (cmd == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		def result = invoiceService.createInvoice(cmd)
		
		respond cmd, [status: HttpStatus.CREATED]
	}
	
	@Transactional
	def show(Invoice i) {
		if (i == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		respond i, [status: HttpStatus.OK]
	}
}
