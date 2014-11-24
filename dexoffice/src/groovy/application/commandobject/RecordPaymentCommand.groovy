package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class RecordPaymentCommand {
	
	String paymentMethodType
	Long partyFromId
	Long partyToId
	Date effectiveDate
	String comments
	BigDecimal amount
	Long invoiceId

}
