package core

import java.util.Date;

class CommunicationEventStatus {

    Date fromDate
	Date thruDate

	CommunicationEvent communicationEvent
	Status status
	
    static constraints = {
		fromDate nullable:false
		thruDate nullable:true
		communicationEvent nullable:false
		status nullable:false
    }
}
