/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.hibernate.Hibernate;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;

import papasoft.octopus.domain.PromotionItem;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.se.domain.SEArticle;
import papasoft.octopus.se.domain.SEPromotion;
import papasoft.octopus.se.domain.Stock;
import papasoft.octopus.webapp.context.ApplicationContext;
import papasoft.octopus.webapp.utils.MessageUtils;
import papasoft.octopus.webapp.utils.ValueHolder;

/**
 * @author maqui
 *
 */
@ManagedBean
@RequestScoped
public class CrudArticleBean extends AbstractBean {

	private static final double DEFAULT_INITIAL_STOCK = 1000.0;
	private static final int MAX_PRICES_COUNT = 10;
	private static final int ORDERS_TABLE_ROWS_PER_PAGE = 10;
	private String articleName;
	private Long articleCode;
	private DataTable articlesDatatable;
	private DataTable promoArticlesDatatable;
	private DataTable promoArticlesDialogDatatable;
	private DataTable promoArticlesToAddDatatable;
	private Long promoArticleCode;
	private String promoArticleName;
	private Double promoPrice;
	
	
	public void searchArticles() throws OctopusException {
		setArticles(retrieveArticles(getArticleCode(), getArticleName()));
		clearArticle();
	}
	
	/**
	 * 
	 */
	private void clearArticle() {
		CrudContext.getCtx().setSelectedArticle(null);
		
		getArticlesDatatable().setRows(ORDERS_TABLE_ROWS_PER_PAGE);
		getArticlesDatatable().setFirst( getArticlesDatatable().getFirst() > ORDERS_TABLE_ROWS_PER_PAGE 
				? getArticlesDatatable().getFirst()-ORDERS_TABLE_ROWS_PER_PAGE
				: 0 );
	}

	/**
	 * 
	 * @return
	 * @throws OctopusException
	 */
	public List<SEArticle> getArticles() throws OctopusException {
		List<SEArticle> articles = CrudContext.getCtx().getArticles();
		if (articles == null) {
			articles = retrieveArticles(getArticleCode(), getArticleName());
			setArticles(articles);
		}
		return articles;
	}
	
	/**
	 * @param name 
	 * @param code 
	 * @return
	 * @throws OctopusException 
	 */
	private List<SEArticle> retrieveArticles(Long code, String name) throws OctopusException {
		if (LoginBean.getLoggedUser() != null) {
			return ApplicationContext.getCtx().getSalesDao().retrieveArticles(LoginBean.getLoggedUser().getCompany().getId(), code, name, true);
		}
		return null;
	}

	/**
	 * 
	 * @param articles
	 */
	public void setArticles(List<SEArticle> articles) {
		CrudContext.getCtx().setArticles(articles);
	}

	/**
	 * @return the articleName
	 */
	public String getArticleName() {
		return articleName;
	}

	/**
	 * @param articleName the articleName to set
	 */
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	/**
	 * @return the articlesDatatable
	 */
	public DataTable getArticlesDatatable() {
		return articlesDatatable;
	}

	/**
	 * @param articlesDatatable the articlesDatatable to set
	 */
	public void setArticlesDatatable(DataTable articlesDatatable) {
		this.articlesDatatable = articlesDatatable;
	}
	
	/**
	 * 
	 * @param evt
	 * @throws OctopusException 
	 */
	public void articleSelect(SelectEvent evt) throws OctopusException {
		if (!Hibernate.isInitialized(CrudContext.getCtx().getSelectedArticle().getPrices())) {
			ApplicationContext.getCtx().getSalesDao().initialize(CrudContext.getCtx().getSelectedArticle(), "prices");
		}
		updateDatatableRowsQty(getArticles().indexOf(CrudContext.getCtx().getSelectedArticle()));
		Stock stock = ApplicationContext.getCtx().getSalesDao().getStock(CrudContext.getCtx().getSelectedArticle());
		if (stock == null) {
			stock = new Stock(DEFAULT_INITIAL_STOCK);
			stock.setArticle(CrudContext.getCtx().getSelectedArticle());
		}
		CrudContext.getCtx().setSelectedStock(stock);
		refreshArticlePrices();
	}

