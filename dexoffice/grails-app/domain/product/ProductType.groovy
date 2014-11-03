package product

import grails.rest.Resource

@Resource(uri='/api/productTypes',formats=["json","xml"])
class ProductType {

    String name
	String description
	
	static belongsTo = [ parent : ProductType ]
	static hasMany = [children:ProductType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		name nullable:true,blank:true
		description nullable:true,blank:true
		parent nullable:true 
    }
}
