package product

import javax.transaction.Transactional

import org.springframework.http.HttpStatus

import party.Person;
import application.commandobject.ProductCommand
import core.Uom
import core.UomType

@Transactional
class ProductController {

	static responseFormats = ['json', 'xml']
	
	def productService
	
	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		def result = Product.list(params)
		respond result, [status: HttpStatus.OK]
	}
	
	@Transactional
	def save(ProductCommand cmd) {
		
		def errors = []
		
		if(cmd == null) {
			respond HttpStatus.NOT_FOUND
		}
		
		def product = new Product()
		product.productName = cmd.productName
		product.introductionDate = cmd.introductionDate
		product.productType = ProductType.findByName(cmd.productType)
		product.description = cmd.description
		product.quantityUom = Uom.findByAbbreviationAndUomType("EACH", UomType.findByName("PRODUCT_QUANTITY"))
		product.quantityIncluded = cmd.quantityIncluded
		product.piecesIncluded = cmd.piecesIncluded
		product.taxable = cmd.taxable?true:false
		
		product.validate()
		
		if(product.errors) {
			errors << product.errors  
			respond errors,[status:HttpStatus.BAD_REQUEST]
		}
		
		if(product.save(flush:true)) {
			if(cmd?.productPrice) {
				def productPrice = new ProductPrice()
				productPrice.product = product
				productPrice.fromDate = cmd?.productPrice?.fromDate
				productPrice.thruDate = cmd?.productPrice?.thruDate
				productPrice.amount = cmd?.productPrice?.amount
				productPrice.currencyUom = Uom.findByAbbreviationAndUomType("INR", UomType.findByName("CURRENCY"))
				
				def productPriceType = "DEFAULT_PRICE"
				if(cmd?.productPrice?.productPriceType) {
					productPriceType = cmd?.productPrice?.productPriceType
				}
				productPrice.priceType = ProductPriceType.findByValue(productPriceType)
				
				if(!productPrice.save(flush:true)) {
					errors << productPrice.errors
				}
			}
			
			if(errors.isEmpty()) {
				respond cmd,[status:HttpStatus.OK]
			} else {
				respond errors,[status:HttpStatus.MULTI_STATUS]
			}
		}
	}
	
	@Transactional
	def show(Product p) {
		if (p == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		respond p, [status: HttpStatus.OK]
	}
}
