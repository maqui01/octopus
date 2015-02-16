/**
 * 
 */
package papasoft.octopus.se.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.Order;
import papasoft.octopus.domain.se.Company;

/**
 * @author maqui
 *
 */
public class SEOrder extends Order {

	private static final long serialVersionUID = 6349518983123062793L;
	
	private Long companyId;
	
	private Company company;
	
	private Collection<SEItem> seItems;

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	/**
	 * @param article
	 * @param itemQuantity
	 * @param pricesList
	 */
	public void addArticle(Article article, Double itemQuantity, Integer pricesList) {
		SEItem seItem = new SEItem(article, itemQuantity, pricesList);
		if (getSeItems() == null) {
			setSeItems(new ArrayList<SEItem>());
		}
		this.getSeItems().add(seItem);
	}

	/**
	 * @param article
	 * @param articleQuantity
	 */
	public void removeArticle(Article article, Double articleQuantity) {
		Iterator<SEItem> it = getSeItems().iterator();
		while (it.hasNext()) {
			SEItem item = it.next();
			if (item.getArticleCode().equals(article.getCode()) && item.getQuantity().equals(articleQuantity)) {
				it.remove();
				return;
			}
		}
	}
	

	/**
	 * 
	 * @return
	 */
	public Double getTotalAmount() {
		Double amount = 0.0;
		for (SEItem item : getSeItems()) {
			if (item.getPrice() != null) {
				amount += item.getTotalAmount();
			}
		}
		return Math.round(amount * 100.0) / 100.0;
	}

	/**
	 * @return the seItems
	 */
	public Collection<SEItem> getSeItems() {
		return seItems;
	}

	/**
	 * @param seItems the seItems to set
	 */
	public void setSeItems(Collection<SEItem> seItems) {
		this.seItems = seItems;
	}
}
