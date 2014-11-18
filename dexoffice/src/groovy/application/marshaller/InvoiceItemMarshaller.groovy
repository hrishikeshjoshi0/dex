package application.marshaller

import grails.converters.JSON
import invoice.InvoiceItem
import party.Party
import party.PartyContactMech
import party.Person
import core.PostalAddress
import core.TelecomNumber

class InvoiceItemMarshaller {
	
	void register() {
		JSON.registerObjectMarshaller(InvoiceItem) { InvoiceItem i ->
			def res = [:]
			res.id = i.id
			res.description = i.description
			res.unitPrice = i.unitPrice
			res.quantity = i.quantity
			res.amount = i.amount
			res.product = i.product?.productName
			res.parent = i.parent?.id
			res.invoiceItemType = [name:i.invoiceItemType?.name,description:i.invoiceItemType?.description]
			
			return res
		}
	}
}
