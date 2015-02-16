/**
 * 
 */
package papasoft.octopus.se.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import papasoft.octopus.dao.AbstractDao;
import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.Customer;
import papasoft.octopus.domain.Order;
import papasoft.octopus.domain.PromotionItem;
import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.User;
import papasoft.octopus.domain.se.Company;
import papasoft.octopus.domain.se.IabOrder;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.message.MessageResult;
import papasoft.octopus.se.domain.SEArticle;
import papasoft.octopus.se.domain.SECustomer;
import papasoft.octopus.se.domain.SEOrder;
import papasoft.octopus.se.domain.SEPromotion;
import papasoft.octopus.se.domain.SESalesUser;
import papasoft.octopus.se.domain.Stock;

/**
 * @author maqui
 *
 */
public class SalesDao extends AbstractDao {

	private static final String PRICES_LIST_QUERY = "SELECT priceId FROM article_price WHERE companyId = ? GROUP BY priceId;";

	/**
	 * @param username
	 * @return
	 * @throws OctopusException 
	 */
	public SESalesUser getUser(Long userId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			return (SESalesUser) session.get(SESalesUser.class, userId);
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving sales user", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings("unchecked")
	public List<SEArticle> getAllArticles(Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(SEArticle.class);
			criteria.add(Restrictions.eq("companyId", companyId));
			criteria.addOrder(org.hibernate.criterion.Order.asc("class"));
			List<SEArticle> articles = criteria.list();
			disconnectArticles(articles);
			return articles;
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving all articles", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * @param articles
	 */
	private void disconnectArticles(List<SEArticle> articles) {
		if (articles != null) {
			for (Article article : articles) {
				article.setPrices(new ArrayList<Double>(article.getPrices()));
				if (article instanceof SEPromotion) {
					SEPromotion prom = (SEPromotion)article;
					prom.setItems(new ArrayList<PromotionItem>(prom.getItems()));
				}
			}
		}
	}

	/**
	 * @param customerId
	 * @param companyId
	 * @return
	 * @throws OctopusException 
	 */
	public Customer getCustomer(Long customerId, Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(SECustomer.class);
			crit.add(Restrictions.eq("id", customerId));
			crit.add(Restrictions.eq("companyId", companyId));
			return (Customer) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving customer", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param order
	 * @throws OctopusException 
	 */
	public void saveOrder(Order order) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.saveOrUpdate(order);
			session.flush();
		} catch (Throwable ex) {
			LogManager.logError("Error saving order", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param order
	 * @throws OctopusException 
	 */
	public void deleteOrder(Order order) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.delete(order);
			session.flush();
		} catch (Throwable ex) {
			LogManager.logError("Error deleting order", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param parseLong
	 * @return
	 * @throws OctopusException 
	 */
	public SEOrder getOrder(Long id, Long userId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(SEOrder.class);
			crit.add(Restrictions.eq("id", id)).createCriteria("user").add(Restrictions.idEq(userId));
			return (SEOrder) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving order", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param parseLong
	 * @return
	 * @throws OctopusException 
	 */
	public Article getArticle(Long id, Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(SEArticle.class);
			crit.add(Restrictions.idEq(new Serializable[]{id, companyId}));
			crit.setFetchMode("prices", FetchMode.JOIN);
			return (Article) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving article", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param order
	 * @param article
	 * @param itemQuantity
	 * @param pricesList
	 * @return
	 * @throws OctopusException 
	 */
	public Boolean bookArticle(SEOrder order, Article article,
			Double itemQuantity, Integer pricesList) throws OctopusException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			if (doBookArticle(order, article, itemQuantity, pricesList, session)) {
				tr.commit();
				return true;
			} else {
				tr.rollback();
				return false;
			}
		} catch (Throwable ex) {
			if (tr != null) {
				tr.rollback();
			}
			LogManager.logError("Error checking article stock", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param order
	 * @param article
	 * @param itemQuantity
	 * @param pricesList
	 * @param object
	 * @param session
	 * @return
	 * @throws OctopusException 
	 */
	private boolean doBookArticle(SEOrder order, Article article,
			Double itemQuantity, Integer pricesList, Session session) throws OctopusException {
		// Check initial stock
		Double initialStock = getStockQuantity(article, session);
		if (itemQuantity > 0 && (initialStock == null || itemQuantity > initialStock)) {
			return false;
		}			
		
		// Add article to order
		addArticleToOrder(order, article, itemQuantity, pricesList, session);
		// Check final stock
		Double finalStock = getStockQuantity(article, session);
		
		if (itemQuantity < 0 || finalStock >= 0.0) {
			return true;
		} else {
			LogManager.logError("Concurrency error: Initial stock was not equal to booked quantity plus final stock - order [" + order.getId() + "] art [" + article.getCode() + "]");
			return false;
		}
	}
	
	/**
	 * 
	 * @param order
	 * @param article
	 * @param itemQuantity
	 * @param pricesList
	 * @param promotionArticle
	 * @param session
	 * @param errorCount
	 * @throws OctopusException
	 */
	private void addArticleToOrder(SEOrder order, Article article,
			Double itemQuantity, Integer pricesList, Session session) throws OctopusException {
		
		try {
			order.addArticle(article, itemQuantity, pricesList);			
			session.update(order);
			Stock stock = getStock(article, session);
			stock.setStock(stock.getStock() - itemQuantity);
			session.update(stock);
			session.flush();
		} catch (Throwable th) {
			throw new OctopusException("Could not add new article to order [" + order.getId() + "]", th, MessageResult.SERVER_DATABASE_ERROR);
		}
	}

	/**
	 * @param article
	 * @param session
	 * @return
	 * @throws OctopusException 
	 */
	private Stock getStock(Article article, Session session) throws OctopusException {
		try {
			Query q = session.createQuery("FROM Stock st where st.article.code = ? and st.article.companyId = ?");
			q.setLong(0, article.getCode());
			q.setLong(1, ((SEArticle)article).getCompanyId());
			return (Stock) q.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving article stock", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		}
	}
	
	/**
	 * 
	 * @param article
	 * @return
	 * @throws OctopusException 
	 */
	public Stock getStock(Article article) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Query q = session.createQuery("FROM Stock st where st.article.code = ? and st.article.companyId = ?");
			q.setLong(0, article.getCode());
			q.setLong(1, ((SEArticle)article).getCompanyId());
			return (Stock) q.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving article stock", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param article
	 * @param session
	 * @return
	 * @throws OctopusException
	 */
	private Double getStockQuantity(Article article, Session session) throws OctopusException {
		try {
			Stock stock = getStock(article, session);
			if (stock != null) {
				return stock.getStock();
			}
			return 0.0;
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving article stock quantity", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		}
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings("unchecked")
	public List<Order> retrievePendingOrders() throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Order.class);
			criteria.add(Restrictions.eq("status", Order.STATUS_ON_EDITION));
			return criteria.list();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving pending orders", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings("unchecked")
	public List<SEOrder> retrieveOrders(Company company) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(SEOrder.class);
			criteria.add(Restrictions.eq("companyId", company.getId()));
			return criteria.list();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving orders", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param username
	 * @param companyName
	 * @return
	 * @throws OctopusException 
	 */
	public SESalesUser getUser(String username, String companyName) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(SESalesUser.class);
			crit.add(Restrictions.eq("name", username));
			crit.createCriteria("company").add(Restrictions.eq("name", companyName));
			return (SESalesUser) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving user", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws OctopusException
	 */
	public Role getRole(String name) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(Role.class);
			crit.add(Restrictions.eq("name", name));
			return (Role) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving role", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	

	/**
	 * @param id
	 * @return
	 * @throws OctopusException 
	 */
	public Company getCompany(Long id) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			return (Company) session.get(Company.class, id);
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving company", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws OctopusException
	 */
	public void save(Object obj) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.save(obj);
			session.flush();
		} catch ( ConstraintViolationException ex ) {
			LogManager.logError("Error saving entity", ex);
			throw new OctopusException(ex, MessageResult.COMPANY_NAME_IN_USE);
		} catch (Throwable ex) {
			LogManager.logError("Error saving entity", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @throws OctopusException
	 */
	public void delete(Object obj) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.delete(obj);
			session.flush();
		} catch (Throwable ex) {
			LogManager.logError("Error deleting entity", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @param orderId
	 * @return
	 */
	public IabOrder getIabOrder(String orderId) {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(IabOrder.class);
			crit.add(Restrictions.eq("orderId", orderId));
			return (IabOrder) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving iabOrder", ex);
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param companyId
	 * @param quantity
	 * @return
	 * @throws OctopusException 
	 */
	public Integer updateCompanyRemainingOrders(Long companyId, Integer quantity) throws OctopusException {
		Session session = null;
		Transaction tr = null;
		try {
			session = getSession();
			tr = session.beginTransaction();
			
			Company company = (Company) session.get(Company.class, companyId);
			company.setRemainingOrders( company.getRemainingOrders() + quantity );
			
			session.update( company );
			tr.commit();
			return company.getRemainingOrders();
		} catch (Throwable ex) {
			if (tr != null) {
				tr.rollback();
			}
			LogManager.logError("Error updating company remaining orders", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public long getLastTestCompanyName() {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(Company.class)
					.add(Restrictions.eq("testCompany", true))
					.addOrder(org.hibernate.criterion.Order.desc("name"))
					.setMaxResults(1);
			Company comp = (Company) crit.uniqueResult();
			Long max = null;
			if (comp != null)
				max = Long.valueOf( comp.getName() );
			return max != null ? max : 0;
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving last test company name", ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return 0;
	}

	/**
	 * 
	 * @param oldestThan
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings("unchecked")
	public List<Company> retrieveTestingCompaniesToPurge(Date oldestThan) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(Company.class);
			criteria.add( Restrictions.eq("testCompany", true) );
			criteria.add( Restrictions.le("creationDate", oldestThan) );
			return criteria.list();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving testing companies to purge", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws OctopusException 
	 */
	public Company getCompany(String name) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria crit = session.createCriteria(Company.class);
			crit.add(Restrictions.eq("name", name));
			return (Company) crit.uniqueResult();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving company", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void deleteStockInfo(Company company) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Query query = session.createSQLQuery(
					"DELETE FROM stock WHERE companyId = :companyId")
					.setParameter("companyId", company.getId());
			query.executeUpdate();
		} catch (Throwable ex) {
			LogManager.logError("Error deleting stock info", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param customerId
	 * @param dateSince
	 * @param dateTo
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SEOrder> retrieveOrders(String customerfilter, Long salesUserId, Long orderIdFrom, 
			Long orderIdTo, Integer status, Date dateSince, Date dateTo, Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(SEOrder.class);
			criteria.addOrder(org.hibernate.criterion.Order.asc("id"));
			criteria.setFetchMode("customer", FetchMode.JOIN);
			criteria.setFetchMode("user", FetchMode.JOIN);
			
			if (customerfilter != null && customerfilter.trim().length() > 0) {
				if ( isLong(customerfilter) ) {
					criteria.createCriteria("customer").add(Restrictions.or(
							Restrictions.eq("id",Long.parseLong(customerfilter)),
							Restrictions.like("name","%"+customerfilter+"%") ));
				} else {
					criteria.createCriteria("customer").add(Restrictions.like("name","%"+customerfilter+"%"));
				}
			}
			
			if (salesUserId != null) {
				criteria.createCriteria("user").add(Restrictions.eq("id",salesUserId));
			}
			
			if (orderIdFrom != null) {
				criteria.add(Restrictions.ge("id",orderIdFrom));
			}
			
			if (orderIdTo != null) {
				criteria.add(Restrictions.le("id",orderIdTo));
			}
			
			if (status != null) {
				criteria.add(Restrictions.eq("status",status));
			}
			
			if (dateSince != null) {
				criteria.add(Restrictions.ge("date", dateSince));
			}
			if (dateTo != null) {
				dateTo = new Date(dateTo.getTime() + 86399000L);
				criteria.add(Restrictions.lt("date", dateTo));
			}
			if (companyId != null) {
				criteria.add(Restrictions.eq("companyId", companyId));
			}
			List list = criteria.list();
			session.getTransaction().commit();
			return list;
		} catch (Throwable ex) {
			if (session != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving orders", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private boolean isLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch ( NumberFormatException e ) {
			return false;
		}
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SECustomer> retrieveAllCustomers(User user) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(SECustomer.class);
			criteria.add(Restrictions.eq("companyId", ((SESalesUser)user).getCompany().getId()));
			criteria.addOrder(org.hibernate.criterion.Order.asc("name"));
			
			List list = criteria.list();
			session.getTransaction().commit();
			return list;
		} catch (Throwable ex) {
			if (session != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving all customers", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param role
	 * @param companyName
	 * @return
	 * @throws OctopusException
	 */
	@SuppressWarnings("unchecked")
	public List<SECustomer> retrieveAllCustomers(String name, String companyName) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Criteria crit = session.createCriteria(SECustomer.class);
			crit.createCriteria("company").add( Restrictions.eq("name", companyName) );
				
			if (name != null) {
				crit.add(Restrictions.like("name", "%" + name + "%"));
			}
			crit.addOrder(org.hibernate.criterion.Order.asc("name"));
			
			List<SECustomer> list = crit.list();
			session.getTransaction().commit();
			return list;
		} catch (Throwable ex) {
			if (session != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving sales customers", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 
	 * @param role
	 * @param companyName
	 * @return
	 * @throws OctopusException
	 */
	@SuppressWarnings("unchecked")
	public List<SESalesUser> retrieveAllUsers(String name, Collection<String> roles, String companyName) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Criteria crit = session.createCriteria(SESalesUser.class);
			crit.createCriteria("company").add( Restrictions.eq("name", companyName) );
			if ( roles != null && !roles.isEmpty() ) {
				Disjunction rolesCrit = Restrictions.disjunction();
				for ( String role : roles ) {
					rolesCrit.add( Restrictions.eq("name", role) );
				}
				crit.createCriteria("roles").add(rolesCrit);
			}
				
			if (name != null) {
				crit.add(Restrictions.like("name", "%" + name + "%"));
			}
			crit.addOrder(org.hibernate.criterion.Order.asc("name"));
			
			List<SESalesUser> list = crit.list();
			session.getTransaction().commit();
			return list;
		} catch (Throwable ex) {
			if (session != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving sales user", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Collection<Role> retrieveAllRoles() throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			List list = session.createCriteria(Role.class).list();
			session.getTransaction().commit();
			return list;
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving roles", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * @param parseInt
	 * @return
	 * @throws OctopusException 
	 */
	public Role retrieveRole(Long id) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Role role = (Role) session.load(Role.class, id);
			Hibernate.initialize(role);
			session.getTransaction().commit();
			return role;
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving role with id=" + id, ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * @param parseInt
	 * @return
	 * @throws OctopusException 
	 */
	public Customer retrieveCustomer(Long id, Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			Criteria crit = session.createCriteria(SECustomer.class);
			crit.add(Restrictions.eq("id", id));
			crit.add(Restrictions.eq("companyId", companyId));
			Customer customer = (Customer) crit.uniqueResult();
			session.getTransaction().commit();
			return customer;
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving customer with id=" + id, ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}

	/**
	 * @param user
	 * @throws OctopusException 
	 */
	public void saveUser(SESalesUser user) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.saveOrUpdate(user);
			session.getTransaction().commit();
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error saving user: " + user.getName() , ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * 
	 * @param customer
	 * @throws OctopusException
	 */
	public void saveOrUpdate(Object entity) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			session.saveOrUpdate(entity);
			session.getTransaction().commit();
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error saving entity: " + entity.getClass() , ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * 
	 * @param customer
	 * @throws OctopusException
	 */
	public void saveOrUpdate(SEArticle article) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			
			if (article.getCode() != null) {
				session.update(article);
			} else {
				saveArticle(session, article);
			}
			
			session.getTransaction().commit();
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error saving article [" + article.getCode() + "]" , ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * @param article
	 */
	private synchronized void saveArticle(Session session, SEArticle article) {
		SQLQuery query = session.createSQLQuery("SELECT MAX(code) FROM article art WHERE art.company = ?;");
		query.setLong(0, article.getCompanyId());
		Long nextId = ((BigInteger) query.uniqueResult()).longValue();
		nextId++;
		article.setCode(nextId);
		session.save(article);
	}

	/**
	 * 
	 * @param companyId
	 * @return
	 * @throws OctopusException
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getAllPricesList(Long companyId) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(PRICES_LIST_QUERY);
			query.setLong(0, companyId);
			List<Integer> list = query.list();
			return list;
		} catch (Throwable ex) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			LogManager.logError("Error retrieving prices list", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);	
		} finally {
			if (session != null)
				session.close();
		}
	}
	
	/**
	 * 
	 * @param companyId
	 * @param promoArticleCode
	 * @param promoArticleName
	 * @return
	 * @throws OctopusException
	 */
	@SuppressWarnings("unchecked")
	public List<SEArticle> retrieveArticles(Long companyId, Long articleCode, String articleName, Boolean includePromotions) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Criteria criteria = session.createCriteria(SEArticle.class);
			criteria.add(Restrictions.eq("companyId", companyId));
			if (articleCode != null) {
				criteria.add(Restrictions.eq("code", articleCode));
			}
			if (articleName != null) {
				criteria.add(Restrictions.like("name", articleName, MatchMode.ANYWHERE));				
			}
			if (includePromotions != null && !includePromotions) {
				criteria.add(Restrictions.eq("class", SEArticle.class));
			}
			criteria.addOrder(org.hibernate.criterion.Order.asc("code"));
			return criteria.list();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving all single articles", ex);
			throw new OctopusException(ex, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param article
	 * @param articleQuantity
	 * @throws OctopusException 
	 */
	public void cancelArticleBooking(SEOrder order, Article article, Double articleQuantity) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.beginTransaction();
			order.removeArticle(article, articleQuantity);
			session.saveOrUpdate(order);
			Stock stock = getStock(article, session);
			if (stock != null) {
				stock.setStock(stock.getStock() + articleQuantity);
				session.update(stock);
			}
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e) {
			LogManager.logError("Error cancelling article", e);
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			throw new OctopusException(e, MessageResult.SERVER_DATABASE_ERROR);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
