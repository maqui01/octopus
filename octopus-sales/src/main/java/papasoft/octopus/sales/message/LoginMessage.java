/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.Date;
import java.util.List;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.domain.SalesUser;
import papasoft.octopus.domain.SecretField;
import papasoft.octopus.domain.User;
import papasoft.octopus.domain.UserStatus;
import papasoft.octopus.domain.se.Company;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class LoginMessage extends SalesMessageHandler {

	private static final int INC_PASSWORD = 0;
	private static final int INC_USERNAME = 1;
	private static final int INC_COMPANYNAME = 2;
	private static final int OUT_USER = 0;
	private static final int OUT_DATE = 1;
	private static final int OUT_APP_PARAMS = 2;
	private static final int OUT_COMPANY_REMAINING_ORDERS = 3;
	private static final int OUT_COMPANY_NAME = 4;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.handler.MessageHandler#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		String password = ((SecretField) getIncomingMessage().getData(INC_PASSWORD)).getSecretField();
		String username =  (String) getIncomingMessage().getData(INC_USERNAME);
		String companyName = (String) getIncomingMessage().getData(INC_COMPANYNAME);
		
		//Veo si tengo que crear una company de prueba:
		if ( username.equals(User.GUEST_USER_NAME)
				&& password.equals(User.GUEST_USER_PASSWORD)
				&& companyName.equals(Company.GUEST_COMPANY_NAME) ) {
			companyName = getSalesModule().getSalesInterface().createTestCompany();
		}
		
		SalesUser user = null;
		try {
			user = getSalesModule().getSalesInterface().getUser(username, companyName);
		} catch (OctopusException e) {
			LogManager.logError("Failed to retrieve user from Interface", e, getModule().getLoggerName());
		}
		
		ServerToClientMessage returnMessage = new ServerToClientMessage();
		if (user != null && user.getPassword().equals(password)) {
			if (user.getStatus().equals(UserStatus.NORMAL)) {
				returnMessage.setResult(MessageResult.RESULT_OK);
				returnMessage.putData(OUT_USER, user);
				returnMessage.putData(OUT_DATE, new Date());
				returnMessage.putData(OUT_APP_PARAMS, OctopusContext.getCtx().getConfigurationManager().getApplicationParameters());
				returnMessage.putData( OUT_COMPANY_REMAINING_ORDERS, user.getCompany() != null 
						? getSalesModule().getSalesInterface().getCompanyRemainingOrders(user.getCompany().getId())
						: new Integer(0) );
				returnMessage.putData(OUT_COMPANY_NAME, companyName);
			} else {
				returnMessage.setResult(MessageResult.USER_BLOCKED);
			}
		} else {
			returnMessage.setResult(MessageResult.INVALID_USER_PASSWORD);
		}
		return singleMessage(returnMessage);
	}
	
}
