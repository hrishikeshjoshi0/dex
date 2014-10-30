package party

import core.RoleType

class PartyRelationship {
	
	String name
	PartyRelationshipType partyRelationshipType
	Date fromDate
	Date thruDate
	String priority
	RoleType roleTypeFrom
	RoleType roleTypeTo

	Party partyFrom
	Party partyTo

    static constraints = {
		name nullable:false
		partyRelationshipType nullable:false
		fromDate nullable:false
		thruDate nullable:true
		priority nullable:true
		roleTypeFrom nullable:false
		roleTypeTo nullable:false
		partyFrom nullable:false
		partyTo nullable:false
    }
}