	/**
	 * @throws OctopusException
	 */
	private void updateDatatableRowsQty(Integer selectedArticleIndex) {
		Integer rowsPerPage = ORDERS_TABLE_ROWS_PER_PAGE/2;
		getArticlesDatatable().setRows(rowsPerPage);
		
		// Veo de estar en la página correcta:
		Integer indexSelected = selectedArticleIndex;
		Integer currentPage = indexSelected / rowsPerPage;
		getArticlesDatatable().setFirst(currentPage * rowsPerPage);
	}

	/**
	 * 
	 */
	private void refreshArticlePrices() {
		CrudContext.getCtx().setSelectedArticlePrices(null);
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getStartDate() {
		if (getArticlesDatatable().isRowAvailable()) {
			SEArticle article = (SEArticle) getArticlesDatatable().getRowData();
			if (article instanceof SEPromotion) {
				return ((SEPromotion)article).getStartDate();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Date getEndDate() {
		if (getArticlesDatatable().isRowAvailable()) {
			SEArticle article = (SEArticle) getArticlesDatatable().getRowData();
			if (article instanceof SEPromotion) {
				return ((SEPromotion)article).getEndDate();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCustomerGroup() {
		if (getArticlesDatatable().isRowAvailable()) {
			SEArticle article = (SEArticle) getArticlesDatatable().getRowData();
			if (article instanceof SEPromotion) {
				return ((SEPromotion)article).getCustomerGroup();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getSelectedArticleIsPromotion() {
		if (CrudContext.getCtx().getSelectedArticle() != null) {
			return CrudContext.getCtx().getSelectedArticle() instanceof SEPromotion;
		}
		return false;
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void newArticle() {
		CrudContext.getCtx().setSelectedArticle(new SEArticle());
		CrudContext.getCtx().setSelectedStock(new Stock(DEFAULT_INITIAL_STOCK));
		List<Double> emptyPrices = new ArrayList<Double>();
		emptyPrices.add(0.0);
		CrudContext.getCtx().getSelectedArticle().setPrices(emptyPrices);
		CrudContext.getCtx().clear();
		updateDatatableRowsQty(0);
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void newPromotion() {
		CrudContext.getCtx().setSelectedArticle(new SEPromotion());
		CrudContext.getCtx().setSelectedStock(new Stock(DEFAULT_INITIAL_STOCK));
		CrudContext.getCtx().clear();
		updateDatatableRowsQty(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getArticlePanelTitle() {
		if (CrudContext.getCtx().getSelectedArticle() != null) {
			if (CrudContext.getCtx().getSelectedArticle().getCode() != null) {
				if (CrudContext.getCtx().getSelectedArticle().getClass().isAssignableFrom(SEArticle.class)) {
					return MessageUtils.getMessage("edit_article");
				} else {
					return MessageUtils.getMessage("edit_promotion");
				}
			} else {
				if (CrudContext.getCtx().getSelectedArticle().getClass().isAssignableFrom(SEArticle.class)) {
					return MessageUtils.getMessage("new_article");
				} else {
					return MessageUtils.getMessage("new_promotion");
				}
			}
		}
		return "";
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void deleteArticle() throws OctopusException {
		if (CrudContext.getCtx().getSelectedArticle() != null && CrudContext.getCtx().getSelectedArticle().getCode() != null) {
			getSalesDao().delete(CrudContext.getCtx().getSelectedArticle());
			searchArticles();
		}
		clearArticle();
	}
	
	/**
	 * 
	 */
	public void cancelArticleEdit() {
		clearArticle();
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void saveSelectedArticle() throws OctopusException {
		SEArticle article = CrudContext.getCtx().getSelectedArticle();
		if (article.getClass().isAssignableFrom(SEArticle.class)) {
			article.setPrices(buildSelectedArticlePrices());	
		}
		article.setCompanyId(LoginBean.getLoggedUser().getCompany().getId());
		getSalesDao().saveOrUpdate(article);
		Stock stock = CrudContext.getCtx().getSelectedStock();
		stock.setArticle(article);
		getSalesDao().saveOrUpdate(stock);
		clearArticle();
		searchArticles();
	}

	/**
	 * @return
	 */
	private List<Double> buildSelectedArticlePrices() {
		List<ValueHolder> values = CrudContext.getCtx().getSelectedArticlePrices();
		List<Double> prices = new ArrayList<Double>();
		for (ValueHolder holder : values) {
			prices.add(Double.valueOf(holder.getValue()));
		}
		return prices;
	}

	/**
	 * 
	 */
	public void addNewPrice() {
		List<ValueHolder> prices = CrudContext.getCtx().getSelectedArticlePrices();
		if (prices == null) {
			prices = new ArrayList<ValueHolder>();
		}
		if (prices.size() < MAX_PRICES_COUNT) 
			prices.add(new ValueHolder(0.0));	
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getCanAddNewPrice() {
		SEArticle article = CrudContext.getCtx().getSelectedArticle();
		if (article != null && (article.getPrices() == null || article.getPrices().size() < MAX_PRICES_COUNT)) {
			return true;
		}
		return false;		
	}
	
	/**
	 * 
	 */
	public void deleteLastPrice() {
		List<ValueHolder> prices = CrudContext.getCtx().getSelectedArticlePrices();
		if (prices.size() > 1) 
			prices.remove(prices.size() - 1);
	}

	/**
	 * @return the promoArticlesDatatable
	 */
	public DataTable getPromoArticlesDatatable() {
		return promoArticlesDatatable;
	}

	/**
	 * @param promoArticlesDatatable the promoArticlesDatatable to set
	 */
	public void setPromoArticlesDatatable(DataTable promoArticlesDatatable) {
		this.promoArticlesDatatable = promoArticlesDatatable;
	}
	
	/**
	 * 
	 */
	public void deletePromoArticle() {
		if (getPromoArticlesDatatable().isRowAvailable()) {
			PromotionItem item = (PromotionItem) getPromoArticlesDatatable().getRowData();
			deletePromoArticle(item);
		}
	}

	/**
	 * @param item
	 */
	private void deletePromoArticle(PromotionItem item) {
		SEPromotion promo = (SEPromotion) CrudContext.getCtx().getSelectedArticle();
		promo.getItems().remove(item);
	}

	/**
	 * @return the promoArticlesDialogDatatable
	 */
	public DataTable getPromoArticlesDialogDatatable() {
		return promoArticlesDialogDatatable;
	}

	/**
	 * @param promoArticlesDialogDatatable the promoArticlesDialogDatatable to set
	 */
	public void setPromoArticlesDialogDatatable(
			DataTable promoArticlesDialogDatatable) {
		this.promoArticlesDialogDatatable = promoArticlesDialogDatatable;
	}

	/**
	 * @return the promoArticleCode
	 */
	public Long getPromoArticleCode() {
		return promoArticleCode;
	}

	/**
	 * @param promoArticleCode the promoArticleCode to set
	 */
	public void setPromoArticleCode(Long promoArticleCode) {
		this.promoArticleCode = promoArticleCode;
	}

	/**
	 * @return the promoArticleName
	 */
	public String getPromoArticleName() {
		return promoArticleName;
	}

	/**
	 * @param promoArticleName the promoArticleName to set
	 */
	public void setPromoArticleName(String promoArticleName) {
		this.promoArticleName = promoArticleName;
	}
	
	/**
	 * 
	 */
	public void deletePromoArticleDialog() {
		if (getPromoArticlesDialogDatatable().isRowAvailable()) {
			PromotionItem item = (PromotionItem) getPromoArticlesDialogDatatable().getRowData();
			deletePromoArticle(item);
		}
	}

	/**
	 * @return the promoArticlesToAddDatatable
	 */
	public DataTable getPromoArticlesToAddDatatable() {
		return promoArticlesToAddDatatable;
	}

	/**
	 * @param promoArticlesToAddDatatable the promoArticlesToAddDatatable to set
	 */
	public void setPromoArticlesToAddDatatable(DataTable promoArticlesToAddDatatable) {
		this.promoArticlesToAddDatatable = promoArticlesToAddDatatable;
	}
	
	/**
	 * 
	 * @return
	 * @throws OctopusException 
	 */
	public List<SEArticle> getPromoArticlesToAdd() throws OctopusException {
		if (CrudContext.getCtx().getPromoArticlesToAdd() == null) {
			CrudContext.getCtx().setPromoArticlesToAdd(retrievePromoArticlesToAdd(getPromoArticleCode(), getPromoArticleName()));
		}
		return CrudContext.getCtx().getPromoArticlesToAdd();
	}

	/**
	 * @param promoArticleCode
	 * @param promoArticleName
	 * @return
	 * @throws OctopusException 
	 */
	private List<SEArticle> retrievePromoArticlesToAdd(
			Long promoArticleCode, String promoArticleName) throws OctopusException {
		return ApplicationContext.getCtx().getSalesDao().retrieveArticles(LoginBean.getLoggedUser().getCompany().getId(), promoArticleCode, promoArticleName, false);
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void searchPromoArticles() throws OctopusException {
		CrudContext.getCtx().setPromoArticlesToAdd(retrievePromoArticlesToAdd(getPromoArticleCode(), getPromoArticleName()));
	}
	
	/**
	 * 
	 */
	public void addArticleToPromotion() {
		if (getPromoArticlesToAddDatatable().isRowAvailable()) {
			SEArticle art = (SEArticle) getPromoArticlesToAddDatatable().getRowData();
			addItemToPromotion(art, 1.0);
		}
	}
	
	/**
	 * 
	 * @param article
	 * @param quantity
	 */
	public void addItemToPromotion(SEArticle article, Double quantity) {
		if (CrudContext.getCtx().getSelectedPromotionItems() == null) {
			CrudContext.getCtx().setSelectedPromotionItems(new ArrayList<PromotionItem>());
		}
		Boolean added = false;
		for (PromotionItem item : CrudContext.getCtx().getSelectedPromotionItems()) {
			if (item.getArticle().equals(article)) {
				item.setQuantity(item.getQuantity() +1);
				added = true;
			}
		}
		if (!added) {
			PromotionItem item = new PromotionItem();
			item.setArticle(article);
			item.setQuantity(quantity);
			CrudContext.getCtx().getSelectedPromotionItems().add(item);
		}
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void refreshPromoArticlesDialog() throws OctopusException {
		setPromoArticleCode(null);
		setPromoArticleName(null);
		if (((SEPromotion)CrudContext.getCtx().getSelectedArticle()).getItems() != null) {
			CrudContext.getCtx().setSelectedPromotionItems(((SEPromotion)CrudContext.getCtx().getSelectedArticle()).getItems());
		}
		searchPromoArticles();
	}
	
	/**
	 * 
	 */
	public void savePromotionArticles() {
		((SEPromotion)CrudContext.getCtx().getSelectedArticle()).setItems(CrudContext.getCtx().getSelectedPromotionItems());
	}

	/**
	 * @return the promoPrice
	 */
	public Double getPromoPrice() {
		return promoPrice;
	}

	/**
	 * @param promoPrice the promoPrice to set
	 */
	public void setPromoPrice(Double promoPrice) {
		this.promoPrice = promoPrice;
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
}
