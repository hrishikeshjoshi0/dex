package product

class ProductCategoryType {

    String name
	String description
	
	static belongsTo = [ parent : ProductCategoryType ]
	static hasMany = [children:ProductCategoryType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		name nullable:true,blank:true
		description nullable:true,blank:true
		parent nullable:true 
    }
}
