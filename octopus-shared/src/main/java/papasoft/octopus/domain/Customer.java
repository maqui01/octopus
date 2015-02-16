/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;


/**
 * @author De La
 *
 */
public class Customer implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 5144888437968684296L;

	private Long id;
	
	private String name;
	
	private Integer pricesList;
	
	private String group;
	
	private String observation;
	
	private String email;
	
	private String address;
	
	private String phoneNumber;

	/**
	 * 
	 */
	public Customer() {
		super();
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param pricesList
	 * @param group
	 * @param observation
	 * @param email
	 * @param address
	 * @param phoneNumber
	 */
	public Customer(Long id, String name, Integer pricesList, String group,
			String observation, String email, String address, String phoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.pricesList = pricesList;
		this.group = group;
		this.observation = observation;
		this.email = email;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return getId() + " - " + getName();
	}

	/**
	 * @return the pricesList
	 */
	public Integer getPricesList() {
		return pricesList;
	}

	/**
	 * @param pricesList the pricesList to set
	 */
	public void setPricesList(Integer pricesList) {
		this.pricesList = pricesList;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}

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
		if (!(obj instanceof Customer))
			return false;
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
