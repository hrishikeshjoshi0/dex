package core

import java.util.Date;

class SettingType {
	
	String code
	String description
	
	static hasMany = [setting : Setting]

    static constraints = {
		code blank:false,nullable:false
		description blank:false,nullable:false
    }
}
