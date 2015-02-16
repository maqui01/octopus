/**
 * 
 */
package papasoft.octopus.domain;

import java.io.Serializable;

/**
 * @author De La
 *
 */
public class Item implements Serializable {
	
	private static final long serialVersionUID = -2932648548708337915L;
	
	public static final int STATUS_WAITING_FOR_CONFIRMATION = 0;
	public static final int STATUS_WAITING_FOR_CANCELATION = 1;
	public static final int STATUS_CONFIRMED = 2;
	public static final int STATUS_CANCELED = 3;
	public static final int STATUS_CANCELED_FROM_SERVER = 4;
	
	private Long id;
	
	private Article article;
	
	private Double quantity;
	
	private Double price;
	
	private Integer status = STATUS_WAITING_FOR_CONFIRMATION;
	
	private String statusDescription;
	
	private Boolean toCredit;
	
	/**
	 * 
	 */
	public Item() {}
	
	
	/**
	 * 
	 * @param article
	 * @param quantity
	 */
	public Item(Article article, Double quantity) {
		super();
		this.article = article;
		this.quantity = quantity;
	}



	/**
	 * @return the article
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article the article to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}

	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
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


	/**
	 * @return the toCredit
	 */
	public Boolean getToCredit() {
		return toCredit;
	}


	/**
	 * @param toCredit the toCredit to set
	 */
	public void setToCredit(Boolean toCredit) {
		this.toCredit = toCredit;
	}
	

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}


	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getTotalAmount() {
		Double amount = getPrice() * getQuantity();
		return (getToCredit() ? -1.0 : 1.0) * (amount * 100.0) / 100.0;
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
		if (!(obj instanceof Item))
			return false;
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
