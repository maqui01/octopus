/**
 * 
 */
package papasoft.octopus.dao;

import java.lang.reflect.Field;

import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import papasoft.octopus.log.LogManager;

/**
 * @author maqui
 *
 */
public class AbstractDao {

	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * 
	 * @return
	 */
	public Session getSession() {
		return this.sessionFactory.openSession();
	}
	

	
	/**
	 * 
	 * @param entity
	 */
	public void initialize(Object entity) {
		
		Session s = null;
		try {
			if (!Hibernate.isInitialized(entity)) {
				s = getSession();
				s.beginTransaction();
				s.buildLockRequest(LockOptions.NONE).lock(entity);
				Hibernate.initialize(entity);
				s.getTransaction().commit();
			}
		} catch (Exception e) {
			if (s != null) {
				s.getTransaction().rollback();
			}
			LogManager.logError("Error during initialization of lazy property", e);
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}
	
	/**
	 * 
	 * @param detachedParent
	 * @param fieldName
	 */
	public void initialize(Object detachedParent, String fieldName) {
		Session s = null;
		try {
			s = getSession();
			s.beginTransaction();
			s.update(detachedParent); 

		    // get the field from the entity and initialize it
		    Field fieldToInitialize = findField(detachedParent, fieldName);
		    fieldToInitialize.setAccessible(true);
		    Object objectToInitialize = fieldToInitialize.get(detachedParent);

		    Hibernate.initialize(objectToInitialize);
			s.getTransaction().commit();
		} catch (Exception e) {
			if (s != null) {
				s.getTransaction().rollback();
			}
			LogManager.logError("Error during initialization of lazy property", e);
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	/**
	 * @param detachedParent
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private Field findField(Object detachedParent, String fieldName) {
		Class clazz = detachedParent.getClass();
		while (clazz != null) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
 	}
}
