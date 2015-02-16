/**
 * 
 */
package papasoft.octopus.sales.module;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.Customer;
import papasoft.octopus.domain.Order;
import papasoft.octopus.domain.SalesUser;
import papasoft.octopus.domain.se.Company;
import papasoft.octopus.domain.se.IabOrder;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.minterface.ModuleInterface;

/**
 * @author maqui
 *
 */
public interface SalesInterface extends ModuleInterface {
	
	public SalesUser getUser(String username, String companyName) throws OctopusException;
	
	public SalesUser getUser(Long id) throws OctopusException;
	
	public List<Article> getArticles(Long userId) throws OctopusException;
	
	public HashMap<Customer, Integer[]> getCustomersForUser(Long userId) throws OctopusException;

	public Long createOrder(Long userId, Long customerCode) throws OctopusException;

	public void confirmOrder(Long orderNumber, Long customerCode, Long userId) throws OctopusException;

	public void cancelOrder(Long orderNumber, Long userId) throws OctopusException;

	public void bookArticle(Long orderNumber, Long customerCode, Long articleCode,
			Double itemQuantity, Integer pricesList, Long userId) throws OctopusException;

	public void bookPromotion(Long orderNumber, Long customerCode, Long promotionCode,
			Double itemQuantity, Integer pricesList, Long userId) throws OctopusException;

	public void cancelPromotionBooking(Long orderNumber, Long customerCode, Long promotionCode, Double promotionQuantity, Long userId) throws OctopusException;
	
	public void cancelArticleBooking(Long orderNumber, Long customerCode, Long articleCode, Double articleQuantity, Long userId) throws OctopusException;

	public List<Order> retrievePendingOrders() throws OctopusException;
	
	public void createCompany(String companyName, String userName, String password, String email) throws OctopusException;

	public IabOrder retrieveIabOrder(String orderId);

	public void saveIabOrder(String orderId, String sku, String inappToken, Integer status) throws OctopusException;

	public Integer getCompanyRemainingOrders(Long companyId);
	
	public Integer updateCompanyRemainingOrders(Long companyId, Integer quantity)  throws OctopusException;

	public String createTestCompany();

	public List<Company> retrieveTestingCompaniesToPurge(Date oldestThan) throws OctopusException;

	public void deleteTestingCompany(Company company) throws OctopusException;

}
