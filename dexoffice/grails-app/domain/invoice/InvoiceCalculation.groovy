package invoice

class InvoiceCalculation {
	
	Date calculationDate
	
	BigDecimal invoiceGrandTotal = 0.0
	BigDecimal currentReceivedAmount = 0.0
	BigDecimal currentReceivableAmount = 0.0
	
	static belongsTo = [invoice : Invoice]

    static constraints = {
		calculationDate nullable:false
		invoiceGrandTotal nullable:false
		currentReceivedAmount nullable:false
		currentReceivableAmount  nullable:false
    }
}
