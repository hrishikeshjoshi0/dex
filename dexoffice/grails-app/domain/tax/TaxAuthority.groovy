package tax

import party.Party
import core.Geo

class TaxAuthority {
	
	Party party
	Geo geo
	
    static constraints = {
		party nullable:false
		geo nullable:false
    }
}
