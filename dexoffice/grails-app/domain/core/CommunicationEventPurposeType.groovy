package core

class CommunicationEventPurposeType {
	
	String description
	
	static hasMany = [children: CommunicationEventPurposeType]
	static belongsTo = [ parent : CommunicationEventPurposeType ]
	static mappedBy = [ parent: "none"]

    static constraints = {
		parent nullable:false
		description nullable:false,blank:false
    }
}
