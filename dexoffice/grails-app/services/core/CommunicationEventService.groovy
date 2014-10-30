package core

import grails.transaction.Transactional

@Transactional
class CommunicationEventService extends BaseService {

    def createCommunicationEvent(CommunicationEvent communicationEvent,String type,String purpose) {
		def result = this.createResultMap()
		
		if(type == null) {
			//Error
			result.addError("PARAM_COMMUNICATION_EVENT_NOT_FOUND","CommunicationEvent " + purpose + " not found.")
			return result
		}
		
		if(purpose == null) {
			//purpose
			result.addError("PARAM_COMMUNICATION_EVENT_PURPOSE_TYPE_NOT_FOUND","CommunicationEventPurposeType " + purpose + " not found.")
			return result
		}
		
		def communicationEventPurposeType = CommunicationEventPurposeType.findByDescription(purpose);
		if(!communicationEventPurposeType) {
			//Create
			result.addError("PARAM_COMMUNICATION_EVENT_PURPOSE_TYPE_NOT_FOUND","CommunicationEventPurposeType " + purpose + " not found.")
			return result
		}
		
		//Create Communication Event.
		def communicationEventPurpose = new CommunicationEventPurpose()
		communicationEventPurpose.communicationEventPurposeType = communicationEventPurposeType
		communicationEventPurpose.communicationEvent = communicationEvent
		def communicationEventPurposeResult = this.saveInstance(communicationEventPurpose, null)
		
		if(!communicationEventPurposeResult.isError()) {
			return communicationEventPurposeResult
		}
		
		def communicationEventType = CommunicationEventType.findByDescription(type);
		if(!communicationEventType) {
			//Create
			result.addError("COMMUNICATION_EVENT_TYPE","COMMUNICATION_EVENT_TYPE " + type + " not found.")
			return result
		}
		communicationEvent.communicationEventType = communicationEventType	
		return this.saveInstance(communicationEvent, null);
    }
}
