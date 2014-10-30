package core

class CommunicationEventPurpose {
	
	CommunicationEventPurposeType communicationEventPurposeType
	CommunicationEvent communicationEvent

    static constraints = {
		communicationEventPurposeType nullable:false
		communicationEvent nullable:false
    }
}
