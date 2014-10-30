package party


import org.grails.databinding.BindingFormat;

import core.Enumeration

class Person extends Party {
	
	String currentFirstName
	String currentMiddleName
	String currentLastName
	@BindingFormat("dd-MM-yyyy")
	Date birthDate
	Enumeration gender
	Enumeration maritalStatus
	String nickName
	
    static constraints = {
		currentFirstName nullable:false,blank:false
		currentMiddleName nullable:true,blank:true
		currentLastName nullable:true,blank:true
		birthDate nullable:true,blank:true
		gender nullable:true,blank:true
		maritalStatus nullable:true,blank:true
		nickName nullable:true,blank:true
    }
}
