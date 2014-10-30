package core

class Uom {
	
	UomType uomType
	String abbreviation
	String description
	
    static constraints = {
		uomType nullable:false
		abbreviation nullable:false,blank:false
		description nullable:false,blank:false
    }
}
