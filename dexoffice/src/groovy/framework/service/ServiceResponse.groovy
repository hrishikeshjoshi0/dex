/**
 * 
 */
package framework.service

/**
 * @author hrishikesh.joshi
 *
 */
class ServiceResponse {
	
	public static final String RETURN_INSTANCE = "RETURN_INSTANCE"
	
	public static final String RETURN_INSTANCE_MAP = "RETURN_INSTANCE_LIST"
	
	def resultMap = [:]
	
	def messages = []
	
	private boolean isError = false
	
	def addNamedInstance(String name, def instance) {
		resultMap << [name:instance]
	}
	
	def addInstance(def instance) {
		resultMap << [RETURN_INSTANCE:instance]
	}
	
	def addInstanceList(List instanceList) {
		resultMap << [RETURN_INSTANCE_LIST:instanceList]
	}
	
	//Add Errors
	def addError(String error) {
		isError = true
		addMessage(error,error,Level.error)
	}
	
	def addError(String errorCode,String errorDescription) {
		isError = true
		addMessage(errorCode,errorDescription,Level.error)
	}
	
	//Add Success
	def addSuccess(String success) {
		addMessage(success,success,Level.success)
	}
	
	def addSuccess(String successCode,String successDescription) {
		addMessage(successCode,successDescription,Level.success)
	}
	
	//Add Warning
	def addWarning(String code) {
		addMessage(code,code,Level.warning)
	}
	
	def addWarning(String code,String description) {
		addMessage(code,description,Level.warning)
	}
	
	def isError() {
		return isError
	}
	
	def isSuccess() {
		return !isError
	}
	
	def getResultMap() {
		return resultMap
	}
	
	def getInstance() {
		return resultMap[RETURN_INSTANCE]
	}
	
	def getInstanceList() {
		return resultMap[RETURN_INSTANCE_LIST]
	}
	
	def getNamedInstance(String name) {
		return resultMap[name]
	}
	
	def getNamedInstanceList(String name) {
		return resultMap[name]
	}
	
	private addMessage(String errorCode,String errorDescription,Level level) {
		def msg = new Message(code:errorCode,description:errorDescription,level:Level.error)
		messages << msg
	}
	
	class Message {
		String code
		String description
		Level level
	}
	
	enum Level {
		error,
		warning,
		success
	}

}
