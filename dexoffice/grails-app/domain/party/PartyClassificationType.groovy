package party

class PartyClassificationType {
	
	String description
	
	static belongsTo = [ parent : PartyClassificationType ]
	static hasMany = [children:PartyClassificationType]
	static mappedBy = [ parent: "none"]

    static constraints = {
		description nullable:false
		parent nullable:true
    }
}
