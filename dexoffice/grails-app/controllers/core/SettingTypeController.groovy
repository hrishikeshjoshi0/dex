package core

import org.springframework.http.HttpStatus

class SettingTypeController {

    static responseFormats = ['json', 'xml']
	static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE",show:"GET"]
	
	def index(Integer max) {
		def result = SettingType.list()
		respond result, [status: HttpStatus.OK]
	}
}
