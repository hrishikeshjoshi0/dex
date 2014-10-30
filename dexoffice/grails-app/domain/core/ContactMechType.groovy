package core

class ContactMechType {
	
	String description

	static belongsTo = [ parent : UomType ]
	static hasMany = [children:ContactMechType]
	static mappedBy = [ parent: "none"]
	
    static constraints = {
		description nullable:false,blank:false
		parent nullable:true
    }
}
