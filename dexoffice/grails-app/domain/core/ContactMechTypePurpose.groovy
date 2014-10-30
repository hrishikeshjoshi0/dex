package core

class ContactMechTypePurpose {
	
	ContactMechPurposeType contactMechPurposeType
	ContactMechType contactMechType

    static constraints = {
		contactMechPurposeType nullable:false
		contactMechType nullable:false
    }
}
