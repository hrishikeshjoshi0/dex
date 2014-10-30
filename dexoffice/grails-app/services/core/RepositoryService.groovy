package core

import grails.transaction.Transactional
import party.Party
import party.PartyRelationship;
import party.PartyRelationshipType;
import party.PartyRole;

@Transactional
class RepositoryService {

	def getPartyRole(Party party, RoleType roleType) {
		def c = PartyRole.createCriteria()
		def partyRoles = c.list {
			and {
				eq("party",party)
				eq("roleType",roleType)
			}
		}
		return partyRoles;
	}

	def getLatestPartyStatus(Party party) {
		party.currentPartyStatus
	}

	def getPartyRelationship(Party partyFrom, Party partyTo, String partyRoleTypeFrom, String partyRoleTypeTo, def partyRelationshipType) {
		def c = PartyRelationship.createCriteria()
		def l = c.list {
			and {
				eq("partyFrom",partyFrom)
				eq("partyTo",partyTo)
				eq("roleTypeFrom",RoleType.findByDescription(partyRoleTypeFrom))
				eq("roleTypeTo",RoleType.findByDescription(partyRoleTypeTo))
			}
		}
		return l
	}
}
