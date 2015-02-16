/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.Serializable;

/**
 * @author maqui
 *
 */
public class SalesUserCustomer implements Serializable {

	private static final long serialVersionUID = 6424306893724466108L;

	private static final Integer[] DEFAULT_VISIT_DAYS = new Integer[] {1,1,1,1,1,1,1};
	
	private SECustomer customer;
	
	private SESalesUser salesUser;
	
	private String daysToVisit;

	/**
	 * @return the customer
	 */
	public SECustomer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(SECustomer customer) {
		this.customer = customer;
	}

	/**
	 * @return the daysToVisit
	 */
	public String getDaysToVisit() {
		return daysToVisit;
	}

	/**
	 * @param daysToVisit the daysToVisit to set
	 */
	public void setDaysToVisit(String daysToVisit) {
		this.daysToVisit = daysToVisit;
	}

	/**
	 * @return the salesUser
	 */
	public SESalesUser getSalesUser() {
		return salesUser;
	}

	/**
	 * @param salesUser the salesUser to set
	 */
	public void setSalesUser(SESalesUser salesUser) {
		this.salesUser = salesUser;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer[] getIntDaysToVisit() {
		if (daysToVisit == null || daysToVisit.length() < 13) {
			return DEFAULT_VISIT_DAYS;
		}
		try {
			String[] strArr = daysToVisit.split(",");
			if (strArr.length != 7) {
				return DEFAULT_VISIT_DAYS;
			}
			Integer[] intArr = new Integer[7];
			for (int i = 0; i < 7; i++) {
				intArr[i] = Integer.parseInt(strArr[i]);
			}
			return intArr;
		} catch (Throwable th) {
			return DEFAULT_VISIT_DAYS;
		}
	}
}
