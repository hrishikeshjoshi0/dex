package invoice

import core.ContactMech;
import party.Party

class Invoice {
	
	Party partyFrom
	Party party
	InvoiceStatus currentInvoiceStatus
	ContactMech contactMech
	Date invoiceDate
	Date dueDate
	Date paidDate
	String message
	String referenceNumber
	String invoiceNumber
	InvoiceCalculation currentInvoiceCalculation
	
	static hasMany = [statuses : InvoiceStatus,items:InvoiceItem,invoiceCalculations:InvoiceCalculation]

    static constraints = {
		partyFrom nullable:true
		party nullable:false
		currentInvoiceStatus nullable:true
		contactMech nullable:true
		invoiceDate nullable:false
		dueDate nullable:true
		paidDate nullable:true
		message nullable:true
		referenceNumber nullable:true
		invoiceNumber nullable:true
		currentInvoiceCalculation nullable:true
    }
}
