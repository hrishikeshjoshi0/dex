package application.commandobject

import grails.validation.Validateable
import core.PostalAddress


@Validateable(nullable=true)
class EmailAddressCommand {
	
	Long id
	Long partyId
	String email
	String contactMechPurpose
	
	static constraints = {
		email nullable:false,blank:false
		contactMechPurpose nullable:false,blank:false
	}

}
