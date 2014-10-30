package party


import spock.lang.*
import core.ContactMechType
import core.PostalAddress
import core.RoleType
import core.TelecomNumber

/**
 *
 */
class PartyTestsSpec extends Specification {
	
	def partyService
	
	def contactMechService

    def setup() {
    }

    def cleanup() {
    }

    private Person createNewPersonParty(String firstName, String lastName, String description, String partyLocalNumber) {
    	def p = new Person()
    	p.currentFirstName = firstName
    	p.currentLastName = lastName
    	p.description = description
    	p.partyNumberLocal = partyLocalNumber
    	return p
    }
	
	private Organization createNewOrganizationParty(String name, String description, String partyLocalNumber) {
		def o = new Organization()
		o.description = description
		o.name = name
		return o
	}
	
    void "Create new Party"() {
		def p = createNewPersonParty("John","Smith","Description","P0001")
		def result = partyService.createPerson(p,null)
		expect : 
		result.isError() == false
		Person.findByCurrentFirstNameAndCurrentLastName("John","Smith") != null
    }

	
	void "Create new organization"() {
		def o = createNewOrganizationParty("John Smith & Co.","Description","P0001")
		def result = partyService.createOrganization(o,null)
		expect :
		result.isError() == false
		Organization.findByName("John Smith & Co.") != null
	}

	void "Create party role"() {
		def p = createNewPersonParty("John","Smith","Description","P0001")
		def result = partyService.createPersonAndRole(p,"LEAD",null)
		expect :
		result.isError() == false
		Person.findByCurrentFirstNameAndCurrentLastName("John","Smith") != null
		def rt = RoleType.findByDescription("LEAD")
		rt != null
		PartyRole.findByPartyAndRoleType(p, rt) != null		
	}
	
	void "Create organization role"() {
		def o = createNewOrganizationParty("John Smith & Co.","Description","P0001")
		def result = partyService.createOrganizationAndRole(o,"ACCOUNT_LEAD",null)
		expect :
		result.isError() == false
		Organization.findByName("John Smith & Co.") != null
		def rt = RoleType.findByDescription("ACCOUNT_LEAD")
		rt != null
		PartyRole.findByPartyAndRoleType(o, rt) != null
	}
	
	void "Create party relationship"() {
		setup:
			partyService.createRoleType "OWNER"
			partyService.createRoleType "ACCOUNT_LEAD"
			def prType = new PartyRelationshipType()
			prType.name = "LEAD_REL"
			prType.description = "Lead Relationship"
			prType.validRoleTypeFrom = RoleType.findByDescription("OWNER")
			prType.validRoleTypeTo = RoleType.findByDescription("ACCOUNT_LEAD")
			def result = partyService.saveInstance(prType,null)
		when:
			def p = createNewPersonParty("John","Smith","Description","P0001")
			result = partyService.createPersonAndRole(p,"OWNER",null)
			def o = createNewOrganizationParty("John Smith & Co.","Description","A0001")
			result = partyService.createOrganizationAndRole(o,"ACCOUNT_LEAD",null)
			
			def partyRelationshipParams = [:]
			partyRelationshipParams.fromDate = new Date()
			partyRelationshipParams.name = "Example"
			result = partyService.createPartyRelationship p,o,"OWNER","ACCOUNT_LEAD","LEAD_REL",partyRelationshipParams 
		then: 
			result.isError() == false
		expect:
			def pr = PartyRelationship.findByName("Example")
			pr != null
			pr.fromDate?.clearTime() == new Date().clearTime()
			pr.partyFrom == p
			pr.partyTo == o
			pr.roleTypeFrom == RoleType.findByDescription("OWNER")
			pr.roleTypeTo == RoleType.findByDescription("ACCOUNT_LEAD")
	}
	
	void "create postal address"() {
		setup:
			def p = createNewPersonParty("John","Smith","Description","P0001")
			def result = partyService.createPerson(p,null)
			
			def cmt = ContactMechType.findOrCreateByDescription("POSTAL_ADDRESS")
			contactMechService.saveInstance(cmt,null)
			
			def postalAddress = new PostalAddress()
			postalAddress.address1 = "Address 1"
			postalAddress.toName = "John Smith"
			
			result = contactMechService.createPostalAddress(postalAddress)
		expect :
			result.isError() == false
			PostalAddress.findByToName("John Smith") != null	
	}
	
	void "create postal address and purpose"() {
		setup:
			def p = createNewPersonParty("John","Smith","Description","P0001")
			def result = partyService.createPerson(p,null)
			
			def cmt = ContactMechType.findOrCreateByDescription("POSTAL_ADDRESS")
			contactMechService.saveInstance(cmt,null)
			
			def postalAddress = new PostalAddress()
			postalAddress.address1 = "Address 1"
			postalAddress.toName = "John Smith"
			result = contactMechService.createPostalAddress(postalAddress)
			result = partyService.createPartyContactMechAndPurpose(p,postalAddress,"BILLING_LOCATION",new Date(),null)
		expect :
			result.isError() == false
			def person = Person.findByCurrentFirstNameAndCurrentLastName("John","Smith")
			person != null
			person.partyContactMechs != null
			def pcm = person.partyContactMechs.first()
			pcm != null
			pcm.contactMech != null
			pcm.contactMech.contactMechType == ContactMechType.findByDescription("POSTAL_ADDRESS") 
	}
	
	void "create email address and purpose"() {
		setup:
			def p = createNewPersonParty("John","Smith","Description","P0001")
			def result = partyService.createPerson(p,null)
			
			def cmt = ContactMechType.findOrCreateByDescription("POSTAL_ADDRESS")
			contactMechService.saveInstance(cmt,null)
			
			cmt = ContactMechType.findOrCreateByDescription("EMAIL")
			contactMechService.saveInstance(cmt,null)
			
			result = contactMechService.createEmail("test@dexbiz.com")
			def email = result.getInstance()
			
			result = partyService.createPartyContactMechAndPurpose(p,email,"BILLING_EMAIL",new Date(),null)
		expect :
			result.isError() == false
			def person = Person.findByCurrentFirstNameAndCurrentLastName("John","Smith")
			person != null
			person.partyContactMechs != null
			def pcm = person.partyContactMechs.first()
			pcm != null
			pcm.contactMech != null
			pcm.contactMech.contactMechType == ContactMechType.findByDescription("EMAIL")
			pcm.contactMech.value == "test@dexbiz.com"
	}
	
	void "create phone number and purpose"() {
		setup:
			def p = createNewPersonParty("John","Smith","Description","P0001")
			def result = partyService.createPerson(p,null)
			
			def cmt = ContactMechType.findOrCreateByDescription("MOBILE_NUMBER")
			contactMechService.saveInstance(cmt,null)
			
			def tn = new TelecomNumber(countryCode:"91",areaCode:"022",contactNumber:"9898989898")
			result = contactMechService.createTelecomNumber(tn,"MOBILE_NUMBER")
			result = partyService.createPartyContactMechAndPurpose(p,tn,"BILLING_LOCATION",new Date(),null)
		expect :
			result.isError() == false
			def person = Person.findByCurrentFirstNameAndCurrentLastName("John","Smith")
			person != null
			person.partyContactMechs != null
			def pcm = person.partyContactMechs.first()
			pcm != null
			pcm.contactMech != null
			pcm.contactMech.contactMechType == ContactMechType.findByDescription("MOBILE_NUMBER")
			pcm.contactMech.countryCode == "91"
			pcm.contactMech.areaCode == "022"
			pcm.contactMech.contactNumber == "9898989898"
	}
}
