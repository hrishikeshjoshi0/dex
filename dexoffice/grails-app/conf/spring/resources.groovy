import util.marshalling.CustomObjectMarshallers
import application.marshaller.BaseJsonMarshaller
import application.marshaller.InvoiceItemMarshaller
import application.marshaller.InvoiceMarshaller
import application.marshaller.PersonMarshaller
import application.marshaller.ProductMarshaller

// Place your Spring DSL code here
beans = {
	
	baseJsonMarshaller(BaseJsonMarshaller) { bean ->
		bean.'abstract' = true
		partyService = ref("partyService")
		contactMechService = ref("contactMechService")
		communicationEventService = ref("communicationEventService")
		productService = ref("productService")
		invoiceService = ref("invoiceService")
		paymentService = ref("paymentService")
	}
	
	personMarshaller(PersonMarshaller) { bean ->
		bean.parent = baseJsonMarshaller		
	}
	
	productMarshaller(ProductMarshaller) { bean ->
		bean.parent = baseJsonMarshaller
	}
	
	invoiceMarshaller(InvoiceMarshaller) { bean ->
		bean.parent = baseJsonMarshaller
	}
	
	invoiceItemMarshaller(InvoiceItemMarshaller) { bean ->
		bean.parent = baseJsonMarshaller
	}
	
	customObjectMarshallers( CustomObjectMarshallers ) {
		marshallers = [ref('personMarshaller'),ref('productMarshaller'),ref('invoiceMarshaller'),ref('invoiceItemMarshaller')]
	}
}
