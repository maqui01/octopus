/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.List;

import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author De La
 *
 */
public class CreateCompanyMessage extends SalesMessageHandler {

	//private static final Integer IN_ORDER_ID = 5;
	//private static final Integer IN_SKU = 6;
	//private static final Integer IN_INAPP_TOKEN = 0;
	private static final Integer IN_COMPANY_NAME = 1;
	private static final Integer IN_USER_NAME = 2;
	private static final Integer IN_PASSWORD = 3;
	private static final Integer IN_EMAIL = 4;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		//String orderId = (String) getIncomingMessage().getData(IN_ORDER_ID);
		//String sku = (String) getIncomingMessage().getData(IN_SKU);
		//String inappToken = (String) getIncomingMessage().getData(IN_INAPP_TOKEN);
		String companyName = (String) getIncomingMessage().getData(IN_COMPANY_NAME);
		String userName = (String) getIncomingMessage().getData(IN_USER_NAME);
		String password = (String) getIncomingMessage().getData(IN_PASSWORD);
		String email = (String) getIncomingMessage().getData(IN_EMAIL);
		
		/* //TODO si es free versión no valido esto
		// Vemos si la orden ya fué procesada anteriormente:
		IabOrder order = getSalesModule().getSalesInterface().retrieveIabOrder(orderId);
		if (order != null) {
			ServerToClientMessage responseMessage = new ServerToClientMessage();
			responseMessage.setResult(MessageResult.RESULT_OK);
			return singleMessage(responseMessage);
		}
		*/
		
		// Primero validamos la compra contra google:
		//TODO Validar contra google
		
		// Creamos la empresa:
		try {
			getSalesModule().getSalesInterface().createCompany(companyName, userName, password, email);
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		
		/* //TODO como es free versión no guardamos nada.
		// Guardo la iabOrder
		try {
			getSalesModule().getSalesInterface().saveIabOrder(orderId, sku, inappToken, IabOrder.ORDER_STATUS_CONSUMED);
		} catch (OctopusException e) {
			LogManager.logError("Failed to save iabOrder", e, getModule().getLoggerName());
		}
		*/
		
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.setResult(MessageResult.RESULT_OK);
		return singleMessage(responseMessage);
	}

}
