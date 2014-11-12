package product

import application.commandobject.ProductPriceCommand;
import grails.transaction.Transactional

@Transactional
class ProductService {

    def createProduct(Product product) {
		product.validate()
		
		if(product.errors) {
			return
		}
		
		product.save(flush:true)
    }
	
	def getProductList(String q) {
		def c = Product.createCriteria()
		def now = new Date()
		
		def res = c. list {
			or {
				like("productName","%" + q + "%")
				like("description","%" + q + "%")
			}
			productPrices {
				le("fromDate",now)
				or {
					isNull("thruDate")
					ge("thruDate",now)
				}
			}
			
		}
		
		return res
	}
	
	def getActiveListPrice(Product product) {
		def c = ProductPrice.createCriteria()
		def now = new Date()
		def priceList = c.list {
			or {
				isNull("thruDate")
				le("thruDate",now)
			}
			ge("fromDate",now)
			eq("product",product)
			productPriceType {
				eq("value","LIST_PRICE")
			}
			order("fromDate", "desc")
		}
		
		if(priceList) {
			return priceList.get(0)
		}
	}
	
	def addProductPrice(ProductPriceCommand cmd) {
		def productPrice = new ProductPrice()
		productPrice.fromDate = cmd.fromDate
		productPrice.thruDate = cmd.thruDate
		productPrice.price = cmd.price
		productPrice.priceType = ProductPriceType.findByDescription(cmd.productPriceType)
		
		def uom = Uom.createCriteria().get {
			eq("uomType.description","CURRENCY")
			eq("abbreviation",cmd.currencyUom?cmd.currencyUom:"INR")
		}
		
		productPrice.currencyUom = uom
	}
	
}
