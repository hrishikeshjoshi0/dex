package core

class Enumeration {
	
	EnumerationType enumerationType
	String enumCode
	String sequenceId
	String description
	
	static belongsTo = [parent:Enumeration]
	static hasMany = [enumerations: Enumeration]

    static constraints = {
		enumerationType nullable:false
		enumCode nullable:false,blank:false
		sequenceId nullable:true,blank:true
		parent nullable:true 
    }
}
