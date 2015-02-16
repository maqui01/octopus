/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.ObjectStreamException;

import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.se.Company;

/**
 * @author maqui
 *
 */
public class SEArticle extends Article implements Cloneable {

	private static final long serialVersionUID = -7455356902235659178L;

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
	private Article buildArticle() {
		return new Article(getCode(), getName(), getPrices(), getPack());
	}
	
	/**
	 * 
	 * @return
	 * @throws ObjectStreamException
	 */
	private Object writeReplace() throws ObjectStreamException {
		return buildArticle();
	}
}
