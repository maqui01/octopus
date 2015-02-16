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
public class BookArticleMessage extends SalesMessageHandler {

	private static final Integer IN_ORDER_NUMBER = 0;
	private static final Integer IN_ARTICLE_CODE = 1;
	private static final Integer IN_PROMOTION_CODE = 2;
	private static final Integer IN_QUANTITY = 3;
	private static final Integer IN_PRICESLIST = 4;
	private static final Integer IN_CUSTOMER_CODE = 5;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.AbstractMessage#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		Long orderNumber = (Long) getIncomingMessage().getData(IN_ORDER_NUMBER);
		Long articleCode = (Long) getIncomingMessage().getData(IN_ARTICLE_CODE);
		Long promotionCode = (Long) getIncomingMessage().getData(IN_PROMOTION_CODE);
		Double itemQuantity = (Double) getIncomingMessage().getData(IN_QUANTITY);
		Integer pricesList = (Integer) getIncomingMessage().getData(IN_PRICESLIST);
		Long customerCode = (Long) getIncomingMessage().getData(IN_CUSTOMER_CODE);
		
		try {
			validateUser(getIncomingMessage().getUserId());
			if (articleCode != null) {
				getSalesModule().getSalesInterface().bookArticle(orderNumber, customerCode, articleCode, itemQuantity, pricesList, getIncomingMessage().getUserId());
			} else {
				getSalesModule().getSalesInterface().bookPromotion(orderNumber, customerCode, promotionCode, itemQuantity, pricesList, getIncomingMessage().getUserId());
			}
			ServerToClientMessage returnMessage = new ServerToClientMessage();
			returnMessage.setResult(MessageResult.RESULT_OK);
			
			return singleMessage(returnMessage);
			
		} catch (OctopusException ex) {
			handleBlockedUser(ex, IN_ORDER_NUMBER);
			return errorMessage(ex.getErrorCode());
		}
	}

}
