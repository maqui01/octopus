/**
 * 
 */
package papasoft.octopus.audit;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.StringType;

import papasoft.octopus.dao.AbstractDao;
import papasoft.octopus.exception.OctopusException;
import papasoft.octopus.log.LogManager;

/**
 * @author maqui
 *
 */
public class AuditDao extends AbstractDao {

	/**
	 * @param entry
	 * @throws OctopusException 
	 */
	public void save(AuditEntry entry) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			session.save(entry);
			session.flush();
		} catch (Throwable ex) {
			LogManager.logError("Error saving audit entry", ex);
			throw new OctopusException(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @return
	 * @throws OctopusException 
	 */
	@SuppressWarnings("unchecked")
	public List<String> retrieveAllImeis() throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			SQLQuery query = session.createSQLQuery("SELECT DISTINCT(deviceId) from AUDIT_ENTRY WHERE deviceId is not null");
			query.addScalar("deviceId", StringType.INSTANCE);
			return query.list();
		} catch (Throwable ex) {
			LogManager.logError("Error retrieving device Ids", ex);
			throw new OctopusException(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	/**
	 * 
	 * @param date
	 * @throws OctopusException 
	 */
	public int deleteAllOlderThanDate(Date date) throws OctopusException {
		Session session = null;
		try {
			session = getSession();
			Query query = session.createQuery("DELETE FROM AuditEntry where date < :date");
			query.setDate("date", date);
			return query.executeUpdate();
		} catch (Throwable ex) {
			LogManager.logError("Error deleting old audits", ex);
			throw new OctopusException(ex);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
