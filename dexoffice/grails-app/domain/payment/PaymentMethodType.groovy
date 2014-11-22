package payment

class PaymentMethodType {

	String code
    String description
	boolean hasTable
	
	static belongsTo = [ parent : PaymentMethodType ]
	static hasMany = [children:PaymentMethodType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		parent nullable:true
		hasTable nullable:true
		code nullable:false,blank:false
		description nullable:true,blank:true
    }
}
