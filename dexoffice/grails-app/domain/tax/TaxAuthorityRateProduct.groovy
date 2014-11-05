package tax

class TaxAuthorityRateProduct {
	
	BigDecimal taxPercentage
	BigDecimal taxShipping
	Date fromDate
	Date toDate
	TaxType taxType
	
	static belongsTo = [taxAuthority : TaxAuthority]

    static constraints = {
		taxPercentage nullable:true,blank:true
		taxShipping nullable:true,blank:true
		fromDate nullable:false,blank:false
		toDate nullable:true,blank:true
		taxType nullable:false,blank:false
    }
}
