/**
 * 
 */
package application.marshaller

import grails.converters.JSON
import tax.TaxCategory
import tax.TaxRate

/**
 * @author hrishi
 *
 */
class TaxCategoryMarshaller extends BaseJsonMarshaller {

	/* (non-Javadoc)
	 * @see application.marshaller.BaseJsonMarshaller#register()
	 */
	@Override
	public void register() {

		JSON.registerObjectMarshaller(TaxCategory) { TaxCategory taxCategory ->
			def res = [:]

			res.id = taxCategory.id
			res.name = taxCategory.name
			res.description = taxCategory.description
			
			def now = new Date()
			
			def c = TaxRate.createCriteria()
			def taxRates = c.list {
				eq("taxCategory",taxCategory)
				le("fromDate",now)
				or {
					isNull("thruDate")
					ge("thruDate",now)
				}
			}
			
			if(!taxRates.isEmpty()) {
				res.taxPercentage = taxRates[0].taxPercentage
			}

			return res
		}
	}
}
