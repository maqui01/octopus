/**
 * 
 */
package papasoft.octopus.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author De La
 *
 */
public class Promotion extends Article {

	private static final long serialVersionUID = 1652760539005085709L;
	
	private Collection<PromotionItem> items;

	private String customerGroup;
	
	private Date startDate;
	
	private Date endDate;
	
	/**
	 * 
	 */
	public Promotion() {
		super();
	}

	/**
	 * 
	 * @param code
	 * @param name
	 * @param prices
	 * @param pack
	 * @param items
	 * @param customerGroup
	 * @param startDate
	 * @param endDate
	 */
	public Promotion(Long code, String name, List<Double> prices, Double pack,
			Collection<PromotionItem> items, String customerGroup,
			Date startDate, Date endDate) {
		super(code, name, prices, pack);
		this.items = items;
		this.customerGroup = customerGroup;
		this.startDate = startDate;
		this.endDate = endDate;
	}

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
}
