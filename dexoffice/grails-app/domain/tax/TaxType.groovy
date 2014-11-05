package tax

class TaxType {
	
	String name
	String description

    static constraints = {
		name nullable:false,blank:false
		description nullable:true,blank:true
    }
}
