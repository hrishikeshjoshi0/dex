package tax

import party.Party


class TaxRate {
	
	Party party
	String name
	Date fromDate
	Date toDate
	TaxAuthority taxAuthority
	BigDecimal taxPercentage
	BigDecimal taxShipping
	
	static belongsTo = [taxCategory : TaxCategory]

    static constraints = {
		party nullable:true,blank:true
		taxPercentage nullable:true,blank:true
		taxShipping nullable:true,blank:true
		fromDate nullable:false,blank:false
		toDate nullable:true,blank:true
		taxAuthority nullable:false,blank:false
    }
}
