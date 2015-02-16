/**
 * 
 */
package papasoft.octopus.se.sales.minterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.Customer;
import papasoft.octopus.domain.Order;
import papasoft.octopus.domain.PromotionItem;
import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.SalesUser;
import papasoft.octopus.domain.User;
import papasoft.octopus.domain.se.Company;
import papasoft.octopus.domain.se.IabOrder;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.sales.module.SalesInterface;
import papasoft.octopus.se.dao.SalesDao;
import papasoft.octopus.se.domain.SEArticle;
import papasoft.octopus.se.domain.SEItem;
import papasoft.octopus.se.domain.SEOrder;
import papasoft.octopus.se.domain.SEPromotion;
import papasoft.octopus.se.domain.SESalesUser;
import papasoft.octopus.se.domain.SalesUserCustomer;
import papasoft.octopus.se.domain.Stock;

/**
 * @author maqui
 *
 */
public class SESalesInterface implements SalesInterface {

	private SalesDao salesDao;
	
	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#getUser(java.lang.String)
	 */
	@Override
	public SalesUser getUser(String username, String companyName) throws OctopusException {
		SESalesUser user = getSalesDao().getUser(username, companyName);
		if ( user == null )
			return null;
		return user;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#getArticles()
	 */
	@Override
	public List<Article> getArticles(Long userId) throws OctopusException {
		SESalesUser user = getSalesDao().getUser(userId);
		List<Article> articles = new ArrayList<Article>();
		articles.addAll( getSalesDao().getAllArticles(user.getCompany().getId()) );
		return articles;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#getCustomersForUser(java.lang.String)
	 */
	@Override
	public HashMap<Customer, Integer[]> getCustomersForUser(Long userId)
			throws OctopusException {
		SESalesUser user = getSalesDao().getUser(userId);
		if (user == null) {
			return null;
		}
		HashMap<Customer, Integer[]> customersForUser = new HashMap<Customer, Integer[]>();
		for (SalesUserCustomer custUser : user.getCustomers()) {
			Customer cust = custUser.getCustomer();
			customersForUser.put(cust, custUser.getIntDaysToVisit());
		}
		return customersForUser;
	}

	/**
	 * @param daysToVisit
	 * @return
	 */


	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#createOrder(java.lang.String, java.lang.String)
	 */
	@Override
	public Long createOrder(Long userId, Long customerCode) throws OctopusException {
		Customer customer = validateCustomer(customerCode, userId);
		
		SESalesUser user = getSalesDao().getUser(userId);
		user.setCompany( getSalesDao().getCompany(user.getCompany().getId()) );
		
		// Veo que la company tenga pedidos
		if ( user.getCompany().getRemainingOrders() <= 0 ) {
			throw new OctopusException("Company [" + user.getCompany().getId() + "] does not have remaining orders.", MessageResult.COMPANY_NOT_HAVE_REMAINING_ORDERS);
		}
		
		SEOrder order = new SEOrder();
		order.setCustomer(customer);
		order.setStatus(Order.STATUS_ON_EDITION);
		order.setDate(new Date());
		order.setUser(user);
		order.setCompany(user.getCompany());
		getSalesDao().saveOrder(order);
		return order.getId();
	}

	/**
	 * @param customerCode
	 * @throws OctopusException 
	 */
	private Customer validateCustomer(Long customerCode, Long userId) throws OctopusException {
		SESalesUser user = getSalesDao().getUser(userId);
		
		// Me fijo si es una test company
		if (  getSalesDao().getCompany(user.getCompany().getId()).getTestCompany() ) {
			Company templateCompany = getSalesDao().getCompany(Company.GUEST_COMPANY_NAME);
			return getSalesDao().getCustomer(customerCode, templateCompany.getId());
		}
		
		Customer customer = getSalesDao().getCustomer(customerCode, user.getCompany().getId());
		if (customer == null) {
			throw new OctopusException("Customer [" + customerCode + "] does not exist", MessageResult.CUSTOMER_DISABLED);
		}
		if (user != null && !user.canSellToCustomerToday(customer)) {
			throw new OctopusException("Customer [" + customerCode + " not authorized to sell today", MessageResult.CUSTOMER_NOT_AUTHORIZED_TO_SELL_TODAY);
		}
		return customer;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#confirmOrder(java.lang.String, java.lang.String)
	 */
	@Override
	public void confirmOrder(Long orderNumber, Long customerCode, Long userId)
			throws OctopusException {
		SEOrder order = (SEOrder) getOrder(orderNumber, userId);
		validateOrder(order);
		validateCustomer(customerCode, userId);
		
		order.setStatus(Order.STATUS_CONFIRMED);
		getSalesDao().saveOrder(order);
	}
	
	/**
	 * @param orderNumber
	 * @param order
	 * @throws OctopusException
	 */
	private SEOrder getOrder(Long orderNumber, Long userId)
			throws OctopusException {
		SEOrder order = getSalesDao().getOrder(orderNumber, userId);
		if (order == null) {
			throw new OctopusException("Order [" + orderNumber + "] does not exist", MessageResult.ORDER_NOT_FOUND);
		}
		return order;
	}
	
	/**
	 * 
	 * @param order
	 * @throws OctopusException
	 */
	private void validateOrder(Order order) throws OctopusException {
		if (order == null) {
			return;
		}
		if (order.getStatus().equals(Order.STATUS_CONFIRMED)) {
			throw new OctopusException("Order [" + order.getId() + "] has been already confirmed", MessageResult.ORDER_ALREADY_CONFIRMED);
		}
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#cancelOrder(java.lang.String)
	 */
	@Override
	public void cancelOrder(Long orderNumber, Long userId) throws OctopusException {
		SEOrder order = getOrder(orderNumber, userId);
		validateOrder(order);
		for (SEItem item : order.getSeItems()) {
			cancelArticleBooking(orderNumber, order.getCustomer().getId(), item.getArticleCode(), item.getQuantity(), userId);
		}
		order.setStatus(Order.STATUS_CANCELED);
		getSalesDao().saveOrder(order);
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#bookArticle(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.String)
	 */
	@Override
	public void bookArticle(Long orderNumber, Long customerCode,
			Long articleCode, Double itemQuantity, Integer pricesList, Long userId)
			throws OctopusException {
		SEOrder order = (SEOrder) getOrder(orderNumber, userId);
		validateOrder(order);
		validateCustomer(customerCode, userId);
		
		Article article = getSalesDao().getArticle(articleCode, order.getCompanyId());
		if (article == null) {
			throw new OctopusException("Article [" + articleCode + "] does not exist", MessageResult.ARTICLE_NOT_FOUND);
		}
		Boolean booked = getSalesDao().bookArticle(order, article, itemQuantity, pricesList);
		if (!booked) {
			throw new OctopusException("Stock insufficient", MessageResult.STOCK_INSUFFICIENT);
		}
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#bookPromotion(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.String)
	 */
	@Override
	public void bookPromotion(Long orderNumber, Long customerCode,
			Long promotionCode, Double itemQuantity, Integer pricesList, Long userId)
			throws OctopusException {
		bookArticle(orderNumber, customerCode, promotionCode, itemQuantity, pricesList, userId);
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#cancelPromotionBooking(java.lang.String, java.lang.String, java.lang.String, java.lang.Double)
	 */
	@Override
	public void cancelPromotionBooking(Long orderNumber, Long customerCode,
			Long promotionCode, Double promotionQuantity, Long userId)
			throws OctopusException {
		cancelArticleBooking(orderNumber, customerCode, promotionCode, promotionQuantity, userId);

	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#cancelArticleBooking(java.lang.String, java.lang.String, java.lang.String, java.lang.Double)
	 */
	@Override
	public void cancelArticleBooking(Long orderNumber, Long customerCode,
			Long articleCode, Double articleQuantity, Long userId) throws OctopusException {
		SEOrder order = (SEOrder) getOrder(orderNumber, userId);
		validateOrder(order);
		validateCustomer(customerCode, userId);
		
		getSalesDao().cancelArticleBooking(order, getSalesDao().getArticle(articleCode, order.getCompanyId()), articleQuantity);
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#retrievePendingOrders()
	 */
	@Override
	public List<Order> retrievePendingOrders() throws OctopusException {
		return getSalesDao().retrievePendingOrders();
	}

	/**
	 * @return the salesDao
	 */
	public SalesDao getSalesDao() {
		return salesDao;
	}

	/**
	 * @param salesDao the salesDao to set
	 */
	public void setSalesDao(SalesDao salesDao) {
		this.salesDao = salesDao;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.sales.module.SalesInterface#getUser(java.lang.Long)
	 */
	@Override
	public SalesUser getUser(Long userId) throws OctopusException {
		return getSalesDao().getUser(userId);
	}

	/**
	 * 
	 */
	@Override
	public void createCompany(String companyName, String userName, String password, String email) throws OctopusException {
		Company company = new Company();
		company.setName(companyName);
		company.setEmail(email);
		company.setCreationDate(new Date());
		getSalesDao().save(company);
		
		SESalesUser user = new SESalesUser();
		user.setName(userName);
		user.getRoles().add( getSalesDao().getRole( Role.ADMIN_ROLE ) );
		user.setPassword(password);
		user.setCompany(company);
		
		getSalesDao().save(user);
	}

	/**
	 * 
	 */
	@Override
	public IabOrder retrieveIabOrder(String orderId) {
		return getSalesDao().getIabOrder(orderId);
	}

	/**
	 * @throws OctopusException 
	 * 
	 */
	@Override
	public void saveIabOrder(String orderId, String sku, String inappToken, Integer status) throws OctopusException {
		IabOrder order = new IabOrder();
		order.setOrderId(orderId);
		order.setSku(sku);
		order.setToken(inappToken);
		order.setDate(new Date());
		order.setStatus(status);
		getSalesDao().save(order);
	}

	/**
	 * 
	 */
	@Override
	public Integer getCompanyRemainingOrders(Long companyId) {
		try {
			Company company = getSalesDao().getCompany(companyId);
			return company.getRemainingOrders();
		} catch (OctopusException ex) {
			LogManager.logError("Error retrieving company remaining orders", ex);
		}
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public Integer updateCompanyRemainingOrders(Long companyId, Integer quantity) throws OctopusException {
		return getSalesDao().updateCompanyRemainingOrders(companyId, quantity);
	}

	/**
	 * 
	 */
	@Override
	public synchronized String createTestCompany() {
		try {
			// Company
			Company company = new Company();
			company.setName( Long.toString(getSalesDao().getLastTestCompanyName()+1) );
			company.setTestCompany(true);
			company.setRemainingOrders(100);
			company.setCreationDate(new Date());
			getSalesDao().save(company);
			
			// User
			SESalesUser userTemplate = getSalesDao().getUser(User.GUEST_USER_NAME, Company.GUEST_COMPANY_NAME);
			SESalesUser user = new SESalesUser();
			user.setName(User.GUEST_USER_NAME);
			user.getRoles().add( getSalesDao().getRole( Role.ADMIN_ROLE ) );
			user.setPassword(User.GUEST_USER_PASSWORD);
			user.setCompany(company);
			user.setCustomers(new ArrayList<SalesUserCustomer>());
			user.getCustomers().addAll(userTemplate.getCustomers());
			getSalesDao().save(user);

			// Articles
			List<SEArticle> articles = generateTestArticles(userTemplate.getCompany().getId(), company.getId());
			for ( SEArticle article : articles ) {
				getSalesDao().save(article);
				Stock stock = new Stock();
				stock.setArticle(article);
				stock.setStock(100.0);
				getSalesDao().save(stock);
			}
			
			return company.getName();
		} catch (Throwable th) {
			LogManager.logError("Error creating test company", th);
			return null;
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws OctopusException 
	 */
	private List<SEArticle> generateTestArticles(Long templateCompanyId, Long newCompanyId) throws OctopusException {
		List<SEArticle> articles = getSalesDao().getAllArticles(templateCompanyId);
		for ( SEArticle article : articles ) {
			article.setCompanyId(newCompanyId);
			if (article instanceof SEPromotion) {
				SEPromotion prom = (SEPromotion)article;
				for ( PromotionItem item : prom.getItems() ) {
					item.setId(null);
				}
			}
		}
		return articles;
	}

	/**
	 * 
	 */
	@Override
	public List<Company> retrieveTestingCompaniesToPurge( Date oldestThan ) throws OctopusException {
		return getSalesDao().retrieveTestingCompaniesToPurge(oldestThan);
	}

	/**
	 * 
	 */
	@Override
	public void deleteTestingCompany(Company company) throws OctopusException {
		SESalesUser testUser = getSalesDao().getUser(User.GUEST_USER_NAME, company.getName());
		
		//Orders
		List<SEOrder> orders = getSalesDao().retrieveOrders(company);
		for ( SEOrder order : orders ) {
			getSalesDao().delete(order);
		}
		
		//Articles
		getSalesDao().deleteStockInfo(company);
		List<SEArticle> articles = getSalesDao().getAllArticles(company.getId());
		//Primero tengo que eliminar las promotions:
		Collections.sort(articles, new Comparator<SEArticle>() {
			public int compare(SEArticle a1, SEArticle a2) {
				if (a1 instanceof SEPromotion && !(a2 instanceof SEPromotion)) {
					return -1;
				}
				if (a2 instanceof SEPromotion && !(a1 instanceof SEPromotion)) {
					return 1;
				}
				return 0;
			}
		});
		for ( SEArticle article : articles ) {
			getSalesDao().delete(article);
		}
		
		//User
		getSalesDao().delete(testUser);
		
		//Company
		getSalesDao().delete(company);
	}
	
	

}
