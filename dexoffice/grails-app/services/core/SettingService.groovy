package core

import grails.transaction.Transactional

@Transactional
class SettingService {
	
	
	def getSettingValueFromCode(String code,String settingType) {
		def c = Setting.createCriteria()
		def setting = c.get {
			eq("code",code)
			eq("settingType",SettingType.findByCode(settingType))
			le("fromDate", new Date())
			or {
				isNull("thruDate")
				ge("thruDate", new Date())
			}
		}
		
		if(setting) {
			return setting.value
		}
	}
	
	def updateSetting(String code, String settingType, String value) {
		def c = Setting.createCriteria()
		def setting = c.get {
			eq("code",code)
			eq("settingType",SettingType.findByCode(settingType))
			le("fromDate", new Date())
			or {
				isNull("thruDate")
				ge("thruDate", new Date())
			}
		}
		
		if(setting) {
			setting.value = value
			setting.save(flush:true)
		}
	}
	
	/**
	 * Save setting
	 * @param setting
	 * @return
	 */
	def saveSetting(Setting setting) {
		setting.save(flush:true)
	}

	/**
	 * Get all settings for a setting type by start date and end date. 
	 * If the start date is not given,the current date is considered as the start date.
	 */
    def getSettings(SettingType settingType,String code, String description,Date fromDate, Date thruDate) {
		def c = Setting.createCriteria()
		def from = fromDate?fromDate:new Date()
		def settings = c.list {
			eq("settingType",settingType)
			
			if(code) {
				like("code","%" + code +"%")
			}
			
			if(description) {
				like("description","%" + description +"%")
			}
			
			le("fromDate", from)
			or {
				if(!thruDate) {
					isNull("thruDate")
					ge("thruDate", new Date())
				} else {
					ge("thruDate", thruDate)
				}
			}
		}
		return settings
    }
}
