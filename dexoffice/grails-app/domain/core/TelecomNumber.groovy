package core

class TelecomNumber extends ContactMech {
	
	String countryCode
	String areaCode
	String contactNumber

    static constraints = {
		countryCode nullable:true,blank:true
		areaCode nullable:true,blank:true
		contactNumber nullable:false,blank:false
    }
}
