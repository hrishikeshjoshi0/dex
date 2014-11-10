package party

import grails.transaction.Transactional
import core.BaseService
import core.ContactMechPurposeType
import core.RoleType


@Transactional
class PartyService extends BaseService {

	def repositoryService
	
	def getAllPersonCustomers(String name) {
		def results = Person.createCriteria().list {
			and {
				createAlias "partyRelationshipsTo", "partyRelationshipsTo"
				eq("partyRelationshipsTo.roleTypeTo", RoleType.findByDescription("CUSTOMER"))
				le("partyRelationshipsTo.fromDate", new Date())
				or {
					isNull("partyRelationshipsTo.thruDate")
					ge("partyRelationshipsTo.thruDate", new Date())
				}
				if(name) {
					or {
						like("currentFirstName","%" + name+"%")
						like("currentLastName","%" + name+"%")
					}
				}
			}
		}
		
		return results
	}
	
	def createPartyType(String description) {
		def partyType = new PartyType()
		partyType.description = description
		def response = this.saveInstance(partyType, null)
		if(response.isSuccess()) {
			response.addInstance(partyType)
		}
		return response
	}
	
	def createRoleType(String description) {
		def roleTypeInstance = new RoleType()
		roleTypeInstance.description = description
		def response = this.saveInstance(roleTypeInstance, null)
		if(response.isSuccess()) {
			response.addInstance(roleTypeInstance)
		}
		return response
	}

	def createPersonAndRole(Person person,String roleType,def params) {
		def result = this.createPerson(person, params)
		if(result.isError()) {
			return result
		}
		
		result = this.createPartyRole(roleType, person)
		result
	}
	
	def createOrganizationAndRole(Organization organization,String roleType,def params) {
		def result = this.createOrganization(organization, params)
		if(result.isError()) {
			return result
		}
		
		result = this.createPartyRole(roleType, organization)
		result
	}
	
	def createPerson(Person person,def params) {
		def personPartyType = PartyType.findByDescription("PERSON")
		if(!personPartyType) {
			def response = createPartyType("PERSON")
			if(response.isSuccess()) {
				 personPartyType = response.getInstance()
			} else {
				return response
			}
		}
		person.partyType = personPartyType
		def result = this.saveInstance(person, params)
		result
	}

	def createOrganization(Organization organization, def params) {
		def organizationPartyType = PartyType.findByDescription("ORGANIZATION")
		if(!organizationPartyType) {
			def response = createPartyType("ORGANIZATION")
			if(response.isSuccess()) {
				 organizationPartyType = response.getInstance()
			}  else {
				return response
			}
		}
		organization.partyType = organizationPartyType
		def result = this.saveInstance(organization, params)
		result
	}

	def createPartyRole(String roleType,Party party) {
		if(!roleType && !party) {
			return this.createServiceResponse()
		}

		//Find if we have a RoleType instance.
		def roleTypeInstance = RoleType.findByDescription(roleType)
		if(!roleTypeInstance) {
			//Create a new role type
			def result = this.createRoleType(roleType)
			if(!result.isSuccess()) {
				return result
			} else {
				roleTypeInstance = result.getInstance()
			}
		}

		//Create a new PartyRole instance.
		def pr = new PartyRole(party:party,roleType:roleTypeInstance)
		def result = this.saveInstance(pr, null);
		if(!result.isSuccess()) {
			return result
		}
		
		party.addToPartyRoles(pr)
		
		result = this.saveInstance(party, null);
		if(!result.isError()) {
			result.addInstance(party)
		}
		result
	}
	
	def expirePartyRelationship(PartyRelationship partyRelationship, def params) {
		def partyRelationshipParams = createResultMap()
		if(!params.thruDate) {
			partyRelationshipParams.thruDate = new Date()
		} else {
			partyRelationshipParams.thruDate = params.thruDate
		}
		def result = this.saveInstance(partyRelationship, partyRelationshipParams)
		result
	}

