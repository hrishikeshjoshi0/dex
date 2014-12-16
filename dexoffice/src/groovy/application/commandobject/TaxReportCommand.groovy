package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class TaxReportCommand {

	Date fromDate
	Date thruDate
	String mode
	
	def taxes = []
}
