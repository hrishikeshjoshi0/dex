package application.commandobject

import grails.validation.Validateable

@Validateable(nullable=true)
class SettingCommand {

	String settingTypeCode
	
	Long id
	String code
	String description
	Date fromDate
	Date thruDate
}
