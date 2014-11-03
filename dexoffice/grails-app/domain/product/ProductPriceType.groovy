package product

import grails.rest.Resource

@Resource(uri='/api/productPriceTypes',formats=["json","xml"])
class ProductPriceType {
	
	String value
	String description

    static constraints = {
		value nullable:false,blank:false
		description nullable:false,blank:false
    }
}
