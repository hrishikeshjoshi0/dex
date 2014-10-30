package core

import party.Party

class CommunicationEvent {
	
	Date dateTimeStarted
	Date dateTimeEnded
	String content
	String header
	Status status
	CommunicationEventType communicationEventType 
	ContactMechType contactMechType
	ContactMech contactMechTo
	ContactMech contactMechFrom
	RoleType roleTypeFrom
	RoleType roleTypeTo
	Party partyFrom
	Party partyTo
	String reason
	String note
	String mimeType
	
	static belongsTo = [parent:CommunicationEvent]
	static hasMany = [communicationStatuses:CommunicationEventStatus,children:CommunicationEvent]
	static hasOne = [communicationEventPurpose:CommunicationEventPurpose]
	
    static constraints = {
		dateTimeStarted nullable:false
		dateTimeEnded nullable:true
		content nullable:true,blank:true
		header nullable:false,blank:false
		communicationEventType nullable:false
		contactMechType nullable:false
		contactMechTo nullable:false
		contactMechFrom nullable:true
		roleTypeFrom nullable:true
		roleTypeTo nullable:false
		partyFrom nullable:false
		partyTo nullable:false
		reason nullable:false,blank:false
		note nullable:false,blank:false
		mimeType nullable:true
		parent nullable:true
    }
}
