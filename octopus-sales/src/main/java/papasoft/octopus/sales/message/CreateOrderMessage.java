/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.List;

import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class CreateOrderMessage extends SalesMessageHandler {

	private static final Integer IN_CUSTOMER_CODE = 0;
	private static final Integer OUT_ORDER_NUMBER = 0;
	private static final Integer OUT_COMPANY_REMAINING_ORDERS = 1;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		Long userId = getIncomingMessage().getUserId();
		Long customerCode = (Long) getIncomingMessage().getData(IN_CUSTOMER_CODE);
		Long orderNumber;
		//User user;
		try {
			/*user = */validateUser(userId);
			orderNumber = getSalesModule().getSalesInterface().createOrder(userId, customerCode);
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		
		// Resto 1 a la cantidad de pedidos de la empresa:
		Integer remainingOrders = 0;
		/*  VERSION GRATUITA: No maneja cantidades de pedidos
		if ( user.getCompany() != null ) {
			try {
				remainingOrders = getSalesModule().getSalesInterface().updateCompanyRemainingOrders(user.getCompany().getId(), -1);
			} catch (OctopusException e) {
				LogManager.logError("Error updating company["+user.getCompany().getId()+"] remaining orders by [-1]", e, getSalesModule().getLoggerName());
			}
		}
		*/
		
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.setResult(MessageResult.RESULT_OK);
		responseMessage.putData( OUT_ORDER_NUMBER, orderNumber );
		responseMessage.putData( OUT_COMPANY_REMAINING_ORDERS, remainingOrders );
		return singleMessage(responseMessage);
	}

}
