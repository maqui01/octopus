/**
 * 
 */
package papasoft.octopus.webapp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import papasoft.octopus.domain.Customer;
import papasoft.octopus.webapp.context.ApplicationContext;
import papasoft.octopus.webapp.log.LogManager;
import papasoft.octopus.webapp.mbeans.LoginBean;

/**
 * @author maqui
 *
 */
@FacesConverter(value="CustomerConverter", forClass=Customer.class)
public class CustomerConverter implements Converter {

	/* (non-Javadoc)
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value)
			throws ConverterException {
		if(value != null && value.trim().length() > 0) {
			try {
				return ApplicationContext.getCtx().getSalesDao().retrieveCustomer(Long.parseLong(value), LoginBean.getLoggedUser().getCompany().getId());
			} catch (Throwable e) {
				LogManager.logError("Error converting Customer", e);
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
            return String.valueOf(((Customer) object).getId());
        }
        else {
            return null;
        }
	}

}
