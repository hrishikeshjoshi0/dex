package application.marshaller

import grails.converters.JSON
import party.PartyContactMech
import party.Person
import core.PostalAddress
import core.TelecomNumber;

class PersonMarshaller extends BaseJsonMarshaller {
	
	void register() {
		JSON.registerObjectMarshaller(Person) { Person p ->
			def res = [:]
			res.id = p.id
			res.currentFirstName = p.currentFirstName
			res.currentMiddleName = p.currentMiddleName
			res.currentLastName = p.currentLastName
			res.gender = p.gender
			res.birthDate = p.birthDate
			res.description = p.description
			
			res.partyContactMechs = []
			
			p?.partyContactMechs?.each { PartyContactMech pcm ->
				def cm = pcm.contactMech
				def cmtype = cm?.contactMechType?.description

				def cmp = []
				pcm?.partyContactMechPurpose?.each {  
					cmp << it?.contactMechPurposeType?.description
				}
				
				if(cmtype == 'EMAIL') {
					res.partyContactMechs << [id:cm.id,	value : cm?.value,type:"EMAIL",purpose:cmp]
				} else if(cm instanceof TelecomNumber && cmtype == 'MOBILE_NUMBER') {
					res.partyContactMechs << [id:cm.id, value:cm?.contactNumber,type:"MOBILE_NUMBER",purpose:cmp]
				} else if(cm instanceof PostalAddress && cmtype == 'POSTAL_ADDRESS') {
					res.partyContactMechs << [
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
