package application.marshaller

import grails.converters.JSON
import invoice.Invoice
import invoice.InvoiceItem
import party.Party
import party.PartyContactMech
import party.Person
import core.PostalAddress
import core.TelecomNumber

class InvoiceMarshaller {
	
	def productService
	
	void register() {
		JSON.registerObjectMarshaller(Invoice) { Invoice i ->
			def res = [:]
			res.id = i.id
			
			res.invoiceDate = i.invoiceDate
			res.invoiceNumber = i.invoiceNumber
			res.message = i.message
			
			//Invoice Status
			res.currentInvoiceStatus = [:]
			res.currentInvoiceStatus.statusCode = i.currentInvoiceStatus?.status?.statusCode
			res.currentInvoiceStatus.description = i.currentInvoiceStatus?.status?.description
			
			res.party = [:]
			
			res.party.id = i.party.id
			res.party.type = i.party.partyType?.description

			if(i.party instanceof Person) {
				res.party.displayName = i.party.currentFirstName + " " + i.party.currentLastName
			} else if(i.party instanceof Party) {
				res.party.displayName = i.party.description
			}
			
			res.items = []
			i?.items?.each {
				 if(it.parent) {
					 return
				 }
				
				 def ii = [
					id:it.id,
					description : it.description,
					unitPrice : it.unitPrice,
					quantity : it.quantity,
					amount : it.amount,
					product : it.product?.productName,
					parent : it.parent?.id,
					invoiceItemType : [name:it.invoiceItemType?.name,description:it.invoiceItemType?.description]
				]
				
				def serviceTax = 0.0
				
				it.children?.each {InvoiceItem child ->
					if(child.invoiceItemType?.name == "ITM_SERVICE_TAX") {
						serviceTax += child.amount
					}
				}
				
				ii.tax = serviceTax
				
				res.items << ii 
			}
			
			res.party.partyContactMechs = []
			
			i.party?.partyContactMechs?.each { PartyContactMech pcm ->
				def cm = pcm.contactMech
				def cmtype = cm?.contactMechType?.description

				def cmp = []
				pcm?.partyContactMechPurpose?.each {
					cmp << it?.contactMechPurposeType?.description
				}
				
				if(cmtype == 'EMAIL') {
					res.party.partyContactMechs << [id:cm.id,	value : cm?.value,type:"EMAIL",purpose:cmp]
				} else if(cm instanceof TelecomNumber && cmtype == 'MOBILE_NUMBER') {
					res.party.partyContactMechs << [id:cm.id, value:cm?.contactNumber,type:"MOBILE_NUMBER",purpose:cmp]
				} else if(cm instanceof PostalAddress && cmtype == 'POSTAL_ADDRESS') {
					res.party.partyContactMechs << [
							id:cm.id,
							type:"POSTAL_ADDRESS",
							address1 : cm?.address1,
							address2 : cm?.address2,
							directions : cm?.directions,
							city : cm?.city,
							postalCode : cm?.postalCode,
							purpose:cmp
						]
				}
			}
			
			return res
		}
	}
}
