package party

import core.RoleType;

class PartyRelationshipType {
	
	String name
	String description
	RoleType validRoleTypeFrom
	RoleType validRoleTypeTo
	boolean hasTable
	
	static belongsTo = [ parent : PartyRelationshipType ]
	static hasMany = [children:PartyRelationshipType]
	static mappedBy = [ parent: "none"]
	
    static constraints = {
		name nullable:false
		description nullable:true
		parent nullable:true
		validRoleTypeFrom nullable:false
		validRoleTypeTo nullable:false 
		hasTable nullable:true
    }
}
