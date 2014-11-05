package party

import tax.PartyTaxAuthInfo

class Party {
	
	String description
	PartyType partyType
	PartyStatus currentPartyStatus
	
	static hasMany = [partyRoles:PartyRole, partyRelationshipsFrom:PartyRelationship,
		partyRelationshipsTo:PartyRelationship,partyClassifications:PartyClassification,partyContactMechs:PartyContactMech,
		partyStatuses:PartyStatus,partyNotes:PartyNote, partyTaxAuthInfo : PartyTaxAuthInfo]
	
	static mappedBy = [partyRelationshipsFrom: 'partyFrom', partyRelationshipsTo: 'partyTo']
	
    static constraints = {
		description nullable:true,blank:true,maxLength:1000
		partyType nullable:true
		currentPartyStatus nullable:true
    }
	
	static mapping = {
		tablePerHierarchy false
	}
}
