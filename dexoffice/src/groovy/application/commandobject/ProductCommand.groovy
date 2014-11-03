package application.commandobject

import grails.validation.Validateable;
import product.ProductCategoryType
import product.ProductPrice;
import product.ProductType
import core.Uom

@Validateable(nullable=true)
class ProductCommand {
	
	String productType
	String primaryProductCategoryType
	Date introductionDate
	String productName
	String description
	String quantityUom
	BigDecimal quantityIncluded
	BigDecimal piecesIncluded
	Boolean taxable
	
	ProductPriceCommand productPrice

}
