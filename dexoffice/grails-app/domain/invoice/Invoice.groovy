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
	
	static hasMany = [statuses : InvoiceStatus,items:InvoiceItem]

    static constraints = {
		partyFrom nullable:false
		party nullable:false
		currentInvoiceStatus nullable:false
		contactMech nullable:false
		invoiceDate nullable:false
		dueDate nullable:true
		paidDate nullable:true
		message nullable:true
		referenceNumber nullable:true
    }
}
