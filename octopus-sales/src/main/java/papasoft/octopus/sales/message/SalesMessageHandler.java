/**
 * 
 */
package papasoft.octopus.sales.message;

import papasoft.octopus.domain.User;
import papasoft.octopus.domain.UserStatus;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.sales.module.SalesModule;
import papasoft.octopus.server.message.AbstractMessage;

/**
 * @author maqui
 *
 */
public abstract class SalesMessageHandler extends AbstractMessage {
	
	protected static final String USERNAME_INVALID = "username_invalid";

	/**
	 * 
	 * @return
	 */
	protected SalesModule getSalesModule() {
		return (SalesModule) getModule();
	}
	


	/**
	 * @return
	 */
	protected User validateUser(Long userId) throws OctopusException {
		User user;
		try {
			user = getSalesModule().getSalesInterface().getUser(userId);
		} catch (OctopusException ex) {
			user = null;
		}
		if (user == null) {
			throw new OctopusException("User not found", MessageResult.INVALID_USER_PASSWORD);
		}
		if (user.getStatus().equals(UserStatus.BLOCKED)) {
			throw new OctopusException("User blocked", MessageResult.USER_BLOCKED);
		}
		return user;
	}
	


	/**
	 * @param ex
	 */
	protected void handleBlockedUser(OctopusException ex, Integer orderNumberKey) {
		if (ex.getErrorCode().equals(MessageResult.USER_BLOCKED)) {
			try {
				getSalesModule().getSalesInterface().cancelOrder((Long)getIncomingMessage().getData(orderNumberKey), getIncomingMessage().getUserId());
			} catch (OctopusException e1) {
				// Do nothing, si falla eventualmente lo va a eliminar el proceso de pedidos colgados
			}
		}
	}
	
}
