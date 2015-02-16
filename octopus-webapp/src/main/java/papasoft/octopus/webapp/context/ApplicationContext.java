/**
 * 
 */
package papasoft.octopus.webapp.context;

import javax.faces.context.FacesContext;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import papasoft.octopus.se.dao.SalesDao;

/**
 * @author maqui
 *
 */
public class ApplicationContext {
	
	private static ApplicationContext instance = null;
	
	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getCtx() {
		if (instance == null) {
			instance = init();
		}
		return instance;
	}
		
	private SalesDao salesDao;
	
	private static ApplicationContext init() {
		ApplicationContext applicationContext = (ApplicationContext) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessionContext");
		if (applicationContext == null) {
			org.springframework.context.ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
			applicationContext = (ApplicationContext) context.getBean("applicationContext");
		}
		return applicationContext;
	}

	/**
	 * @return the salesDao
	 */
	public SalesDao getSalesDao() {
		return salesDao;
	}

	/**
	 * @param salesDao the salesDao to set
	 */
	public void setSalesDao(SalesDao salesDao) {
		this.salesDao = salesDao;
	}
}
