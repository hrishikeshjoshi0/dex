package application.marshaller

import core.TelecomNumber;
import party.PartyContactMech;
import party.Person;
import grails.converters.JSON;

class PersonMarshaller {
	
	void register() {
		JSON.registerObjectMarshaller(Person) { Person p ->
			def res = [:]
			res.id = p.id
			res.currentFirstName = p.currentFirstName
			res.currentMiddleName = p.currentMiddleName
			res.currentLastName = p.currentLastName
			res.gender = p.gender
			res.birthDate = p.birthDate
			res.partyContactMechs = []
			
			p?.partyContactMechs?.each { PartyContactMech pcm ->
				def cm = pcm.contactMech
				def cmtype = cm?.contactMechType?.description

				if(cmtype == 'EMAIL') {
					res.partyContactMechs << [id:pcm.id,	value : cm?.value,type:"EMAIL"]
				} else if(cmtype == 'MOBILE_NUMBER') {
					res.partyContactMechs << [id:pcm.id, value:cm?.contactNumber,type:"MOBILE_NUMBER"]
				} else if(cmtype == 'POSTAL_ADDRESS') {
					res.partyContactMechs << [
							id:pcm.id, 
							type:"POSTAL_ADDRESS",
							address1 : cm?.address1,
							address2 : cm?.address2,
							directions : cm?.directions,
							city : cm?.city,
							postalCode : cm?.postalCode
						]
				} 			
				
			}
			return res
		}
	}

}
