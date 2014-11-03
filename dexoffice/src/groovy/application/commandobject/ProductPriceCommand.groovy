package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class ProductPriceCommand {

	Long id
	Long productId
	String productPriceType
	String currency
	Date fromDate
	Date thruDate
	BigDecimal amount
	String currencyUom
	
	static constraints = {
		productPriceType nullable:false,blank:false
		currency nullable:false,blank:false
		fromDate nullable:false,blank:false
		thruDate nullable:true,blank:true
		amount nullable:false,blank:false
		currencyUom nullable:true,blank:true
	}
	
}
