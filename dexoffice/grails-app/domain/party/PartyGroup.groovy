package party

class PartyGroup extends Party{
	
	String groupName
	String groupNameLocal
	String logoImageUrl
	Integer numEmployees

    static constraints = {
		groupName nullable:true
		groupNameLocal nullable:false
		logoImageUrl nullable:true
	}
	
}