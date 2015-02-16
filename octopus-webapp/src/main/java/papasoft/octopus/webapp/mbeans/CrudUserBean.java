/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;

import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.UserStatus;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.se.domain.SECustomer;
import papasoft.octopus.se.domain.SESalesUser;
import papasoft.octopus.se.domain.SalesUserCustomer;
import papasoft.octopus.webapp.utils.MessageUtils;

/**
 * @author maqui
 *
 */
@ManagedBean
@RequestScoped
public class CrudUserBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = -4385785154081775354L;
	private static final int ORDERS_TABLE_ROWS_PER_PAGE = 10;
	private String username;
	private DataTable usersDatatable;
	private Collection<SECustomer> selectedUserCustomers;
	private Collection<Role> selectedRoles;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 
	 * @param evt
	 * @throws OctopusException 
	 */
	public void searchUsers() throws OctopusException {
		setUsers(retrieveUsers());
		clearUser();
	}

	/**
	 * @return the users
	 * @throws OctopusException 
	 */
	public List<SESalesUser> getUsers() throws OctopusException {
		List<SESalesUser> users = CrudContext.getCtx().getUsers();
		if (users == null) {
			users = retrieveUsers();
			CrudContext.getCtx().setUsers(users);
		}
		return users;
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	private List<SESalesUser> retrieveUsers() throws OctopusException {
		if (LoginBean.getLoggedUser() != null) {
			return getSalesDao().retrieveAllUsers(getUsername(), null, LoginBean.getLoggedUser().getCompany().getName());
		}
		return null;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<SESalesUser> users) {
		CrudContext.getCtx().setUsers(users);
	}

	/**
	 * @return the usersDatatable
	 */
	public DataTable getUsersDatatable() {
		return usersDatatable;
	}

	/**
	 * @param usersDatatable the usersDatatable to set
	 */
	public void setUsersDatatable(DataTable usersDatatable) {
		this.usersDatatable = usersDatatable;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserRoles() {
		if (getUsersDatatable().isRowAvailable()) {
			SESalesUser user = (SESalesUser) getUsersDatatable().getRowData();
			StringBuilder rolesBuilder = new StringBuilder();
			String sep = "";
			getSalesDao().initialize(user, "roles");
			for (Role role : user.getRoles()) {
				rolesBuilder.append(sep).append(role.getName());
				sep = " - ";
			}
			return rolesBuilder.toString();
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getUserBlocked() {
		if (getUsersDatatable().isRowAvailable()) {
			SESalesUser user = (SESalesUser) getUsersDatatable().getRowData();
			return isUserBlocked(user);
		}
		return false;
	}

	/**
	 * @return the selectedUser
	 */
	public SESalesUser getSelectedUser() {
		return CrudContext.getCtx().getSelectedUser();
	}

	/**
	 * @param selectedUser the selectedUser to set
	 */
	public void setSelectedUser(SESalesUser selectedUser) {
		CrudContext.getCtx().setSelectedUser(selectedUser);
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getSelectedUserBlocked() {
		return isUserBlocked(getSelectedUser());
	}
	
	/**
	 * 
	 * @param blocked
	 */
	public void setSelectedUserBlocked(Boolean blocked) {
		if (!blocked) {
			getSelectedUser().setStatus(UserStatus.NORMAL);
		} else {
			getSelectedUser().setStatus(UserStatus.BLOCKED);
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private Boolean isUserBlocked(SESalesUser user) {
		if (user != null) {
			return user.getStatus() > 0;
		}
		return false;
	}
	
	/**
	 * 
	 */
	public Integer getUserCustomersSize() {
		if (getUsersDatatable().isRowAvailable()) {
			SESalesUser user = (SESalesUser) getUsersDatatable().getRowData();
			if (user.getCustomers() != null)
				return user.getCustomers().size();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param evt
	 * @throws OctopusException 
	 */
	public void userSelect(SelectEvent evt) throws OctopusException {
		getSalesDao().initialize(getSelectedUser(), "roles");
		setSelectedUserCustomers(filterCustomers(getSelectedUser().getCustomers()));
		setSelectedRoles(getSelectedUser().getRoles());
		setPasswordChanged(false);

		Integer rowsPerPage = ORDERS_TABLE_ROWS_PER_PAGE/2;
		getUsersDatatable().setRows(rowsPerPage);
		
		// Veo de estar en la página correcta:
		Integer indexSelected = getUsers().indexOf(getSelectedUser());
		Integer currentPage = indexSelected / rowsPerPage;
		//if ( indexSelected % rowsPerPage > 0 ) currentPage++;
		getUsersDatatable().setFirst(currentPage * rowsPerPage);
	}
	
	/**
	 * @param customers
	 * @return
	 */
	private Collection<SECustomer> filterCustomers(
			Collection<SalesUserCustomer> salesCustomers) {
		if (salesCustomers == null) {
			return new ArrayList<SECustomer>();
		}
		Collection<SECustomer> customers = new ArrayList<SECustomer>();
		for (SalesUserCustomer salesCustomer : salesCustomers) {
			customers.add(salesCustomer.getCustomer());
		}
		return customers;
	}
	 
	/**
	 * 
	 * @return
	 * @throws OctopusException 
	 */
	public Collection<Role> getAllRoles() throws OctopusException {
		return getSalesDao().retrieveAllRoles();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAdminRoleName() {
		return Role.ADMIN_ROLE;
	}
	 
	/**
	 * 
	 * @return
	 * @throws OctopusException 
	 */
	public Collection<SECustomer> getAllCustomers() throws OctopusException {
		return getSalesDao().retrieveAllCustomers(LoginBean.getLoggedUser());
	}

	/**
	 * @return the selectedUserCustomers
	 */
	public Collection<SECustomer> getSelectedUserCustomers() {
		return selectedUserCustomers;
	}

	/**
	 * @param selectedUserCustomers the selectedUserCustomers to set
	 */
	public void setSelectedUserCustomers(Collection<SECustomer> selectedUserCustomers) {
		this.selectedUserCustomers = selectedUserCustomers;
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void saveSelectedUser() throws OctopusException {
		SESalesUser user = getSelectedUser();
		updateUserCustomers(user, getSelectedUserCustomers());
		Role adminRole = getAdminRole(user.getRoles());
		user.setRoles(getSelectedRoles());
		if (adminRole != null) {
			user.getRoles().add(adminRole);
		}
		getSalesDao().saveUser(user);
		clearUser();
		searchUsers();
	}

	/**
	 * @param roles
	 * @return
	 */
	private Role getAdminRole(Collection<Role> roles) {
		Role adminRole = null;
		if (roles != null) {
			for (Role role : roles) {
				if (role.getName().equals(Role.ADMIN_ROLE)) {
					adminRole = role;
					break;
				}
			}
		}
		return adminRole;
	}

	/**
	 * 
	 */
	private void clearUser() {
		setSelectedUser(null);
		setPasswordChanged(false);
	}

	/**
	 * @param user
	 * @param selectedUserCustomers
	 */
	private void updateUserCustomers(SESalesUser user,
			Collection<SECustomer> selectedUserCustomers) {
		if (user.getCustomers() != null) {
			Collection<SalesUserCustomer> customersToRemove = new ArrayList<SalesUserCustomer>();
			for (SalesUserCustomer userCustomer : user.getCustomers()) {
				if (!getSelectedUserCustomers().contains(userCustomer.getCustomer())) {
					customersToRemove.add(userCustomer);
				}
			}
			if (customersToRemove.size() > 0) {
				user.getCustomers().removeAll(customersToRemove);
			}
		}
		if (getSelectedUserCustomers() != null) {
			Collection<SalesUserCustomer> customersToAdd = new ArrayList<SalesUserCustomer>();
			for (SECustomer customer : getSelectedUserCustomers()) {
				Boolean in = false;
				if (user.getCustomers() != null) {
					for (SalesUserCustomer userCustomer : user.getCustomers()) {
						if (userCustomer.getCustomer().equals(customer)) {
							in = true;
							break;
						}
					}
				}
				if (!in) {
					SalesUserCustomer userCustomer = new SalesUserCustomer();
					userCustomer.setCustomer(customer);
					userCustomer.setSalesUser(user);
					userCustomer.setDaysToVisit("1,1,1,1,1,1,1");
					customersToAdd.add(userCustomer);
				}
			}
			if (customersToAdd.size() > 0) {
				if (user.getCustomers() == null) {
					user.setCustomers(new ArrayList<SalesUserCustomer>());
				}
				user.getCustomers().addAll(customersToAdd);
			}
		}
	}

	/**
	 * @return the selectedRoles
	 */
	public Collection<Role> getSelectedRoles() {
		return selectedRoles;
	}

	/**
	 * @param selectedRoles the selectedRoles to set
	 */
	public void setSelectedRoles(Collection<Role> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
	
	/**
	 * 
	 */
	public void cancelUserEdit() {
		clearUser();
	}

	/**
	 * @return the passwordChanged
	 */
	public Boolean getPasswordChanged() {
		return CrudContext.getCtx().getPasswordChanged();
	}

	/**
	 * @param passwordChanged the passwordChanged to set
	 */
	public void setPasswordChanged(Boolean passwordChanged) {
		CrudContext.getCtx().setPasswordChanged(passwordChanged);
	}
	
	/**
	 * 
	 */
	public void changePassword() {
		setPasswordChanged(true);
	}
	
	/**
	 * @throws OctopusException 
	 * 
	 */
	public void newUser() throws OctopusException {
		SESalesUser newUser = new SESalesUser();
		newUser.setCompany(LoginBean.getLoggedUser().getCompany());
		setSelectedUser(newUser);
		changePassword();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserPanelTitle() {
		if (getSelectedUser() != null) {
			if (getSelectedUser().getId() != null) {
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
	public void deleteUser() throws OctopusException {
		if (getSelectedUser() != null && getSelectedUser().getId() != null) {
			getSalesDao().delete(getSelectedUser());
			searchUsers();
		}
		clearUser();
	}
}
