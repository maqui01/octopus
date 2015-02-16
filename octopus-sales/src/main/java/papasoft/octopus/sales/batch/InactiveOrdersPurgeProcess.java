/**
 * 
 */
package papasoft.octopus.sales.batch;

import java.util.Date;
import java.util.List;

import papasoft.octopus.domain.Order;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;
import papasoft.octopus.module.batch.BatchProcess;
import papasoft.octopus.sales.module.SalesInterface;

/**
 * @author maqui
 *
 */
public class InactiveOrdersPurgeProcess extends BatchProcess {
	
	private static final String PROCESS_NAME = "Inactive orders purge process";
	private static final String PROCESS_LOGGER = "papasoft.octopus.inactiveOrdersPurgeProcessLogger";
	private Integer orderInactiveMinutes;

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
		List<Order> pendingOrders = null;
		try {
			pendingOrders = getSalesInterface().retrievePendingOrders();
		} catch (OctopusException ex) {
			throw new OctopusException("Failed to retrieve pending orders", ex);
		}
		if (pendingOrders != null) {
			Date limitDate = new Date(System.currentTimeMillis() - getOrderInactiveMinutes() * 60000);
			for (Order order : pendingOrders) {
				if (order.getDate() != null && order.getDate().before(limitDate)) {
					LogManager.logInfo("Order [" + order.getId() + "] is purged for being pending after " + getOrderInactiveMinutes() + " minutes", getLoggerName());
					try {
						getSalesInterface().cancelOrder(order.getId(), order.getUser().getId());
					} catch (OctopusException e) {
						LogManager.logError("Order [" + order.getId() + "] failed to delete from database", e, getLoggerName());
					}
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

	/**
	 * @return the orderInactiveMinutes
	 */
	public Integer getOrderInactiveMinutes() {
		return orderInactiveMinutes;
	}

	/**
	 * @param orderInactiveMinutes the orderInactiveMinutes to set
	 */
	public void setOrderInactiveMinutes(Integer orderInactiveMinutes) {
		this.orderInactiveMinutes = orderInactiveMinutes;
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

}
