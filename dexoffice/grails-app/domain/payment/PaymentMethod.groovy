package payment

class PaymentMethod {
	
	Payment payment
	PaymentMethodType paymentMethodTpe

    static constraints = {
		payment nullable:false
		paymentMethodTpe nullable:false
    }
}
