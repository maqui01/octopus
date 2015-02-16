package papasoft.octopus.webapp.mbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import papasoft.octopus.domain.Role;
import papasoft.octopus.domain.User;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.webapp.context.ApplicationContext;
import papasoft.octopus.webapp.utils.MessageUtils;

@ManagedBean
@SessionScoped
public class LoginBean {

	private static final String COMPANY_PARAM_NAME = "companyName";

	public static final String AUTH_KEY = "octopus.user.name";
	
	private String username;
	private String password;
	private String errorMessage;
	private String companyName;
	private Boolean changedCompany = false;
	
	private User user;

	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String login() {
		try {
			if (getUsername() != null && getUsername().trim().length() > 0 &&
					getPassword() != null && getPassword().trim().length() > 0) {
				
				String companyName = (getSavedCompanyName() != null && !getChangedCompany()) ? getSavedCompanyName() : getCompanyName();
				
				User user = ApplicationContext.getCtx().getSalesDao().getUser(getUsername(), companyName);
				
				if ( user != null 
						&& user.getPassword().equals(getPassword())
						&& hasPermission(user) ) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(
					        AUTH_KEY, user);
					setSavedCompanyName(companyName);
					setErrorMessage(null);		
					setChangedCompany(false);
				    return "login_success";
				}				    
			}
			setErrorMessage(MessageUtils.getMessage("login_error"));
		    return "login_failed";			
		} catch (OctopusException e) {
			setErrorMessage(MessageUtils.getMessage("login_error"));
		    return "login_failed";
		}

	}
	
	/**
	 * @param user
	 * @return
	 */
	private boolean hasPermission(User user) {
		boolean hasPermission = false;
		ApplicationContext.getCtx().getSalesDao().initialize(user, "roles");
		for ( Role role : user.getRoles() ) {
			if ( role.getName().equals( Role.ADMIN_ROLE ) || role.getName().equals( Role.WEBAPP_ROLE ) ) {
				hasPermission = true;
				break;
			}
		}
		return hasPermission;
	}
	
	/**
	 * 
	 * @return
	 */
	public String logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();  
	    return "logout";
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static User getLoggedUser() throws OctopusException {
	    return (User)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY);
	}
	
	/**
	 * 
	 * @param companyName
	 */
	public void setSavedCompanyName(String companyName) {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		Cookie cookie = new Cookie(COMPANY_PARAM_NAME, companyName);
		cookie.setMaxAge(31536000);
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSavedCompanyName() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		Cookie cookie = getCompanyCookie(request.getCookies());
		if (cookie != null && cookie.getValue() != null && cookie.getValue().trim().length() > 0) {
			return cookie.getValue();
		}
		return null;
	}
	
	/**
	 * @param cookies
	 * @return
	 */
	private Cookie getCompanyCookie(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(COMPANY_PARAM_NAME)) {
					return cookie;
				}
			}
		}
		return null;
	}
	/**
	 * 
	 */
	public String changeCompany() {
		setSavedCompanyName(null);
		setChangedCompany(true);
		return "logout";
	}
	
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	/**
	 * @return the changedCompany
	 */
	public Boolean getChangedCompany() {
		return changedCompany;
	}
	/**
	 * @param changedCompany the changedCompany to set
	 */
	public void setChangedCompany(Boolean changedCompany) {
		this.changedCompany = changedCompany;
	}
}
