package party

import core.RoleType

class PartyRole {
	
	RoleType roleType
	Party party
	 
    static constraints = {
		roleType nullable:false
		party nullable:false
    }
}
