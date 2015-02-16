/**
 * 
 */
package papasoft.octopus.webapp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import papasoft.octopus.domain.Role;
import papasoft.octopus.webapp.context.ApplicationContext;
import papasoft.octopus.webapp.log.LogManager;

/**
 * @author maqui
 *
 */
@FacesConverter(value="RoleConverter", forClass=Role.class)
public class RoleConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
			throws ConverterException {
		if(value != null && value.trim().length() > 0) {
			try {
				return ApplicationContext.getCtx().getSalesDao().retrieveRole(Long.parseLong(value));
			} catch (Throwable e) {
				LogManager.logError("Error converting Role", e);
				return null;
			}
        }
        else {
            return null;
        }
	}

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object)
			throws ConverterException {
		if(object != null) {
            return String.valueOf(((Role) object).getId());
        }
        else {
            return null;
        }
	}

}
