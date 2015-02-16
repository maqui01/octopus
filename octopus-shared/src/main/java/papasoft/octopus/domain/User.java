/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import papasoft.octopus.domain.se.Company;


/**
 * @author De La
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = -845895027419396279L;

	public static final String GUEST_USER_NAME = "test";
	public static final String GUEST_USER_PASSWORD = "test";

	public static final Integer STATUS_NORMAL = 0;
	public static final Integer STATUS_BLOCKED = 1;
	
	private Long id;

	private String name;
	
	private String password;
	
	private Integer status = STATUS_NORMAL;
	
	private Company company;
	
	private Collection<Role> roles = new ArrayList<Role>();

	/**
	 * 
	 */
	public User() { }
	
	/**
	 * 
	 */
	public User(Long id) {
		this.id = id;
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

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the roles
	 */
	public Collection<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
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
	
}
