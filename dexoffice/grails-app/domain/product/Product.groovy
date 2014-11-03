package product

import core.Uom

class Product {
	
	ProductType productType
	ProductCategoryType primaryProductCategoryType
	Date introductionDate
	Date salesTerminationDate
	String productName
	String description
	Uom quantityUom
	BigDecimal quantityIncluded
	BigDecimal piecesIncluded
	Boolean taxable
	Uom heightUom
	BigDecimal height
	Uom lengthUom
	BigDecimal length
	Uom depthUom
	BigDecimal depth
	Uom weightUom
	BigDecimal weight
	
	static hasMany = [productPrices : ProductPrice]
	
    static constraints = {
		productType nullable:false
		primaryProductCategoryType nullable:true
		introductionDate nullable:false
		productName nullable:false,blank:false
		description nullable:true,blank:true,maxLength:1000
		quantityIncluded nullable:true,blank:true
		piecesIncluded nullable:true,blank:true
		description nullable:true,blank:true
		taxable nullable:false,blank:false
		heightUom nullable:true,blank:true
		height nullable:true,blank:true
		lengthUom nullable:true,blank:true
		length nullable:true,blank:true
		depthUom nullable:true,blank:true
		depth nullable:true,blank:true
		weightUom nullable:true,blank:true
		weight nullable:true,blank:true
    }
	
	
}
