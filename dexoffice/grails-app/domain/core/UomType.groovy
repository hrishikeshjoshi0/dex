package core

class UomType {
	
	String name
	String description
	
	static belongsTo = [ parent : UomType ]
	static hasMany = [children:UomType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		name nullable:true,blank:true
		description nullable:true,blank:true
		parent nullable:true 
    }
}
