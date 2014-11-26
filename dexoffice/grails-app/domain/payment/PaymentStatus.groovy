package payment

import invoice.Invoice;
import core.Status;

class PaymentStatus {
	
	Status status
	Payment payment

	static constraints = {
		status nullable:false
		payment nullable:false
	}
}
