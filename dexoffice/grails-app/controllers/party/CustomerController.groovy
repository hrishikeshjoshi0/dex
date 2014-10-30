package party



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import application.commandobject.CreateCustomerCommand
import core.Enumeration
import core.EnumerationType
import core.PostalAddress
import core.TelecomNumber

@Transactional(readOnly = true)
class CustomerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def contactMechService
	def partyService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		def result = partyService.getAllPersonCustomers()
        respond result, [status: OK]
    }
	
	@Transactional
	def show(Person p) {
		if (p == null) {
			render status: NOT_FOUND
			return
		}
		
		respond p, [status: OK]
	}

    @Transactional
    def save(CreateCustomerCommand cmd) {
		if (cmd == null) {
            render status: NOT_FOUND
            return
        }
		
		def personInstance = new Person()
		personInstance.partyType = PartyType.findByDescription("Person")
		personInstance.currentFirstName = cmd.currentFirstName
		personInstance.currentLastName = cmd.currentLastName
		personInstance.currentMiddleName = cmd.currentMiddleName
		personInstance.birthDate = cmd.birthDate
		personInstance.gender = Enumeration.findByEnumCodeAndEnumerationType(cmd.gender,EnumerationType.findByDescription("Gender"))

		if(cmd.maritalStatus) {
			personInstance.maritalStatus = Enumeration.findByEnumCodeAndEnumerationType(cmd.maritalStatus,EnumerationType.findByDescription("MaritalStatus"))
		}
		personInstance.description = cmd.description
		
		//Validate
		personInstance.validate()
		
		if (personInstance.hasErrors()) {
			render status: NOT_ACCEPTABLE
			return
		}
		personInstance.save flush:true
		
		//Create Customer
		def owner = Person.findByCurrentFirstName("Admin")
		
		def partyRelationshipParams = [:]
		partyRelationshipParams.fromDate = new Date()
		partyRelationshipParams.name = "CUSTOMER"
		def result = partyService.createPartyRelationship owner,personInstance,"OWNER","CUSTOMER","CUSTOMER",partyRelationshipParams
		
		//Create Postal Address
		if(cmd?.postalAddress) {
			def postalAddress = new PostalAddress()
			postalAddress.address1 = cmd?.postalAddress?.address1
			postalAddress.address2 = cmd?.postalAddress?.address2
			postalAddress.directions = cmd?.postalAddress?.directions
			postalAddress.city = cmd?.postalAddress?.city
			postalAddress.postalCode = cmd?.postalAddress?.postalCode
			postalAddress.postalCodeExt = cmd?.postalAddress?.postalCodeExt
															
			result = contactMechService.createPostalAddress(postalAddress)
			result = partyService.createPartyContactMechAndPurpose(personInstance,postalAddress,"BILLING_LOCATION",new Date(),null)
		}
		
		//Create email,mobile and other phone
		if(cmd.email) {
			result = contactMechService.createEmail(cmd.email)
			def email = result.getInstance()
			result = partyService.createPartyContactMechAndPurpose(personInstance,email,"DEFAULT_EMAIL",new Date(),null)
		}
		
		if(cmd.mobileNumber) {
			def tn = new TelecomNumber(countryCode:"",areaCode:"",contactNumber:cmd.mobileNumber)
			result = contactMechService.createTelecomNumber(tn,"MOBILE_NUMBER")
			result = partyService.createPartyContactMechAndPurpose(personInstance,tn,"COMMUNICATION",new Date(),null)
		}
		
		if(cmd.otherNumber) {
			def tn = new TelecomNumber(countryCode:"",areaCode:"",contactNumber:cmd.otherNumber)
			result = contactMechService.createTelecomNumber(tn,"OTHER_PHONE")
			result = partyService.createPartyContactMechAndPurpose(personInstance,tn,"COMMUNICATION",new Date(),null)
		}
		
		respond cmd, [status: CREATED]
    }

    @Transactional
    def update(Person personInstance) {
        if (personInstance == null) {
            render status: NOT_FOUND
            return
        }

        personInstance.validate()
        if (personInstance.hasErrors()) {
            render status: NOT_ACCEPTABLE
            return
        }

        personInstance.save flush:true
        respond personInstance, [status: OK]
    }

    @Transactional
    def delete(Person personInstance) {

        if (personInstance == null) {
            render status: NOT_FOUND
            return
        }
		
		personInstance.removed = true
		personInstance.save flush:true
		
        render status: NO_CONTENT
    }
}
