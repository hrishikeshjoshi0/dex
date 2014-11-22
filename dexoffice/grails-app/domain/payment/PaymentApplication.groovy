package payment

import invoice.Invoice
import invoice.InvoiceItem

class PaymentApplication {
	
	Invoice invoice
	InvoiceItem invoiceItem
	BigDecimal amountApplied
	
	static belongsTo = [payment : Payment]

    static constraints = {
    }
}
