import util.marshalling.CustomObjectMarshallers
import application.marshaller.InvoiceItemMarshaller
import application.marshaller.InvoiceMarshaller
import application.marshaller.PersonMarshaller
import application.marshaller.ProductMarshaller

// Place your Spring DSL code here
beans = {
	customObjectMarshallers( CustomObjectMarshallers ) {
		marshallers = [new PersonMarshaller(),new ProductMarshaller(),new InvoiceMarshaller(),new InvoiceItemMarshaller()]
	}
}
