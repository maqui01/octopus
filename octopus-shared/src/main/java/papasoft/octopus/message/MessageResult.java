/**
 * 
 */
package papasoft.octopus.message;

/**
 * @author maqui
 *
 */
public class MessageResult {

	public static final String RESULT_OK = "00000";
	public static final String INVALID_USER_PASSWORD = "00001";
	public static final String MESSAGE_UNRESPONSE = "00002";
	public static final String HEADER_INVALID_MODULE_OR_MESSAGE_TYPE = "00003";
	public static final String GENERIC_SERVER_ERROR = "00004";
	public static final String SERVER_DATABASE_ERROR = "00005";
	public static final String USER_BLOCKED = "00006";
	public static final String ORDER_NOT_FOUND = "10000";
	public static final String ARTICLE_NOT_FOUND = "10001";
	public static final String STOCK_INSUFFICIENT = "10002";
	public static final String PROMOTION_NOT_FOUND = "10003";
	public static final String ORDER_ALREADY_CONFIRMED = "10004";
	public static final String NO_ARTICLES_TO_RETRIEVE = "10005";
	public static final String NO_CUSTOMERS_FOR_SALESMAN = "10006";
	public static final String CUSTOMER_DISABLED = "10007";
	public static final String CUSTOMER_NOT_AUTHORIZED_TO_SELL_TODAY = "10008";
	public static final String COMPANY_NAME_IN_USE = "10009";
	public static final String COMPANY_NOT_HAVE_REMAINING_ORDERS = "10010";
}
