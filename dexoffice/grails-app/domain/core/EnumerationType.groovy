package core

class EnumerationType {
	
	String description
	boolean hasTable
	
	static belongsTo = [ parent : EnumerationType ]
	static hasMany = [children:EnumerationType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		description nullable:false,blank:false
		hasTable nullable:true
		parent nullable:true
    }
}
