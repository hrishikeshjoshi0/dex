package invoice

import grails.transaction.Transactional

import org.codehaus.groovy.grails.plugins.jasper.JasperExportFormat
import org.codehaus.groovy.grails.plugins.jasper.JasperReportDef
import org.springframework.http.HttpStatus

import application.commandobject.InvoiceCommand
import core.Status

class InvoiceController {

	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def invoiceService
	
	def jasperService
	
	def partyService
	
	def getInvoicePdf(Invoice invoice) {
		params.put("_file", "invoice_A4.jasper")
		params.put("_format", "PDF")
		
		def data = [customerName:invoice.party.currentFirstName,invoiceNumber:invoice.invoiceNumber,invoice:invoice,invoiceItems:invoice.items]
		
		def reportDef = new JasperReportDef(name:'invoice_A4.jasper',fileFormat:JasperExportFormat.PDF_FORMAT,reportData:[data]) 
		def bytes = jasperService.generateReport(reportDef).toByteArray()
		
		response.setHeader("Content-disposition", "inline; filename=" + 'fileName' + ".pdf")
		response.contentType = "application/pdf"
		response.outputStream << bytes
	}

	def index(Integer max) {
		//params.max = Math.min(max ?: 10, 100)
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
		
		//def p = i.party
		//def billingAddress = partyService.getBillingAddress(p)
		//renderPdf(template: "/pdf/customer", model: [customer: p,billingAddress:billingAddress,invoice:i], filename: "report.pdf")
		//return null
		respond i, [status: HttpStatus.OK]
	}
}
