/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.ObjectStreamException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import papasoft.octopus.domain.Customer;
import papasoft.octopus.domain.SalesUser;

/**
 * @author maqui
 *
 */
public class SESalesUser extends SalesUser {

	private static final long serialVersionUID = 2471906447745864858L;
	private Collection<SalesUserCustomer> customers;

	/**
	 * @return the customers
	 */
	public Collection<SalesUserCustomer> getCustomers() {
		return customers;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(Collection<SalesUserCustomer> customers) {
		this.customers = customers;
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	public Boolean canSellToCustomerToday(Customer customer) {
		if (getCustomers() != null) {
			for (SalesUserCustomer userCust : getCustomers()) {
				if (userCust.getCustomer().getId().equals(customer.getId())) {
					return canSellToCustomerToday(userCust);
				}
			}
		}
		return null;
	}

	/**
	 * @param userCust
	 * @return
	 */
	private Boolean canSellToCustomerToday(SalesUserCustomer userCust) {
		if (userCust.getIntDaysToVisit()[getDayOfWeek()] > 0)
			return true;
		return false;
	}
	
	/**
	 * @return
	 */
	private int getDayOfWeek() {
		int dayOfWeek = new GregorianCalendar(TimeZone.getTimeZone("GMT-3:00")).get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
			case Calendar.MONDAY : return 0;
			case Calendar.TUESDAY : return 1;
			case Calendar.WEDNESDAY : return 2;
			case Calendar.THURSDAY : return 3;
			case Calendar.FRIDAY : return 4;
			case Calendar.SATURDAY : return 5;
			case Calendar.SUNDAY : return 6;
			default : return 0;
		}
	}

	/**
	 * 
	 */
	private SalesUser buildSalesUser() {
		SalesUser user = new SalesUser();
		user.setId(getId());
		user.setName(getName());
		user.setPassword(getPassword());
		user.setStatus(getStatus());
		user.setCanCreateCreditNotes(getCanCreateCreditNotes());		
		return user;
	}
	
	/**
	 * 
	 * @return
	 * @throws ObjectStreamException
	 */
	private Object writeReplace() throws ObjectStreamException {
		return buildSalesUser();
	}
}
