/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;

import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.se.domain.SECustomer;
import papasoft.octopus.webapp.context.ApplicationContext;
import papasoft.octopus.webapp.utils.MessageUtils;

/**
 * @author maqui
 *
 */
@ManagedBean
@RequestScoped
public class CrudCustomerBean extends AbstractBean {

	private static final int ORDERS_TABLE_ROWS_PER_PAGE = 10;
	private static final String EMPTY_GROUP = "";
	private String customerName;
	private DataTable customersDatatable;

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * 
	 * @param evt
	 * @throws OctopusException 
	 */
	public void searchCustomers() throws OctopusException {
		setCustomers(retrieveCustomers());
		clearCustomer();
	}

	/**
	 * 
	 */
	private void clearCustomer() {
		CrudContext.getCtx().setSelectedCustomer(null);
	}

	/**
	 * @return the customersDatatable
	 */
	public DataTable getCustomersDatatable() {
		return customersDatatable;
	}

	/**
	 * @param customersDatatable the customersDatatable to set
	 */
	public void setCustomersDatatable(DataTable customersDatatable) {
		this.customersDatatable = customersDatatable;
	}
	
	/**
	 * @return the users
	 * @throws OctopusException 
	 */
	public List<SECustomer> getCustomers() throws OctopusException {
		List<SECustomer> customers = CrudContext.getCtx().getCustomers();
		if (customers == null) {
			customers = retrieveCustomers();
			CrudContext.getCtx().setCustomers(customers);
		}
		return customers;
	}
	
	/**
	 * @param users the users to set
	 */
	public void setCustomers(List<SECustomer> customers) {
		CrudContext.getCtx().setCustomers(customers);
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	private List<SECustomer> retrieveCustomers() throws OctopusException {
		if (LoginBean.getLoggedUser() != null) {
			return getSalesDao().retrieveAllCustomers(getCustomerName(), LoginBean.getLoggedUser().getCompany().getName());
		}
		return null;
	}
	
	/**
	 * 
	 * @param evt
	 * @throws OctopusException 
	 */
	public void customerSelect(SelectEvent evt) throws OctopusException {

		Integer rowsPerPage = ORDERS_TABLE_ROWS_PER_PAGE/2;
		getCustomersDatatable().setRows(rowsPerPage);
		
		// Veo de estar en la página correcta:
		Integer indexSelected = getCustomers().indexOf(CrudContext.getCtx().getSelectedCustomer());
		Integer currentPage = indexSelected / rowsPerPage;
		//if ( indexSelected % rowsPerPage > 0 ) currentPage++;
		getCustomersDatatable().setFirst(currentPage * rowsPerPage);
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void newCustomer() throws OctopusException {
		SECustomer newCustomer = new SECustomer();
		newCustomer.setCompany(LoginBean.getLoggedUser().getCompany());
		CrudContext.getCtx().setSelectedCustomer(newCustomer);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCustomerPanelTitle() {
		if (CrudContext.getCtx().getSelectedCustomer() != null) {
			if (CrudContext.getCtx().getSelectedCustomer().getId() != null) {
				return MessageUtils.getMessage("edit");
			} else {
				return MessageUtils.getMessage("new");
			}
		}
		return "";
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void saveSelectedCustomer() throws OctopusException {
		SECustomer customer = CrudContext.getCtx().getSelectedCustomer();
		if (customer.getGroup() == null) {
			customer.setGroup(EMPTY_GROUP);
		}
		getSalesDao().saveOrUpdate(customer);
		clearCustomer();
		searchCustomers();
	}
	
	/**
	 * 
	 */
	public void cancelCustomerEdit() {
		clearCustomer();
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void deleteCustomer() throws OctopusException {
		if (CrudContext.getCtx().getSelectedCustomer() != null && CrudContext.getCtx().getSelectedCustomer().getId() != null) {
			getSalesDao().delete(CrudContext.getCtx().getSelectedCustomer());
			searchCustomers();
		}
		clearCustomer();
	}
	
	/**
	 * 
	 * @return
	 * @throws OctopusException 
	 */
	public List<SelectItem> getAllPricesList() throws OctopusException {
		List<Integer> pricesList = getPricesList();
		List<SelectItem> itemsList = new ArrayList<SelectItem>();
		for (Integer list : pricesList) {
			itemsList.add(new SelectItem(list));
		}
		return itemsList;
	}
	
	private List<Integer> getPricesList() throws OctopusException {
		if (CrudContext.getCtx().getPricesList() == null) {
			CrudContext.getCtx().setPricesList(ApplicationContext.getCtx().getSalesDao().getAllPricesList(LoginBean.getLoggedUser().getCompany().getId()));
		}
		return CrudContext.getCtx().getPricesList();
	}
}
