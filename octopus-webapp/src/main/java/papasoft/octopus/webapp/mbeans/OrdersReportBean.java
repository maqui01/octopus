package papasoft.octopus.webapp.mbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.component.datatable.DataTable;

import papasoft.octopus.domain.Order;
import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.User;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.se.domain.SEOrder;
import papasoft.octopus.se.domain.SESalesUser;
import papasoft.octopus.webapp.utils.ReportsHelper;

@ManagedBean
@RequestScoped
public class OrdersReportBean extends AbstractBean implements Serializable {
	
	private static final long serialVersionUID = 9077774962462293055L;
	
	private static final int ORDERS_TABLE_ROWS_PER_PAGE = 10;
	
	private String customerFilter;
	private Date dateSinceFilter = DateUtils.truncate(new Date(), Calendar.DATE);
	private Date dateToFilter;
	private Long numberFromFilter;
	private Long numberToFilter;
	private Long userIdFilter;
	private Integer orderStatusFilter;

	private DataTable ordersDatatable;
	
	private List<SEOrder> orders;
	private List<SelectItem> usersItems;
	private SEOrder selectedOrder;

	/**
	 * 
	 * @throws OctopusException
	 */
	private void retrieveOrders() throws OctopusException {
		setOrders(getSalesDao().retrieveOrders(getCustomerFilter(), 
				getUserIdFilter(), getNumberFromFilter(), getNumberToFilter(), getOrderStatusFilter(),
				getDateSinceFilter(), getDateToFilter(), LoginBean.getLoggedUser().getCompany().getId()));
	}
	