	def createPartyRelationship(Party partyFrom, Party partyTo, String partyRoleTypeFrom, String partyRoleTypeTo, def partyRelationshipType,def params) {
		def result = this.createServiceResponse()
		
		if(partyTo == null) {
			throw new IllegalArgumentException("PartyTo instance is null.")
		}

		if(!partyRoleTypeFrom) {
			def partyRoleFrom = repositoryService.getPartyRole(partyFrom, RoleType.findByDescription(partyRoleTypeFrom))
			if(partyRoleFrom) {
				def createPartyRoleResult = createPartyRole(partyRoleTypeFrom, partyFrom)
						
				if(!createPartyRoleResult.isSuccess()) {
					return createPartyRoleResult
				}
			}
		}

		def partyRoleTo = repositoryService.getPartyRole(partyTo, RoleType.findByDescription(partyRoleTypeTo))
		if(!partyRoleTo) {
			def createPartyRoleResult = createPartyRole(partyRoleTypeTo, partyTo)
			
			if(!createPartyRoleResult.isSuccess()) {
				return createPartyRoleResult
			}
		}

		def partyRelationship = repositoryService.getPartyRelationship(partyFrom, partyTo, partyRoleTypeFrom, partyRoleTypeTo, partyRelationshipType)
		if(partyRelationship) {
			//Expire the existing party relationship
			def today = new Date()
			def partyRelationshipParams = [:]
			partyRelationshipParams."thruDate" = today
			expirePartyRelationship(partyRelationship, expirePartyRelationship(partyRelationship, partyRelationshipParams))
		}
		
		def prType = PartyRelationshipType.findByName(partyRelationshipType)
		if(!prType) {
			result.addError("PARTY_RELATIONSHIP_TYPE_NOT_FOUND","Party Relationship type" + partyRelationshipType + " not found.")
			return result
		}

		//Create a new party relationship
		def newPartyRelationship = new PartyRelationship()
		newPartyRelationship.partyRelationshipType = prType
		newPartyRelationship.partyFrom = partyFrom
		newPartyRelationship.partyTo = partyTo
		newPartyRelationship.roleTypeFrom = RoleType.findByDescription(partyRoleTypeFrom)
		newPartyRelationship.roleTypeTo = RoleType.findByDescription(partyRoleTypeTo)
		newPartyRelationship.name = params.name
		newPartyRelationship.fromDate = params.fromDate?params.fromDate:new Date().plus(1)
		newPartyRelationship.thruDate = params.thruDate?params.thruDate:null
		result = this.saveInstance(newPartyRelationship, null)
		result
	}
	
	def setPartyStatus(def party, def status) {
		def result = this.createServiceResponse()
		if(party == null) {
			result.addError("PARTY_NOT_FOUND","Party not found")
		}
		
		def partyStatus = repositoryService.getLatestPartyStatus(party)
		partyStatus.thruDate = new Date()
		def endPartyCurrentStatus = this.saveInstance(partyStatus, null)
		if(!endPartyCurrentStatus.isError()) {
			result.addError("PARTY_STATUS_NOT_ENDED","Could not end party's current status")
			result.messages << endPartyCurrentStatus.messages
			return result
		}
		
		def newPartyStatus = new PartyStatus()
		def now = new Date()
		newPartyStatus.fromDate = now.plus(1)
		newPartyStatus.thruDate = null
		newPartyStatus.party = party
		
		//TODO : Check if the new status is available from the current party status
		newPartyStatus.status = status
		def newPartyStatusResult = this.saveInstance(newPartyStatus, null)
		if(!newPartyStatusResult.isError()) {
			result.addError("PARTY_STATUS_RECORD_NOT_SAVED","Failed to save party status record")
			result.messages << newPartyStatusResult.messages
			return result
		}
		
		party.currentPartyStatus = newPartyStatus
		result = this.saveInstance(party, null)
		
		result
	}
	
	def createPartyContactMechAndPurpose(def party, def contactMech, String contactMechPurpose, def fromDate, def thruDate) {
		def result = this.createServiceResponse()
		if(party == null) {
			result.addError("PARTY_STATUS_NOT_ENDED","Could not end party's current status")
			return result
		}
		
		if(contactMech == null) {
			result.addError("PARTY_STATUS_NOT_ENDED","Could not end party's current status")
			return result
		}
	
		
		def pcm = new PartyContactMech()
		pcm.contactMech = contactMech
		pcm.fromDate = fromDate
		pcm.thruDate = thruDate
		pcm.isVerified = false
		
		party.addToPartyContactMechs(pcm)
		def pcmResult = this.saveInstance(party, null)
		
		if(!pcmResult.isError()) {
			def cmpt = ContactMechPurposeType.findByDescription(contactMechPurpose)
			if(!cmpt) {
				cmpt = new ContactMechPurposeType()
				cmpt.description = contactMechPurpose
				this.saveInstance(cmpt, null)
			}
			
			if(cmpt) {
				def partyContactMechPurpose = new PartyContactMechPurpose()
				partyContactMechPurpose.contactMechPurposeType = cmpt
				pcm.addToPartyContactMechPurpose(partyContactMechPurpose)
				result = this.saveInstance(pcm, null)
			}
		}
		
		return result
	}
}
