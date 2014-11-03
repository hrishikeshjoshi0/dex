package product

import core.Uom


class ProductPrice {
	
	Date fromDate
	Date thruDate
	ProductPriceType priceType
	BigDecimal amount
	Uom currencyUom
	
	static belongsTo = [product : Product]

    static constraints = {
		fromDate nullable:false,blank:false
		thruDate nullable:true,blank:true
		priceType nullable:false
		amount nullable:false
		currencyUom nullable:true
    }
}
