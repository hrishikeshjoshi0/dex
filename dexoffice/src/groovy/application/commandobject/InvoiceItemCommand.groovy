package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class InvoiceItemCommand {
	
	Long id
	Long invoiceId
	String invoiceItemType
	BigDecimal quantity
	String productId
	Boolean taxable
	BigDecimal unitPrice
	BigDecimal tax
	BigDecimal taxPercentage
	BigDecimal amount
	String description

}
