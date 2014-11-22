package payment

class PaymentType {

    String code
    String description
	boolean hasTable
	
	static belongsTo = [ parent : PaymentType ]
	static hasMany = [children:PaymentType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		parent nullable:true
		hasTable nullable:true
		code nullable:false,blank:false
		description nullable:true,blank:true
    }
}
