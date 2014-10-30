package core

import grails.transaction.Transactional

@Transactional
class ContactMechService extends BaseService {

    def createPostalAddress(PostalAddress postalAddress) {
		def cmt = ContactMechType.findByDescription("POSTAL_ADDRESS")
		if(!cmt) {
			def cmtResult = this.createServiceResponse()
			cmtResult.addError("CONTACT_MECH_TYPE_NOT_FOUND","Contact Mech Type " + "POSTAL_ADDRESS" + " not found.") 
			return cmtResult
		}
		
		postalAddress.contactMechType = ContactMechType.findByDescription("POSTAL_ADDRESS")
		if(!postalAddress.toName) {
			postalAddress.toName = " "
		} 
		
		def result = this.saveInstance(postalAddress, null)
		result.addInstance(postalAddress)
		return result
    }
	
	def createTelecomNumber(TelecomNumber tn, String type) {
		def cmt = ContactMechType.findByDescription(type)
		if(!cmt) {
			def cmtResult = this.createServiceResponse()
			cmtResult.addError("CONTACT_MECH_TYPE_NOT_FOUND","Contact Mech Type " + type + " not found.")
			return cmtResult
		}
		
		tn.contactMechType = cmt
		def result = this.saveInstance(tn, null)
		result.addInstance(tn)
		return result
	}
	
	def createContactMech(ContactMech contactMech,def params) {
		def result = this.createServiceResponse()
		if(params.contactMechType == 'POSTAL_ADDRESS') {
			result.addError("INVALID_SERVICE_METHOD","Cannot use this service method to create Postal address")
			return result
		} else if(params.contactMechType == 'TELECOM_NUMBER') {
			result.addError("INVALID_SERVICE_METHOD","Cannot use this service method to create Telecom number")
			return result
		} else {
			result = this.saveInstance(contactMech, params)
			result.addInstance(contactMech)
		}
		return result
	}
	
	def createEmail(String email) {
		def contactMech = new ContactMech()
		contactMech.value = email
		contactMech.contactMechType = ContactMechType.findByDescription("EMAIL")
		def result = this.saveInstance(contactMech, null)
		result.addInstance(contactMech)
		return result
	}
}
