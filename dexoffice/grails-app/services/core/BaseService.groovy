package core

import framework.service.ServiceResponse

class BaseService {
	
	def createResultMap() {
		return [:]
	}
	
	def createServiceResponse() {
		return new ServiceResponse()
	}
	
	def saveInstance(def instance,def params) {
		ServiceResponse result = createServiceResponse()

		try {
			if (!instance) {
				result.addError('not_found')
			} else {
				if(params) {
					instance.properties = params
				}
				
				instance.validate()

				if (instance.hasErrors() || !instance.save(flush: true)) {
					def errors = instance.errors
					//Add All Error Messages.
					result.addError("PERSISTENCE_ERROR")
					return result
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace()
			result.addError("TECHNICAL_FAULT",ex.getMessage())	
		}
		return result
	}

	def deleteInstance(def instance) {
		ServiceResponse result = createServiceResponse()

		try {
			if (!instance) {
				result.addError('not_found')
			} else {
				if(params) {
					instance.properties = params
				}

				if (instance.hasErrors() || !instance.delete(flush: true)) {
					def errors = instance.errors
					//Add All Error Messages.
					errors?.each {
						result.addError(it.getCode(),it.getDefaultMessage())
					}
					return result
				}
				
				result.addSuccess("success")				
			}
		}
		catch (Exception ex) {
			result.addError("TECHNICAL_FAULT",ex.getMessage())	
		}
		return result
	}
}
