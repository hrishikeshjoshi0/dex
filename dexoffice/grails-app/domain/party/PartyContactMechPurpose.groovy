package party

import core.ContactMechPurposeType

class PartyContactMechPurpose {
	
	ContactMechPurposeType contactMechPurposeType
	
	static belongsTo = [partyContactMech:PartyContactMech]

    static constraints = {
		partyContactMech nullable:false
		contactMechPurposeType nullable:false 
    }
}
