package tax

import party.Party

class PartyTaxAuthInfo {
	
	Party party
	TaxAuthority taxAuthority

    static constraints = {
		party nullable:false
		taxAuthority nullable:false
    }
}
