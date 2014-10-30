package core

class StatusType {
	
	String description
	boolean hasTable
	
	static belongsTo = [ parent : StatusType ]
	static hasMany = [children:StatusType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		parent nullable:true
		hasTable nullable:true
		description nullable:false,blank:false
    }
}
