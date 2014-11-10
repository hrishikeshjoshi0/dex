package invoice

import product.Product

class InvoiceItem {
	
	String sequenceId
	InvoiceItemType invoiceItemType
	BigDecimal quantity
	Product product
	InvoiceItem parentInvoiceItem
	Boolean taxable
	BigDecimal amount
	String description
	
	static belongsTo = [invoice:Invoice]

    static constraints = {
		sequenceId nullable:false
		invoiceItemType nullable:false
		quantity nullable:false
		product nullable:false
		parentInvoiceItem nullable:true
		taxable nullable:true
		amount nullable:false
		description nullable:false
    }
}
