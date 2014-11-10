package invoice

import core.Status

class InvoiceStatus {
	
	Status status
	Invoice invoice

    static constraints = {
		status nullable:false
		invoice nullable:false 
    }
}
