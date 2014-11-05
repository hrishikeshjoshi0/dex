package application.marshaller

import grails.converters.JSON
import product.Product
import product.ProductPrice

class ProductMarshaller {
	
	def productService
	
	void register() {
		JSON.registerObjectMarshaller(Product) { Product p ->
			def res = [:]
			res.id = p.id
			res.productName = p.productName
			res.introductionDate = p.introductionDate
			res.productType = p.productType?.name
			res.primaryProductCategoryType = p.primaryProductCategoryType?.description
			res.description = p.description

			res.quantityUom = [:]
			res.quantityUom.abbreviation = p.quantityUom?.abbreviation
			res.quantityUom.description = p.quantityUom?.description
			
			res.quantityIncluded = p.quantityIncluded
			res.quantityIncluded = p.quantityIncluded
			
			res.taxable = p.taxable
			
			res.heightUom = [:]
			res.heightUom.abbreviation = p.heightUom?.abbreviation
			res.heightUom.description = p.heightUom?.description
			res.height = p.height
			
			res.lengthUom = [:]
			res.lengthUom.abbreviation = p.heightUom?.abbreviation
			res.lengthUom.description = p.heightUom?.description
			res.length = p.length
			
			res.depthUom = [:]
			res.depthUom.abbreviation = p.heightUom?.abbreviation
			res.depthUom.description = p.heightUom?.description
			res.depth = p.depth
			
			res.weightUom = [:]
			res.weightUom.abbreviation = p.heightUom?.abbreviation
			res.weightUom.description = p.heightUom?.description
			res.weight = p.weight
			
			res.defaultPrice = [:]
			def defaultPrice = getActiveDefaultPrice(p)
			if(defaultPrice) {
				res.defaultPrice = [:]
				res.defaultPrice.currencyUom = [:]
				res.defaultPrice.currencyUom.abbreviation = p.heightUom?.abbreviation
				res.defaultPrice.currencyUom.description = p.heightUom?.description
				res.defaultPrice.amount = defaultPrice.amount
				res.defaultPrice.priceType = [:]
				res.defaultPrice.priceType?.value = defaultPrice.priceType?.value
				res.defaultPrice.priceType?.description = defaultPrice.priceType?.description
			}
			
			res.prices = []
			def prices = getProductPrices(p)
			prices?.each {price ->
				res.prices << [
					fromDate : price.fromDate,
					thruDate : price.thruDate,
					amount : price.amount,
					priceType : [value:price?.priceType?.value,
								description:price.priceType?.description]
				] 	
			}
			
			return res
		}
	}
	
	def getProductPrices(Product product) {
		def c = ProductPrice.createCriteria()
		def now = new Date()
		def priceList = c.list {
			or {
				isNull("thruDate")
				ge("thruDate",now)
			}
			le("fromDate",now)
			eq("product",product)
			order("fromDate", "desc")
		}
		
		priceList
	}
	
	def getActiveDefaultPrice(Product product) {
		def c = ProductPrice.createCriteria()
		def now = new Date()
		def priceList = c.list {
			or {
				isNull("thruDate")
				ge("thruDate",now)
			}
			createAlias("priceType", "priceType")
			le("fromDate",now)
			eq("product",product)
			eq("priceType.value","DEFAULT_PRICE")
			order("fromDate", "desc")
		}
		
		if(priceList) {
			return priceList.get(0)
		}
	}

}
