package core

import party.Party

class Note {
	
	String name
	Date dateTime
	String noteInfo
	
	static hasMany = [parties:Party]

    static constraints = {
		name nullable:false,blank:false
		dateTime nullable:false
		noteInfo nullable:false,blank:false,maxLength:1000
    }
}
