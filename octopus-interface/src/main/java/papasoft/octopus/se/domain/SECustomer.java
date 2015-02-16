/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.ObjectStreamException;

import papasoft.octopus.domain.Customer;
import papasoft.octopus.domain.se.Company;

/**
 * @author maqui
 *
 */
public class SECustomer extends Customer {

	private static final long serialVersionUID = 4032134685377140385L;
	
	private Long companyId;
	
	private Company company;

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
		if (this.company != null) {
			setCompanyId(this.company.getId());
		}
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
	 * 
	 * @return
	 */
	private Customer buildCustomer() {
		return new Customer(getId(),
				getName(),
				getPricesList(),
				getGroup(),
				getObservation(),
				getEmail(),
				getAddress(),
				getPhoneNumber());
	}
	
	/**
	 * 
	 * @return
	 * @throws ObjectStreamException
	 */
	private Object writeReplace() throws ObjectStreamException {
		return buildCustomer();
	}

}