	/**
	 * 
	 */
	public void refreshOperations() {
		setOrders(null);
		closeOrder();
		getOrdersDatatable().setFirst(0);
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void exportOrdersToPdf() throws OctopusException {
		FacesContext context = FacesContext.getCurrentInstance();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("customerFilter", getCustomerFilter());
		params.put("dateSinceFilter", getDateSinceFilter());
		params.put("dateToFilter", getDateToFilter());
		params.put("numberFromFilter", getNumberFromFilter());
		params.put("numberToFilter", getNumberToFilter());
		
		String statusFilter = null;
		if ( getOrderStatusFilter() != null ) {
			switch (getOrderStatusFilter()) {
			case Order.STATUS_CONFIRMED:
				statusFilter = getI18N("accepted");
				break;
			case Order.STATUS_ON_EDITION:
				statusFilter = getI18N("pending");
				break;
			case Order.STATUS_CANCELED:
				statusFilter = getI18N("cancelled");
				break;
			}
		}
		params.put("orderStatusFilter", statusFilter);

		String userFilter = null;
		if ( getUserIdFilter() != null ) {
			User user = getSalesDao().getUser(getUserIdFilter());
			userFilter = user.getName();
		}
		params.put("userFilter", userFilter);
				
		ReportsHelper.exportPDF(context, params, getOrders(), "Pedidos", "orders.jasper");
	}

	/**
	 * 
	 * @return
	 * @throws OctopusException
	 */
	public List<SelectItem> getUsersItems() throws OctopusException {
		if (this.usersItems == null) {
			this.usersItems = retrieveUsersItems();
		}
		return usersItems;
	}

	/**
	 * 
	 * @return
	 * @throws MobileDepositException
	 */
	private List<SelectItem> retrieveUsersItems() throws OctopusException {
		Collection<String> roles = new ArrayList<String>();
		roles.add(Role.MOBILE_ROLE);
		roles.add(Role.ADMIN_ROLE);
		List<SESalesUser> users = getSalesDao().retrieveAllUsers(null, roles, LoginBean.getLoggedUser().getCompany().getName());
		
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (SESalesUser user : users) {
			items.add(new SelectItem(user.getId(), user.getName()));
		}
		return items;
	}

	/**
	 * 
	 * @param usersItems
	 */
	public void setUsersItems(List<SelectItem> usersItems) {
		this.usersItems = usersItems;
	}

	/**
	 * 
	 * @return
	 */
	public SEOrder getSelectedOrder() {
		return selectedOrder;
	}

	/**
	 * 
	 * @param selectedOperation
	 */
	public void setSelectedOrder(SEOrder selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void selectOrder() throws OctopusException {
		Integer rowsPerPage = ORDERS_TABLE_ROWS_PER_PAGE/2;
		getOrdersDatatable().setRows(rowsPerPage);
		
		// Veo de estar en la página correcta:
		Integer indexSelected = getOrders().indexOf(getSelectedOrder());
		Integer currentPage = indexSelected / rowsPerPage;
		//if ( indexSelected % rowsPerPage > 0 ) currentPage++;
		getOrdersDatatable().setFirst(currentPage * rowsPerPage);
	}
	
	/**
	 * 
	 */
	public void closeOrder() {
		getOrdersDatatable().setRows(ORDERS_TABLE_ROWS_PER_PAGE);
		setSelectedOrder(null);
		getOrdersDatatable().setFirst( getOrdersDatatable().getFirst() > ORDERS_TABLE_ROWS_PER_PAGE/2 
				? getOrdersDatatable().getFirst()-ORDERS_TABLE_ROWS_PER_PAGE/2
				: 0 );
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void nextOrder() throws OctopusException {
		Integer index = 0;
		if ( getSelectedOrder() != null ) {
			index = getOrders().indexOf(getSelectedOrder())+1;
		}
		if (index >= getOrders().size()) index = getOrders().size()-1;
		setSelectedOrder(getOrders().get(index));
		selectOrder();
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void prevOrder() throws OctopusException {
		Integer index = 0;
		if ( getSelectedOrder() != null ) {
			index = getOrders().indexOf(getSelectedOrder())-1;
		}
		if (index < 0) index = 0;
		setSelectedOrder(getOrders().get(index));
		selectOrder();
	}

	/**
	 * 
	 * @return
	 * @throws OctopusException
	 */
	public List<SEOrder> getOrders() throws OctopusException {
		if (orders == null) {
			retrieveOrders();
		}
		return orders;
	}

	/**
	 * 
	 * @param orders
	 */
	public void setOrders(List<SEOrder> orders) {
		this.orders = orders;
	}

	/**
	 * @return the ordersDatatable
	 */
	public DataTable getOrdersDatatable() {
		return ordersDatatable;
	}

	/**
	 * @param ordersDatatable the ordersDatatable to set
	 */
	public void setOrdersDatatable(DataTable ordersDatatable) {
		this.ordersDatatable = ordersDatatable;
	}

	/**
	 * @return the customerFilter
	 */
	public String getCustomerFilter() {
		return customerFilter;
	}

	/**
	 * @param customerFilter the customerFilter to set
	 */
	public void setCustomerFilter(String customerFilter) {
		this.customerFilter = customerFilter;
	}

	/**
	 * @return the dateSinceFilter
	 */
	public Date getDateSinceFilter() {
		return dateSinceFilter;
	}

	/**
	 * @param dateSinceFilter the dateSinceFilter to set
	 */
	public void setDateSinceFilter(Date dateSinceFilter) {
		this.dateSinceFilter = dateSinceFilter;
	}

	/**
	 * @return the dateToFilter
	 */
	public Date getDateToFilter() {
		return dateToFilter;
	}

	/**
	 * @param dateToFilter the dateToFilter to set
	 */
	public void setDateToFilter(Date dateToFilter) {
		this.dateToFilter = dateToFilter;
	}

	/**
	 * @return the numberFromFilter
	 */
	public Long getNumberFromFilter() {
		return numberFromFilter;
	}

	/**
	 * @param numberFromFilter the numberFromFilter to set
	 */
	public void setNumberFromFilter(Long numberFromFilter) {
		this.numberFromFilter = numberFromFilter;
	}

	/**
	 * @return the numberToFilter
	 */
	public Long getNumberToFilter() {
		return numberToFilter;
	}

	/**
	 * @param numberToFilter the numberToFilter to set
	 */
	public void setNumberToFilter(Long numberToFilter) {
		this.numberToFilter = numberToFilter;
	}

	/**
	 * @return the userIdFilter
	 */
	public Long getUserIdFilter() {
		return userIdFilter;
	}

	/**
	 * @param userIdFilter the userIdFilter to set
	 */
	public void setUserIdFilter(Long userIdFilter) {
		this.userIdFilter = userIdFilter;
	}

	/**
	 * @return the orderStatusFilter
	 */
	public Integer getOrderStatusFilter() {
		return orderStatusFilter;
	}

	/**
	 * @param orderStatusFilter the orderStatusFilter to set
	 */
	public void setOrderStatusFilter(Integer orderStatusFilter) {
		this.orderStatusFilter = orderStatusFilter;
	}
	
}
