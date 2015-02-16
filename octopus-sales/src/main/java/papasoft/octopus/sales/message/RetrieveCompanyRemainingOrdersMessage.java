/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.List;

import papasoft.octopus.domain.User;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author De La
 *
 */
public class RetrieveCompanyRemainingOrdersMessage extends SalesMessageHandler {

	private static final Integer OUT_COMPANY_REMAINING_ORDERS = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		Long userId = getIncomingMessage().getUserId();
		User user;
		try {
			user = validateUser(userId);
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		
		// Resto 1 a la cantidad de pedidos de la empresa:
		Integer remainingOrders = 0;
		if ( user.getCompany() != null ) {
			remainingOrders = getSalesModule().getSalesInterface().getCompanyRemainingOrders( user.getCompany().getId() );
		}
		
		ServerToClientMessage responseMessage = new ServerToClientMessage();
		responseMessage.setResult(MessageResult.RESULT_OK);
		responseMessage.putData( OUT_COMPANY_REMAINING_ORDERS, remainingOrders );
		return singleMessage(responseMessage);
	}

}
