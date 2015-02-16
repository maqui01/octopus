/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Date;

import papasoft.octopus.domain.Promotion;
import papasoft.octopus.domain.PromotionItem;

/**
 * @author maqui
 *
 */
public class SEPromotion extends SEArticle {
	
	private static final long serialVersionUID = 738449174292099442L;

	private Collection<PromotionItem> items;

	private String customerGroup;
	
	private Date startDate;
	
	private Date endDate;

	/**
	 * @return the articles
	 */
	public Collection<PromotionItem> getItems() {
		return items;
	}

	/**
	 * @param articles the articles to set
	 */
	public void setItems(Collection<PromotionItem> items) {
		this.items = items;
	}
	
	/* (non-Javadoc)
	 * @see papasoft.octopus.domain.Article#toString()
	 */
	@Override
	public String toString() {
		return getCode() + " - " + getName() + " - PromotionSize: " + getItems().size();
	}

	/**
	 * @param customerGroup
	 */
	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	/**
	 * @return the customerGroup
	 */
	public String getCustomerGroup() {
		return customerGroup;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 
	 * @return
	 */
	private Promotion buildPromotion() {
		return new Promotion(getCode(),
				getName(),
				getPrices(),
				getPack(),
				getItems(),
				getCustomerGroup(),
				getStartDate(),
				getEndDate());
	}
	
	/**
	 * 
	 * @return
	 * @throws ObjectStreamException
	 */
	private Object writeReplace() throws ObjectStreamException {
		return buildPromotion();
	}
}
