package application.commandobject

import grails.validation.Validateable
import core.PostalAddress


@Validateable(nullable=true)
class PostalAddressCommand {
	
	Long id
	Long partyId
	String toName
	String address1
	String address2
	String directions
	String city
	String postalCode
	String postalCodeExt
	String country
	
	static constraints = {
		importFrom PostalAddress
	}

}
