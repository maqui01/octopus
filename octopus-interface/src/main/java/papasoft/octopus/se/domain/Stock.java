/**
 * 
 */
package papasoft.octopus.se.domain;

import java.io.Serializable;

import papasoft.octopus.domain.Article;

/**
 * @author maqui
 *
 */
public class Stock implements Serializable {

	private static final long serialVersionUID = -5562712759722199349L;

	private Article article;
	
	private Double stock;
	
	public Stock () {
		
	}
	
	public Stock (Double stock) {
		this.stock = stock;
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
	 * @return the stock
	 */
	public Double getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Double stock) {
		this.stock = stock;
	}
}
