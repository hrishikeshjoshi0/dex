package tax

import party.Party


class TaxRate {
	
	Party party
	String name
	Date fromDate
	Date thruDate
	TaxAuthority taxAuthority
	TaxType taxType
	BigDecimal taxPercentage
	BigDecimal taxShipping
	
	static belongsTo = [taxCategory : TaxCategory]

    static constraints = {
		party nullable:true,blank:true
		taxPercentage nullable:true,blank:true
		taxShipping nullable:true,blank:true
		fromDate nullable:false,blank:false
		thruDate nullable:true,blank:true
		taxType nullable:false,blank:false
		taxAuthority nullable:false,blank:false
    }
}
