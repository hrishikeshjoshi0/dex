package core

class PostalAddress extends ContactMech {
	
	String toName
	String attnName
	String address1
	String address2
	String directions
	String city
	String postalCode
	String postalCodeExt
	Geo stateProvinceGeo
	Geo countryGeo
	Geo postalCodeGeo

    static constraints = {
		toName blank:true,nullable:true
		attnName blank:true,nullable:true
		address1 blank:false,nullable:false
		address2 blank:true,nullable:true
		directions blank:true,nullable:true
		city blank:true,nullable:true
		postalCode blank:true,nullable:true
		postalCodeExt blank:true,nullable:true
		stateProvinceGeo nullable:true
		countryGeo nullable:true
		postalCodeGeo nullable:true
    }
}
