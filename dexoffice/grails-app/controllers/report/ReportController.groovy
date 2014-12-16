package report

import org.springframework.http.HttpStatus

import application.commandobject.TaxReportCommand

class ReportController {
	
	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def invoiceService

    def taxesReport(TaxReportCommand cmd) { 
		def result = invoiceService.getTaxesOnInvoice(cmd)
		respond result, [status: HttpStatus.OK]
	}
}
