package party

import core.ContactMech

class PartyContactMech {
	
	Date fromDate
	Date thruDate
	boolean isVerified
	
	Party party
	ContactMech contactMech
	
	static hasMany = [partyContactMechPurpose : PartyContactMechPurpose]

    static constraints = {
		fromDate nullable:false
		thruDate nullable:true
		isVerified nullable:true
		
		party nullable:false
		contactMech nullable:false
    }
	
}
