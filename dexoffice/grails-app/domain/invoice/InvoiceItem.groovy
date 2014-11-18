package invoice

import product.Product

class InvoiceItem {
	
	String sequenceId
	InvoiceItemType invoiceItemType
	BigDecimal quantity
	Product product
	Boolean taxable
	BigDecimal unitPrice
	BigDecimal amount
	String description
	
	static belongsTo = [invoice:Invoice,parent:InvoiceItem]
	static hasMany = [children:InvoiceItem]
	static mappedBy = [ parent: "none"]

    static constraints = {
		sequenceId nullable:true
		invoiceItemType nullable:false
		quantity nullable:true
		product nullable:true
		taxable nullable:true
		unitPrice nullable:true
		amount nullable:false
		description nullable:true
		parent nullable:true
    }
}
