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
public class ConfirmOrderMessage extends SalesMessageHandler {

	private static final Integer IN_CUSTOMER_CODE = 0;
	private static final Integer IN_ORDER_NUMBER = 1;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {		
		Long customerCode = (Long) getIncomingMessage().getData(IN_CUSTOMER_CODE);
		Long orderNumber = (Long) getIncomingMessage().getData(IN_ORDER_NUMBER);
		try {
			validateUser(getIncomingMessage().getUserId());
			getSalesModule().getSalesInterface().confirmOrder(orderNumber, customerCode, getIncomingMessage().getUserId());
		} catch (OctopusException e) {
			handleBlockedUser(e, IN_ORDER_NUMBER);
			return errorMessage(e.getErrorCode());
		}
		
		ServerToClientMessage returnMessage = new ServerToClientMessage();
		returnMessage.setResult(MessageResult.RESULT_OK);
		
		return singleMessage(returnMessage);
	}

}
