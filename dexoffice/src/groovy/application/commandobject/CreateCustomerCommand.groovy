/**
 * 
 */
package application.commandobject

import grails.validation.Validateable
import grails.web.RequestParameter

import org.grails.databinding.BindingFormat
import org.springframework.web.bind.annotation.RequestParam;

import party.Person

/**
 * @author hrishi
 *
 */
@Validateable(nullable=true)
class CreateCustomerCommand {
	
	Long id
	String currentFirstName
	String currentMiddleName
	String currentLastName
	@BindingFormat("dd-MM-yyyy")
	Date birthDate
	String gender
	String maritalStatus
	String description
	PostalAddressCommand postalAddress
	
	String email
	String mobileNumber
	String otherNumber
	
	static constraints = {
		importFrom Person
		email nullable:true,blank:true
		mobileNumber nullable:true,blank:true
		otherNumber nullable:true,blank:true
	}
}
