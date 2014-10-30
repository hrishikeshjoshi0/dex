import org.springframework.web.context.support.WebApplicationContextUtils

import party.PartyRelationshipType
import party.PartyType
import party.Person
import core.ContactMechType
import core.Enumeration
import core.EnumerationType
import core.RoleType

class BootStrap {
	
	def partyService

    def init = { servletContext ->
		def springContext = WebApplicationContextUtils.getWebApplicationContext(servletContext)
		
		//Person
		if(!PartyType.findByDescription("Person")) {
			def pt = new PartyType()
			pt.description = "Person"
			pt.save(flush:true)
		}
		
		if(!PartyType.findByDescription("Organization")) {
			def pt = new PartyType()
			pt.description = "Organization"
			pt.save(flush:true)
		}
		
		//Enumeration Types
		if(!EnumerationType.findByDescription("Gender")) {
			def et = new EnumerationType(description : "Gender")
			et.save(flush:true)
		}
		
		//Enumerations
		if(!Enumeration.findByEnumCodeAndEnumerationType("M",EnumerationType.findByDescription("Gender"))) {
			def e = new Enumeration()
			e.enumCode = "M"
			e.description = "Male"
			e.enumerationType = EnumerationType.findByDescription("Gender")
			e.save(flush:true)
		}
		
		if(!Enumeration.findByEnumCodeAndEnumerationType("F",EnumerationType.findByDescription("Gender"))) {
			def e = new Enumeration()
			e.enumCode = "F"
			e.description = "Female"
			e.enumerationType = EnumerationType.findByDescription("Gender")
			e.save(flush:true)
		}
		
		//Contact Mech Types
		if(!ContactMechType.findByDescription("POSTAL_ADDRESS")) {
			def cmt = ContactMechType.findOrCreateByDescription("POSTAL_ADDRESS")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("EMAIL")) {
			def cmt = ContactMechType.findOrCreateByDescription("EMAIL")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("MOBILE_NUMBER")) {
			def cmt = ContactMechType.findOrCreateByDescription("MOBILE_NUMBER")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("RESIDENCE_NUMBER")) {
			def cmt = ContactMechType.findOrCreateByDescription("RESIDENCE_NUMBER")
			cmt.save(flush:true)
		}
		
		if(!ContactMechType.findByDescription("OTHER_PHONE")) {
			def cmt = ContactMechType.findOrCreateByDescription("OTHER_PHONE")
			cmt.save(flush:true)
		}
		
		//Role Type
		if(!RoleType.findByDescription("OWNER")) {
			def rt = new RoleType(description:"OWNER")
			rt.save(flush:true)
		}
		
		if(!RoleType.findByDescription("CUSTOMER")) {
			def rt = new RoleType(description:"CUSTOMER")
			rt.save(flush:true)
		}
		
		if(!RoleType.findByDescription("LEAD")) {
			def rt = new RoleType(description:"LEAD")
			rt.save(flush:true)
		}
		
		//Party Relationship Type
		if(!PartyRelationshipType.findByNameAndValidRoleTypeFromAndValidRoleTypeTo("CUSTOMER",RoleType.findByDescription("OWNER"),RoleType.findByDescription("CUSTOMER"))) {
			def prType = new PartyRelationshipType()
			prType.description = "Customer Relationship"
			prType.name = "CUSTOMER"
			prType.validRoleTypeFrom = RoleType.findByDescription("OWNER")
			prType.validRoleTypeTo = RoleType.findByDescription("CUSTOMER")
			prType.save(flush:true)
		}
		
		//Dummy Owner
		if(!Person.findByCurrentFirstNameAndCurrentLastName("Admin","User")) {
			def p = new Person()
			p.currentFirstName = "Admin"
			p.currentLastName = "User"
			p.description = "Admin User"
			partyService.createPersonAndRole(p,"OWNER",null)
		}
		
		//Marshalling
		springContext.getBean("customObjectMarshallers").register()
    }
	
    def destroy = {
    }
}
