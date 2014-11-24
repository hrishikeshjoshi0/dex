package payment

class PaymentMethod {
	
	Payment payment
	PaymentMethodType paymentMethodType

    static constraints = {
		payment nullable:false
		paymentMethodType nullable:false
    }
}
