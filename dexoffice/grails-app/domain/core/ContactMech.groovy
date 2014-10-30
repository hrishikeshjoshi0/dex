package core

class ContactMech {
	
	ContactMechType contactMechType
	String value
	
    static constraints = {
		contactMechType nullable:false
		value nullable:true,blank:true
    }
	
	static mapping = {
		tablePerHierarchy false
	}
}
