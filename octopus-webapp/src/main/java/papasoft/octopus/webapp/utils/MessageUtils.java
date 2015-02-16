package papasoft.octopus.webapp.utils;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

public class MessageUtils {

	
	public static String getMessage(String key) {
		String bundlename = "octopus";
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		ResourceBundle bundle = ResourceBundle.getBundle(bundlename, locale);
		String value = bundle.getString(key);
		if (value != null) {
			return value;
		}
		return "??? KEY " + key + " NOT FOUND ???";
	}
}
