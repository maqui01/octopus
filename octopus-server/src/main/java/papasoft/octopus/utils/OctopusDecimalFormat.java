/**
 * 
 */
package papasoft.octopus.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author maqui
 *
 */
public class OctopusDecimalFormat {

	/**
	 * 
	 * @param number
	 * @return
	 */
	public static String formatDouble(Double number) {
		if (number == null) {
			return "";
		}
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat format = new DecimalFormat("0.00", decimalFormatSymbols);
		return format.format(number);
	}
	
	/**
	 * 
	 * @param number
	 * @param pattern
	 * @return
	 */
	public static String formatDouble(Double number, String pattern) {
		if (number == null) {
			return "";
		}
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		DecimalFormat format = new DecimalFormat(pattern, decimalFormatSymbols);
		return format.format(number);
	}
}
