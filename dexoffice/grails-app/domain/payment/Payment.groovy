package payment

import party.Party
import core.Status

class Payment {
	
	PaymentType paymentType
	PaymentMethod paymentMethod
	Party partyFrom
	Party partyTo
	Date effectiveDate
	String paymentRefNumber
	BigDecimal amount
	String comments
	Status status
	PaymentStatus currentStatus
	
	static hasMany = [paymentApplications : PaymentApplication,statuses:PaymentStatus]

    static constraints = {
		paymentType nullable:true
		paymentMethod nullable:true
		partyFrom nullable:false
		partyTo nullable:false
		effectiveDate nullable:true
		paymentRefNumber nullable:true
		amount nullable:false
		comments nullable:true
		status nullable:true
    }
}
