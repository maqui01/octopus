/**
 * 
 */
package papasoft.octopus.se.domain.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import papasoft.octopus.se.domain.SEOrder;

/**
 * @author maqui
 *
 */
public class OrderIdGenerator implements IdentifierGenerator {

	/* (non-Javadoc)
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.spi.SessionImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		SEOrder orderToSave = (SEOrder)object;
		Connection conn = session.connection();
		try {
			PreparedStatement st = conn.prepareStatement("SELECT MAX(id) as nextval FROM order_m WHERE companyId = ?");
			st.setLong(1, orderToSave.getCompany().getId());
			ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Long id = rs.getLong("nextval");
                return new Serializable[]{id + 1, orderToSave.getCompany().getId()};
            }
		} catch (Exception e) {
			throw new HibernateException("Error generating next value", e);
		}
		return null;
	}

}
