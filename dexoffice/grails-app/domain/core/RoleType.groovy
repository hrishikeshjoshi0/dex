package core

class RoleType {
	
	String description
	boolean hasTable
	
	static belongsTo = [ parent : StatusType ]
	static hasMany = [children:RoleType]
	static mappedBy = [ parent: "none"]
	
    static constraints = {
		description nullable:false,blank:false
		hasTable nullable:true,blank:true
		parent nullable:true
    }
}
