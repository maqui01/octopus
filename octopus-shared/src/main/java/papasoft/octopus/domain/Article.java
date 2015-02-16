/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author De La
 *
 */
public class Article implements Serializable {
	
	private static final long serialVersionUID = 4462254938643412510L;
	
	private Long code;
	
	private String name;
	
	private List<Double> prices;
	
	private Double pack;
	
	/**
	 * 
	 */
	public Article() {
		super();
	}

	/**
	 * 
	 * @param code
	 * @param name
	 * @param prices
	 * @param pack
	 */
	public Article(Long code, String name, List<Double> prices, Double pack) {
		super();
		this.code = code;
		this.name = name;
		this.prices = prices;
		this.pack = pack;
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
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Article))
			return false;
		Article article = (Article) obj;
		if ( getCode() != null && article.getCode() != null && getCode().equals(article.getCode()) )
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getPrice() {
		if (getPrices() == null || getPrices().isEmpty())
			return 0.0;
		else
			return getPrices().get(0);
	}
	
	/**
	 * 
	 */
	public void setPrice(Double price) {
		if (getPrices() == null)
			setPrices(new ArrayList<Double>());
		getPrices().clear();
		getPrices().add(price);
	}

	/**
	 * @return the prices
	 */
	public List<Double> getPrices() {
		return prices;
	}

	/**
	 * @param prices the prices to set
	 */
	public void setPrices(List<Double> prices) {
		this.prices = prices;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getUnitPrice(Customer customer) {
		if (customer == null) {
			return this.prices.get(0);
		}
		return this.prices.get(customer.getPricesList());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getCode() + " - " + getName();
	}

	/**
	 * @return the pack
	 */
	public Double getPack() {
		return pack;
	}

	/**
	 * @param pack the pack to set
	 */
	public void setPack(Double pack) {
		this.pack = pack;
	}

	/**
	 * @return the code
	 */
	public Long getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Long code) {
		this.code = code;
	}

}
