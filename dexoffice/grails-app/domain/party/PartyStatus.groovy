package party

import core.Status

class PartyStatus {
	
	Date fromDate
	Date thruDate
	Party party
	Status status
	
    static constraints = {
		fromDate nullable:false
		thruDate nullable:false
		party nullable:false
		status nullable:true
    }
}
