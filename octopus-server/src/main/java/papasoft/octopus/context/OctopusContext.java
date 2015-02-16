/**
 * 
 */
package papasoft.octopus.context;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import papasoft.octopus.audit.AuditManager;
import papasoft.octopus.config.ConfigurationManager;
import papasoft.octopus.module.Module;
import papasoft.octopus.server.batch.ServerBatchProcess;
import papasoft.octopus.session.SessionManager;

/**
 * @author maqui
 *
 */
public class OctopusContext {

	private SessionManager sessionManager;
	private ConfigurationManager configurationManager;
	private AuditManager auditManager;
	private List<Module> modules;
	private List<ServerBatchProcess> batchProcesses;
	private String companyName;

	private static OctopusContext instance = null;
	
	public static OctopusContext getCtx() {
		if (instance == null) {
			ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"application-context.xml"});
			instance = (OctopusContext) context.getBean("octopusContext");
		}
		return instance;
	}
	
	/**
	 *
	 * @return the session manager
	 */
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	/**
	 * @param sessionManager the sessionManager to set
	 */
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * 
	 * @return
	 */
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/**
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @return the modules
	 */
	public List<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules the modules to set
	 */
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	/**
	 * 
	 * @param moduleId
	 * @return
	 */
	public Module getModule(Integer moduleId) {
		if (this.modules != null && this.modules.size() > 0) {
			for (Module module : this.modules) {
				if (module.getModuleId().equals(moduleId)) {
					return module;
				}
			}
		}
		return null;
	}

	/**
	 * @return the auditManager
	 */
	public AuditManager getAuditManager() {
		return auditManager;
	}

	/**
	 * @param auditManager the auditManager to set
	 */
	public void setAuditManager(AuditManager auditManager) {
		this.auditManager = auditManager;
	}

	/**
	 * @return the batchProcesses
	 */
	public List<ServerBatchProcess> getBatchProcesses() {
		return batchProcesses;
	}

	/**
	 * @param batchProcesses the batchProcesses to set
	 */
	public void setBatchProcesses(List<ServerBatchProcess> batchProcesses) {
		this.batchProcesses = batchProcesses;
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
}
