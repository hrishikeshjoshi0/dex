package core

class Setting {

	String code
	String description
	String value
	Date fromDate
	Date thruDate
	
	static belongsTo = [settingType : SettingType]
	
    static constraints = {
		code nullable:false,blank:false
		description nullable:false,blank:false
		value nullable:false,blank:false
		fromDate nullable:false
		thruDate nullable:true
    }
}
