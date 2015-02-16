/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author De La
 *
 */
public class Order implements Serializable {
	
	private static final long serialVersionUID = -5389649678748471158L;
	
	public static final int STATUS_ON_EDITION = 0;
	public static final int STATUS_WAITING_FOR_CONFIRMATION = 1;
	public static final int STATUS_CONTAIN_ITEMS_ON_CONFLICT = 2;
	public static final int STATUS_WAITING_FOR_CANCELATION = 3;
	public static final int STATUS_CONFIRMED = 4;
	public static final int STATUS_CANCELED = 5;
	public static final int STATUS_CANCELED_FROM_SERVER = 6;
	
	private Long id;
	
	private User user;
	
	private Customer customer;
	
	private Date date;
	
	private Collection<Item> items;
	
	private Integer status = STATUS_ON_EDITION;
	
	private String statusDescription;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the items
	 */
	public Collection<Item> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(Collection<Item> items) {
		this.items = items;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}

	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
