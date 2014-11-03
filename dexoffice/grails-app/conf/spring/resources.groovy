import util.marshalling.CustomObjectMarshallers
import application.marshaller.PersonMarshaller
import application.marshaller.ProductMarshaller

// Place your Spring DSL code here
beans = {
	customObjectMarshallers( CustomObjectMarshallers ) {
		marshallers = [new PersonMarshaller(),new ProductMarshaller()]
	}
}
