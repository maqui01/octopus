/**
 * 
 */
package papasoft.octopus.domain.se;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import papasoft.octopus.domain.User;

/**
 * @author maqui
 *
 */
public class Company {
	
	public static final String GUEST_COMPANY_NAME = "test-company";
	
	public static final int STARTING_ORDERS_QUANTITY = 500;

	private Long id;
	
	private String name;
	
	private String email;
	
	private Collection<User> users = new ArrayList<User>();
	
	/* La cantidad de órdenes de la cual dispone la empresa  */
	private Integer remainingOrders = STARTING_ORDERS_QUANTITY;
	
	/* Si es true significa que es una company para Guest-User */
	private Boolean testCompany = false;
	
	private Date creationDate;

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
	 * @return the users
	 */
	public Collection<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Collection<User> users) {
		this.users = users;
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
	 * @return the remainingOrders
	 */
	public Integer getRemainingOrders() {
		return remainingOrders;
	}

	/**
	 * @param remainingOrders the remainingOrders to set
	 */
	public void setRemainingOrders(Integer remainingOrders) {
		this.remainingOrders = remainingOrders;
	}

	/**
	 * @return the testCompany
	 */
	public Boolean getTestCompany() {
		return testCompany;
	}

	/**
	 * @param testCompany the testCompany to set
	 */
	public void setTestCompany(Boolean testCompany) {
		this.testCompany = testCompany;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	} 
}
