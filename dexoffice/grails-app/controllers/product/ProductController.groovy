package product

import javax.transaction.Transactional

import org.springframework.http.HttpStatus

import party.Person;
import tax.TaxCategory;
import application.commandobject.ProductCommand
import core.Uom
import core.UomType

@Transactional
class ProductController {

	static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def productService
	
	def index(Integer max) {
		params.max = Math.min(max ?: 10, 100)
		def result = productService.getProductList(params.q)
		respond result, [status: HttpStatus.OK]
	}
	
	@Transactional
	def update(ProductCommand cmd) {
		if (cmd == null || cmd.id == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		def product = Product.get(cmd.id)
		if (product == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}

		product.validate()
		if (product.hasErrors()) {
			render status: HttpStatus.NOT_ACCEPTABLE
			return
		}
		
		
		mapCommandObject(product, cmd)

		product.save flush:true
		respond cmd, [status: HttpStatus.OK]
	}
	
	@Transactional
	def save(ProductCommand cmd) {
		
		def errors = []
		
		if(cmd == null) {
			respond HttpStatus.NOT_FOUND
		}
		
		def product = new Product()
		mapCommandObject(product, cmd)
		
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

	private mapCommandObject(Product product, ProductCommand cmd) {
		product.productName = cmd.productName
		product.introductionDate = cmd.introductionDate
		product.productType = ProductType.findByName(cmd.productType)
		product.description = cmd.description
		product.quantityUom = Uom.findByAbbreviationAndUomType("EACH", UomType.findByName("PRODUCT_QUANTITY"))
		product.quantityIncluded = cmd.quantityIncluded
		product.piecesIncluded = cmd.piecesIncluded
		product.taxable = cmd.taxable?true:false
		product.taxCategory = TaxCategory.findByName(cmd.taxCategory)
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
