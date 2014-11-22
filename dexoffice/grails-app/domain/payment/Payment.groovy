package payment

import party.Party

class Payment {
	
	PaymentType paymentType
	PaymentMethod paymentMethod
	Party partyFrom
	Party partyTo
	Date effectiveDate
	String paymentRefNumber
	BigDecimal amount
	String comments
	
	static hasMany = [paymentApplications : PaymentApplication]

    static constraints = {
		paymentType nullable:true
		paymentMethod nullable:true
		partyFrom nullable:false
		partyTo nullable:false
		effectiveDate nullable:true
		paymentRefNumber nullable:true
		amount nullable:false
		comments nullable:true
    }
}
