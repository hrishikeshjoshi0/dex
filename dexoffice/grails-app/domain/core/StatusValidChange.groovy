package core

class StatusValidChange {
	
	Status status
	Status statusTo
	String transitionName
	String conditionExpression

    static constraints = {
		status nullable:false
		statusTo nullable:false
		transitionName nullable:false
		conditionExpression nullable:true,blank:true
    }
}
