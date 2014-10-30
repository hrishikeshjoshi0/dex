package core

class CommunicationEventType {
	
	String description
	
	static belongsTo = [ parent : CommunicationEventType ]
	static hasMany = [children:CommunicationEventType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		description nullable:false,blank:false
		parent nullable:true
    }
}
