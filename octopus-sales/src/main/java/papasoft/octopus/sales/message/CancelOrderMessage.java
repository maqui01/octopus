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
public class CancelOrderMessage extends SalesMessageHandler {

	private static final Integer IN_ORDER_NUMBER = 0;
	private static final Integer OUT_COMPANY_REMAINING_ORDERS = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		//User user;
		try {
			/*user = */validateUser(getIncomingMessage().getUserId());
			getSalesModule().getSalesInterface().cancelOrder((Long)getIncomingMessage().getData(IN_ORDER_NUMBER), getIncomingMessage().getUserId());
		} catch (OctopusException ex) {
			handleBlockedUser(ex, IN_ORDER_NUMBER);
			return errorMessage(ex.getErrorCode());
		}
		
		// Aumento 1 la cantidad de pedidos de la empresa:
		Integer remainingOrders = 0;
		/*  VERSION GRATUITA: No maneja cantidades de pedidos
		if ( user.getCompany() != null ) {
			try {
				remainingOrders = getSalesModule().getSalesInterface().updateCompanyRemainingOrders(user.getCompany().getId(), 1);
			} catch (OctopusException e) {
				LogManager.logError("Error updating company["+user.getCompany().getId()+"] remaining orders by [1]", e, getSalesModule().getLoggerName());
			}
		}
		*/
		
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.setResult(MessageResult.RESULT_OK);
		responseMessage.putData( OUT_COMPANY_REMAINING_ORDERS, remainingOrders );
		return singleMessage(responseMessage);
	}

}
