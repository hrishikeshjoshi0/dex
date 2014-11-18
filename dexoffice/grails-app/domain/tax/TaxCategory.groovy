package tax

class TaxCategory {
	
	String name
	String description
	
	static hasMany = [taxRates : TaxRate]

    static constraints = {
		name nullable:false
		description nullable:false
    }
	
}
