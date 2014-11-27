/**
 * 
 */
package application.marshaller

/**
 * Base class for all the marshalling
 * 
 * @author hrishi
 *
 */
public abstract class BaseJsonMarshaller {
	
	def partyService
	def communicationEventService
	def contactMechService
	def productService
	def invoiceService
	def paymentService
	
	abstract void register()

}
