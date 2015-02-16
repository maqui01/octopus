/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Hibernate;

import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.User;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.webapp.context.ApplicationContext;

/**
 * @author maqui
 *
 */
@ManagedBean
@SessionScoped
public class UserBean extends AbstractBean {
	
	private static final String ADMIN_NAME = "admin";

	/**
	 * 
	 * @return
	 * @throws OctopusException
	 */
	public static User getLoggedUser() throws OctopusException {
	    return (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(LoginBean.AUTH_KEY);
	}
	
	/**
	 * 
	 * @return
	 * @throws OctopusException 
	 */
	public Boolean getIsAdminUser() throws OctopusException {
		User user = getLoggedUser();
		if (user.getRoles() != null) {
			if (!Hibernate.isInitialized(user.getRoles())) {
				ApplicationContext.getCtx().getSalesDao().initialize(user, "roles");
			}
			for (Role role : user.getRoles()) {
				if (role.getName().equals(ADMIN_NAME)) {
					return true;
				}
			}
		}
		return false;
	}
}
