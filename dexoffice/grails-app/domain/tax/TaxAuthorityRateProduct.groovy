package tax

import product.Product

class TaxAuthorityRateProduct {
	
	BigDecimal taxPercentage
	BigDecimal taxShipping
	Date fromDate
	Date toDate
	TaxType taxType
	Product product
	TaxAuthority taxAuthority

    static constraints = {
		taxPercentage nullable:true,blank:true
		taxShipping nullable:true,blank:true
		fromDate nullable:false,blank:false
		toDate nullable:true,blank:true
		taxType nullable:false,blank:false
		taxAuthority nullable:false,blank:false
    }
}
