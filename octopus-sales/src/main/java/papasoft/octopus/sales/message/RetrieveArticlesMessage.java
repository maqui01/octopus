/**
 * 
 */
package papasoft.octopus.sales.message;

import java.util.ArrayList;
import java.util.List;

import papasoft.octopus.domain.Article;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.message.ServerToClientMessage;

/**
 * @author maqui
 *
 */
public class RetrieveArticlesMessage extends SalesMessageHandler {

	private static final int OUT_ARTICLE = 0;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.message.handler.MessageHandler#handleMessage()
	 */
	@Override
	public List<ServerToClientMessage> handleMessage() {
		List<Article> articles = null;
		try {
			validateUser(getIncomingMessage().getUserId());
			articles = getSalesModule().getSalesInterface().getArticles(getIncomingMessage().getUserId());
		} catch (OctopusException e) {
			return errorMessage(e.getErrorCode());
		}
		if (articles == null || articles.size() == 0) {
			return errorMessage(MessageResult.NO_ARTICLES_TO_RETRIEVE);
		}
		List<ServerToClientMessage> responseMessages = new ArrayList<ServerToClientMessage>();
		for (Article article : articles) {
			ServerToClientMessage response = new ServerToClientMessage();
			response.putData(OUT_ARTICLE, article);
			responseMessages.add(response);
		}
		return responseMessages;
	}

}
