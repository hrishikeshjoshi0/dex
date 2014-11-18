package core

class Status {
	
	String description
	StatusType statusType
	String statusCode
	String sequenceId
	
    static constraints = {
		description nullable:true,blank:true
		statusType nullable:false
		statusCode nullable:false,blank:false 
		sequenceId nullable:true,blank:true
    }
	
}
