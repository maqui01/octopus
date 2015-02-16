/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import papasoft.octopus.domain.Customer;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class RetrieveCustomersMessage extends SalesMessageHandler {

	private static final int OUT_DAYS_TO_VISIT = 1;
	private static final int OUT_CUSTOMER = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.handler.MessageHandler#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		Long userId = getIncomingMessage().getUserId();
		HashMap<Customer, Integer[]> customers = null;
		try {
			validateUser(getIncomingMessage().getUserId());
			customers = getSalesModule().getSalesInterface().getCustomersForUser(userId);
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		List<ServerToClientMessage> returnMessages = new ArrayList<ServerToClientMessage>();
		if (customers != null) {
			for (Customer customer : customers.keySet()) {
				ServerToClientMessage returnMessage = new ServerToClientMessage();
				returnMessage.putData(OUT_CUSTOMER, customer);
				returnMessage.putData(OUT_DAYS_TO_VISIT, customers.get(customer));
				returnMessages.add(returnMessage);
			}
		}
		if (customers == null || customers.size() == 0) {
			return errorMessage(MessageResult.NO_CUSTOMERS_FOR_SALESMAN);
		}
		return returnMessages;
	}

}
