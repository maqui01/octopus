/**
 * 
 */
package papasoft.octopus.se.domain;

import java.util.ArrayList;
import java.util.Collection;

import papasoft.octopus.domain.Article;
import papasoft.octopus.domain.Promotion;
import papasoft.octopus.domain.PromotionItem;

/**
 * @author maqui
 *
 */
public class SEItem {
	
	private Long id;
	private String name;
	private Double price;
	private Double quantity;
	private Double pack;
	private Boolean toCredit;
	private Collection<SEItem> promotionItems;
	private Long articleCode;

	public SEItem () {
		
	}
	
	/**
	 * 
	 * @param item
	 */
	public SEItem (Article article, Double itemQuantity, Integer pricesList) {
		this.name = article.getName();
		this.pack = article.getPack();
		this.articleCode = article.getCode();
		if (Promotion.class.isInstance(article)) {
			this.price = article.getPrices().get(0);
		} else {
			this.price = article.getPrices().get(pricesList);
		}
		this.quantity = Math.abs(itemQuantity);
		this.toCredit = itemQuantity < 0.0;
		if (Promotion.class.isInstance(article)) {
			Promotion promo = (Promotion)article;
			promotionItems = new ArrayList<SEItem>();
			for (PromotionItem promoItem : promo.getItems()) {
				SEItem promoItemBackup = new SEItem(promoItem.getArticle(), promoItem.getQuantity(), pricesList);
				promotionItems.add(promoItemBackup);
			}
		}
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
	 * @return the promotionItems
	 */
	public Collection<SEItem> getPromotionItems() {
		return promotionItems;
	}

	/**
	 * @param promotionItems the promotionItems to set
	 */
	public void setPromotionItems(Collection<SEItem> promotionItems) {
		this.promotionItems = promotionItems;
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
	 * @return the articleCode
	 */
	public Long getArticleCode() {
		return articleCode;
	}

	/**
	 * @param articleCode the articleCode to set
	 */
	public void setArticleCode(Long articleCode) {
		this.articleCode = articleCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public Double getTotalAmount() {
		Double amount = getPrice() * getQuantity();
		return (getToCredit() ? -1.0 : 1.0) * (amount * 100.0) / 100.0;
	}
}
