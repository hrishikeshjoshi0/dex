package invoice

class InvoiceItemType {
	
	String name
	String description
	
	static belongsTo = [ parent : InvoiceItemType ]
	static hasMany = [children:InvoiceItemType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		name nullable:true,blank:true
		description nullable:true,blank:true
		parent nullable:true 
    }
	
}
