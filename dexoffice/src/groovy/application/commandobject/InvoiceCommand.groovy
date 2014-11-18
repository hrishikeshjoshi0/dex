package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class InvoiceCommand {

	Long id
	Long partyFromId
	Long partyId
	String currentInvoiceStatus
	Date invoiceDate
	Date dueDate
	Date paidDate
	String message
	String referenceNumber
	String invoiceNumber
	
	List<InvoiceItemCommand> items
	Object errors	
}
