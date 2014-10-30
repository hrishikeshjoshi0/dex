package party

class PartyClassificationGroup {
	
	PartyClassificationType partyClassificationType
	String description

    static constraints = {
		parent nullable:true
		partyClassificationType nullable:false
		description nullable:false
    }
	
	static belongsTo = [ parent : PartyClassificationGroup ]
	static hasMany = [children:PartyClassificationGroup]
	static mappedBy = [ parent: "none"]
}
