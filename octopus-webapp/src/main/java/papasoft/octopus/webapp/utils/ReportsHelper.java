/**
 * 
 */
package papasoft.octopus.webapp.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author De La
 * 
 */
public abstract class ReportsHelper {

	/**
	 * 
	 * @param context
	 * @param fileName
	 * @param jasperReportPath
	 */
	public static void exportPDF(FacesContext context,  HashMap<String, Object> params, Collection<? extends Object> data, String fileName, String jasperReportPath) {
		JasperPrint jasperPrint;
		try {
			jasperPrint = JasperFillManager.fillReport(
					ReportsHelper.class.getResourceAsStream("/" + jasperReportPath),
							params,
							new JRBeanCollectionDataSource(data));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

			writePDFToResponse(context.getExternalContext(), baos, fileName);

			context.responseComplete();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private static void writePDFToResponse(ExternalContext externalContext, ByteArrayOutputStream baos, String fileName) {
		try {
			externalContext.responseReset();
			externalContext.setResponseContentType("application/pdf");
			externalContext.setResponseHeader("Expires", "0");
			externalContext.setResponseHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			externalContext.setResponseHeader("Pragma", "public");
			externalContext.setResponseHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
			externalContext.setResponseContentLength(baos.size());
			OutputStream out = externalContext.getResponseOutputStream();
			baos.writeTo(out);
			externalContext.responseFlushBuffer();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
}
