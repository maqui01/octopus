/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.List;

import papasoft.octopus.domain.User;
import papasoft.octopus.domain.se.IabOrder;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author De La
 *
 */
public class BuyOrdersPackMessage extends SalesMessageHandler {

	private static final Integer IN_ORDER_ID = 0;
	private static final Integer IN_SKU = 1;
	private static final Integer IN_INAPP_TOKEN = 2;
	private static final Integer IN_ORDERS_QUANTITY = 3;
	private static final Integer OUT_COMPANY_REMAINING_ORDERS = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		Long userId = getIncomingMessage().getUserId();
		String orderId = (String) getIncomingMessage().getData(IN_ORDER_ID);
		String sku = (String) getIncomingMessage().getData(IN_SKU);
		String inappToken = (String) getIncomingMessage().getData(IN_INAPP_TOKEN);
		Integer ordersQuantity = (Integer) getIncomingMessage().getData(IN_ORDERS_QUANTITY);
		
		User user;
		try {
			user = validateUser(userId);
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		
		// Vemos si la orden ya fué procesada anteriormente:
		IabOrder order = getSalesModule().getSalesInterface().retrieveIabOrder(orderId);
		if (order != null) {
			ServerToClientMessage responseMessage = new ServerToClientMessage();
			responseMessage.setResult(MessageResult.RESULT_OK);
			return singleMessage(responseMessage);
		}
		
		// Primero validamos la compra contra google:
		//TODO Validar contra google
		
		// Agregamos el pack a la empresa:
		Integer remainingOrders = 0;
		if ( user.getCompany() != null ) {
			try {
				remainingOrders = getSalesModule().getSalesInterface().updateCompanyRemainingOrders(user.getCompany().getId(), ordersQuantity);
			} catch (OctopusException e) {
				LogManager.logError("Error updating company["+user.getCompany().getId()+"] remaining orders by ["+ordersQuantity+"]", e, getSalesModule().getLoggerName());
			}
		}
		
		// Guardo la iabOrder
		try {
			getSalesModule().getSalesInterface().saveIabOrder(orderId, sku, inappToken, IabOrder.ORDER_STATUS_CONSUMED);
		} catch (OctopusException e) {
			LogManager.logError("Failed to save iabOrder", e, getModule().getLoggerName());
		}
		
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.setResult(MessageResult.RESULT_OK);
		responseMessage.putData( OUT_COMPANY_REMAINING_ORDERS, remainingOrders );
		return singleMessage(responseMessage);
	}

}
