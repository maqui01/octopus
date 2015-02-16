/**
 * 
 */
package papasoft.octopus.sales.batch;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import papasoft.octopus.domain.se.Company;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.module.batch.BatchProcess;
import papasoft.octopus.sales.module.SalesInterface;

/**
 * @author De La
 *
 */
public class TestingCompaniesPurgeProcess extends BatchProcess {
	
	private static final String PROCESS_NAME = "Testing companies purge process";
	private static final String PROCESS_LOGGER = "papasoft.octopus.testingCompaniesPurgeProcessLogger";
	private Integer companiesLifeHours;

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.batch.BatchProcess#init()
	 */
	@Override
	protected Boolean init() {
		return true;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.batch.BatchProcess#execute()
	 */
	@Override
	public void execute() throws OctopusException {
		List<Company> companiesToDelete = null;
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());
			calendar.add(GregorianCalendar.HOUR, -getCompaniesLifeHours());
			Date oldestThan = calendar.getTime();
			
			companiesToDelete = getSalesInterface().retrieveTestingCompaniesToPurge( oldestThan );
		} catch (OctopusException ex) {
			throw new OctopusException("Failed to retrieve testing companies to purge", ex);
		}
		if (companiesToDelete != null) {
			for (Company company : companiesToDelete) {
				try {
					getSalesInterface().deleteTestingCompany( company );
					LogManager.logInfo("Company [" + company.getName() + "] was purged after " + getCompaniesLifeHours() + " hours of life.", getLoggerName());
				} catch (OctopusException e) {
					LogManager.logError("Company [" + company.getName() + "] failed to delete from database", e, getLoggerName());
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public SalesInterface getSalesInterface() {
		return (SalesInterface) getModuleInterface();
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.batch.BatchProcess#getLoggerName()
	 */
	@Override
	protected String getLoggerName() {
		return PROCESS_LOGGER;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.module.batch.BatchProcess#getProcessName()
	 */
	@Override
	public String getProcessName() {
		return PROCESS_NAME;
	}

	/**
	 * @return the companiesLifeHours
	 */
	public Integer getCompaniesLifeHours() {
		return companiesLifeHours;
	}

	/**
	 * @param companiesLifeHours the companiesLifeHours to set
	 */
	public void setCompaniesLifeHours(Integer companiesLifeHours) {
		this.companiesLifeHours = companiesLifeHours;
	}

}
