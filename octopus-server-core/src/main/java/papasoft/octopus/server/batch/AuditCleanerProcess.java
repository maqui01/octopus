/**
 * 
 */
package papasoft.octopus.server.batch;

import java.util.Calendar;
import java.util.Date;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;

/**
 * @author De La
 *
 */
public class AuditCleanerProcess extends ServerBatchProcess {
	private static final String PROCESS_NAME = "Audit Cleaner process";
	private static final String PROCESS_LOGGER = "papasoft.octopus.auditCleanerProcess";
	
	private Integer daysToSave = 30;

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#init()
	 */
	@Override
	protected Boolean init() {
		return true;
	}

	/* (non-Javadoc)
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#execute()
	 */
	@Override
	public void execute() throws OctopusException {
		try {
			//Obtengo date de la fecha límite:
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -getDaysToSave());
			Date date = calendar.getTime();
			
//			Integer deleted = OctopusContext.getCtx().getAuditManager().getAuditDao().deleteAllOlderThanDate(date);
//			LogManager.logDebug( "Old " + deleted + " audit entries deleted", getLoggerName());
		} catch (Throwable th) {
			// Hago como si nada...
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#getLoggerName()
	 */
	@Override
	protected String getLoggerName() {
		return PROCESS_LOGGER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#getProcessName()
	 */
	@Override
	public String getProcessName() {
		return PROCESS_NAME;
	}

	/**
	 * @return the daysToSave
	 */
	public Integer getDaysToSave() {
		return daysToSave;
	}

	/**
	 * @param daysToSave the daysToSave to set
	 */
	public void setDaysToSave(Integer daysToSave) {
		this.daysToSave = daysToSave;
	}

}
