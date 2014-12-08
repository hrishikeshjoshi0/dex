package core

import grails.transaction.Transactional

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError;

import application.commandobject.SettingCommand

class SettingController {
	
	static responseFormats = ['json', 'xml']
	
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def settingService

    def index() {
		def st = params.settingType
		def settingType = SettingType.findByCode(st)
		def settings = settingService.getSettings(settingType, params.code, params.description,params.fromDate,params.thruDate)
		respond settings,[status:HttpStatus.OK]
	}
	
	@Transactional
	def show(SettingCommand s) {
		if (s == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		def setting = Setting.get(s.id)
		
		respond setting, [status: HttpStatus.OK]
	}
	
	@Transactional
	def save(SettingCommand cmd) {
		if (cmd == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}
		
		def settings = Setting.findAllByCode(cmd.code)
		if(!settings?.isEmpty()) {
			def errors = new FieldError("setting","code","The setting already exists.")
			render status: HttpStatus.BAD_REQUEST
			return
		}
		
		def setting = new Setting()
		setting.code = cmd.code
		setting.description = cmd.description
		setting.fromDate = cmd.fromDate
		setting.thruDate = cmd.thruDate
		
		setting.settingType = SettingType.findByCode(settingTypeCode)
		setting.save(flush:true)
		
		if(!setting.hasErrors()) {
			respond setting.errors, [status: HttpStatus.NOT_ACCEPTABLE]
		}
		
		respond setting, [status: HttpStatus.OK]
	}
	
	@Transactional
	def update(Setting setting) {
		if (setting == null) {
			render status: HttpStatus.NOT_FOUND
			return
		}

		setting.validate()
		
		if (setting.hasErrors()) {
			render status: HttpStatus.NOT_ACCEPTABLE
			return
		}

		setting.save flush:true
		
		respond setting, [status: HttpStatus.OK]
	}
}
