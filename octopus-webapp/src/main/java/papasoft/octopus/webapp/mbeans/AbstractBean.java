/**
 * 
 */
package papasoft.octopus.webapp.mbeans;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import papasoft.octopus.se.dao.SalesDao;
import papasoft.octopus.webapp.context.ApplicationContext;

/**
 * @author De La
 *
 */
public abstract class AbstractBean {


	/**
	 * @return
	 */
	protected SalesDao getSalesDao() {
		return ApplicationContext.getCtx().getSalesDao();
	}
	
	/**
	 * @return
	 */
	protected String getI18N(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
	    ResourceBundle messages = context.getApplication().evaluateExpressionGet(context, "#{msg}", ResourceBundle.class);
	    try {
	    	return messages.getString(key);
	    } catch (MissingResourceException e) {
	    	return "?";
	    }
	}
	
}
