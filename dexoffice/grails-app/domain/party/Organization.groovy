package party


class Organization extends Party {
	
	String name
	String externalId
	
    static constraints = {
		name nullable:false,blank:false
		externalId nullable:true
    }
}
