package party



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import org.springframework.http.HttpStatus

import application.commandobject.CreateCustomerCommand
import application.commandobject.EmailAddressCommand
import application.commandobject.PostalAddressCommand
import application.commandobject.TelecomNumberCommand
import core.ContactMech
import core.Enumeration
import core.EnumerationType
import core.PostalAddress
import core.Status
import core.TelecomNumber

@Transactional(readOnly = true)
class CustomerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def contactMechService
	def partyService
	def invoiceService
	def paymentService
	
	def pdfRenderingService
	
	def customerReport() {
		def customer = Party.get(params.id)
		ByteArrayOutputStream bytes = pdfRenderingService.render(template: "/pdf/customer", model: [customer: customer])
	}
	
	def invoiceStatusTypes() {
		def c = Status.createCriteria()
		def invoiceStatusTypes = c.list {
			createAlias("statusType","statusType")
			eq("statusType.description","INVOICE_STATUS")
		}
		respond invoiceStatusTypes,[status:HttpStatus.OK]
	}

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
		def q = params.q
		def result = partyService.getAllPersonCustomers(q)
        respond result, [status: OK]
    }
	
	@Transactional
	def show(Person p) {
		if (p == null) {
			render status: NOT_FOUND
			return
		}
		//renderPdf(template: '/pdf/customer', model: [customer: p], filename: "yourTitle.pdf")
		respond p, [status: OK]
	}
	
	@Transactional
	def saveTelecomNumber(TelecomNumberCommand cmd) {
		if (cmd == null) {
			render status: NOT_FOUND
			return
		}
		
		if(cmd.partyId == null) {
			render status: BAD_REQUEST
			return
		}
		
		def personInstance = Person.get(cmd.partyId)
		
		if(!cmd.id) {
			if(!cmd.contactMechPurpose) {
				cmd.contactMechPurpose = "COMMUNICATION"
			}
			def tn = new TelecomNumber(countryCode:cmd.countryCode,areaCode:cmd.areaCode,contactNumber:cmd.contactNumber)
			def	result = contactMechService.createTelecomNumber(tn,"MOBILE_NUMBER")
			result = partyService.createPartyContactMechAndPurpose(personInstance,tn,cmd.contactMechPurpose,new Date(),null)
			render status : OK
		} else {
			def tn = TelecomNumber.get(cmd.id)
			tn.areaCode = cmd.areaCode
			tn.countryCode = cmd.countryCode
			tn.contactNumber = cmd.contactNumber
			tn.save(flush:true)
			render status : OK
		}
	}
	
	@Transactional
	def saveEmail(EmailAddressCommand cmd) {
		if (cmd == null) {
			render status: NOT_FOUND
			return
		}
		
		if(cmd.partyId == null) {
			render status: BAD_REQUEST
			return
		}
		
		def personInstance = Person.get(cmd.partyId)
		
		if(!cmd.id) {
			def	result = contactMechService.createEmail(cmd.email)
			def email = result.getInstance()
			result = partyService.createPartyContactMechAndPurpose(personInstance,email,"DEFAULT_EMAIL",new Date(),null)
			render status : OK
		} else {
			def cm = ContactMech.get(cmd.id)
			cm.value = cmd.email
			cm.save(flush:true)
			render status : OK
		}
	}
	
	@Transactional
	def savePostalAddress(PostalAddressCommand cmd) {
		if (cmd == null) {
			render status: NOT_FOUND
			return
		}
		
		if(cmd.partyId == null) {
			render status: BAD_REQUEST
			return
		}
		
		def postalAddressId = null
		def postalAddress = new PostalAddress()
		if(cmd.id) {
			postalAddressId = cmd.id
			postalAddress = PostalAddress.get(cmd.id)
		}
		
		postalAddress.address1 = cmd?.address1
		postalAddress.address2 = cmd?.address2
		postalAddress.directions = cmd?.directions
		postalAddress.city = cmd?.city
		postalAddress.postalCode = cmd?.postalCode
		postalAddress.postalCodeExt = cmd?.postalCodeExt
		
		def personInstance = Person.get(cmd.partyId)				
		
		//Create Party Contact mech and purpose only if it is a new postal address.
		if(!postalAddressId) {
			def result = contactMechService.createPostalAddress(postalAddress)
			result = partyService.createPartyContactMechAndPurpose(personInstance,postalAddress,"BILLING_LOCATION",new Date(),null)
			if(!result.isError()) {
				render status: CREATED
			}
		} else {
			postalAddress.save(flush:true)
			println PostalAddress.get(cmd.id)
			 
			render status: CREATED
		}
		
		
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
		if(personInstance.save(flush:true)) {
			cmd.id = personInstance.id
		}
		
		//Create Customer
		//TODO
		def owner = Organization.findByName("-COMPANY-")
		
		def partyRelationshipParams = [:]
		partyRelationshipParams.fromDate = new Date()
		partyRelationshipParams.name = "CUSTOMER"
		def result = partyService.createPartyRelationship owner,personInstance,"PARENT_ORGANIZATION","CUSTOMER","CUSTOMER",partyRelationshipParams
		
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
	
	//Invoices
	def invoices(Party party) {
		def invoices = invoiceService.getInvoicesForParty(party)
		respond invoices, [status: OK]
	}
	
	def payments(Party party) {
		def payments = paymentService.getPaymentsForParty(party)
		respond payments, [status: OK]
	}
}
