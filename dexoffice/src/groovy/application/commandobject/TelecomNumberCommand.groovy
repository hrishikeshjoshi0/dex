package application.commandobject

import grails.validation.Validateable
import core.PostalAddress


@Validateable(nullable=true)
class TelecomNumberCommand {
	
	Long id
	Long partyId
	String countryCode
	String areaCode
	String contactNumber
	String contactMechPurpose
	
	static constraints = {
		countryCode nullable:false,blank:false
		areaCode nullable:false,blank:false
		contactNumber nullable:false,blank:false
		contactMechPurpose nullable:false,blank:false
	}

}
