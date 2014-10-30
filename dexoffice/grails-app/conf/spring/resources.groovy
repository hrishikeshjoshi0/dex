import util.marshalling.CustomObjectMarshallers
import application.marshaller.PersonMarshaller

// Place your Spring DSL code here
beans = {
	customObjectMarshallers( CustomObjectMarshallers ) {
		marshallers = [new PersonMarshaller()]
	}
}
