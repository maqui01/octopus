/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import papasoft.octopus.domain.PromotionItem;
import papasoft.octopus.se.domain.SEArticle;
import papasoft.octopus.se.domain.SECustomer;
import papasoft.octopus.se.domain.SESalesUser;
import papasoft.octopus.se.domain.Stock;
import papasoft.octopus.webapp.utils.ValueHolder;

/**
 * @author maqui
 *
 */
@SessionScoped
@ManagedBean
public class CrudContext {
	
	private List<SESalesUser> users;
	private SESalesUser selectedUser;
	private Boolean passwordChanged = false;
	private SECustomer selectedCustomer;
	private List<SECustomer> customers;
	private List<Integer> pricesList;
	private List<SEArticle> articles;
	private SEArticle selectedArticle;
	private List<ValueHolder> selectedArticlePrices;
	private List<SEArticle> promoArticlesToAdd;
	private Collection<PromotionItem> selectedPromotionItems;
	private Stock selectedStock;

	/**
	 * @return the users
	 */
	public List<SESalesUser> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<SESalesUser> users) {
		this.users = users;
	}

	/**
	 * @return the selectedUser
	 */
	public SESalesUser getSelectedUser() {
		return selectedUser;
	}

	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(SESalesUser selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	/**
	 * 
	 * @return
	 */
	public static CrudContext getCtx() {
		CrudContext bean = (CrudContext) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("crudContext");
		if (bean == null) {
			bean = new CrudContext();
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("crudContext", bean);
		}
		return bean;
	}

	/**
	 * @return the passwordChanged
	 */
	public Boolean getPasswordChanged() {
		return passwordChanged;
	}

	/**
	 * @param passwordChanged the passwordChanged to set
	 */
	public void setPasswordChanged(Boolean passwordChanged) {
		this.passwordChanged = passwordChanged;
	}

	/**
	 * @return the selectedCustomer
	 */
	public SECustomer getSelectedCustomer() {
		return selectedCustomer;
	}

	/**
	 * @param selectedCustomer the selectedCustomer to set
	 */
	public void setSelectedCustomer(SECustomer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	/**
	 * @return
	 */
	public List<SECustomer> getCustomers() {
		return this.customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(List<SECustomer> customers) {
		this.customers = customers;
	}

	/**
	 * @return the pricesList
	 */
	public List<Integer> getPricesList() {
		return pricesList;
	}

	/**
	 * @param pricesList the pricesList to set
	 */
	public void setPricesList(List<Integer> pricesList) {
		this.pricesList = pricesList;
	}

	/**
	 * @return the articles
	 */
	public List<SEArticle> getArticles() {
		return articles;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setArticles(List<SEArticle> articles) {
		this.articles = articles;
	}

	/**
	 * @return the selectedArticle
	 */
	public SEArticle getSelectedArticle() {
		return selectedArticle;
	}

	/**
	 * @param selectedArticle the selectedArticle to set
	 */
	public void setSelectedArticle(SEArticle selectedArticle) {
		this.selectedArticle = selectedArticle;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ValueHolder> getSelectedArticlePrices() {
		if (selectedArticlePrices == null) {
			selectedArticlePrices = new ArrayList<ValueHolder>();
			if (getSelectedArticle().getPrices() != null) {
				for (Double price : getSelectedArticle().getPrices()) {
					selectedArticlePrices.add(new ValueHolder(price));
				}
			}
		}
		return selectedArticlePrices;
	}

	/**
	 * @param selectedArticlePrices the selectedArticlePrices to set
	 */
	public void setSelectedArticlePrices(
			List<ValueHolder> selectedArticlePrices) {
		this.selectedArticlePrices = selectedArticlePrices;
	}

	/**
	 * @return the promoArticlesToAdd
	 */
	public List<SEArticle> getPromoArticlesToAdd() {
		return promoArticlesToAdd;
	}

	/**
	 * @param promoArticlesToAdd the promoArticlesToAdd to set
	 */
	public void setPromoArticlesToAdd(List<SEArticle> promoArticlesToAdd) {
		this.promoArticlesToAdd = promoArticlesToAdd;
	}

	/**
	 * @return the selectedPromotionArticles
	 */
	public Collection<PromotionItem> getSelectedPromotionItems() {
		return selectedPromotionItems;
	}

	/**
	 * @param selectedPromotionArticles the selectedPromotionArticles to set
	 */
	public void setSelectedPromotionItems(
			Collection<PromotionItem> selectedPromotionItems) {
		this.selectedPromotionItems = selectedPromotionItems;
	}

	/**
	 * 
	 */
	public void clear() {
		setSelectedArticlePrices(null);
		setSelectedPromotionItems(null);
	}
	
	/**
	 * 
	 */
	public void clearSelectedValues() {
		setSelectedArticle(null);
		setSelectedCustomer(null);
		setSelectedUser(null);
		clear();
	}

	/**
	 * @return the selectedStock
	 */
	public Stock getSelectedStock() {
		return selectedStock;
	}

	/**
	 * @param selectedStock the selectedStock to set
	 */
	public void setSelectedStock(Stock selectedStock) {
		this.selectedStock = selectedStock;
	}
}
