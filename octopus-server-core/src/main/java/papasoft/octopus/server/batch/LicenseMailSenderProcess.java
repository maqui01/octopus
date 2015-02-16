/**
 * 
 */
package papasoft.octopus.server.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import papasoft.octopus.context.OctopusContext;
import papasoft.octopus.exception.OctopusException;

/**
 * @author De La
 * 
 */
public class LicenseMailSenderProcess extends ServerBatchProcess {

	private static final String PROCESS_NAME = "License mail sender process";
	private static final String PROCESS_LOGGER = "papasoft.octopus.licenseMailSenderProcess";
	
	private static final String PROPERTIE_FROM = "licenciaSenderPapaSoft@gmail.com";
	private static final String PROPERTIE_PASSWORD = "papalicencia1234";
	private static final String PROPERTIE_TO = "licenciaPapaSoft@gmail.com";
	
	private static final String PROPERTIE_SUBJECT = "Automatic license message from ";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#init()
	 */
	@Override
	protected Boolean init() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see papasoft.octopus.server.batch.ServerBatchProcess#execute()
	 */
	@Override
	public void execute() throws OctopusException {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
	
			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(PROPERTIE_FROM, PROPERTIE_PASSWORD);
						}
					});
	
			try {
	
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(PROPERTIE_FROM));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(PROPERTIE_TO));
				message.setSubject(PROPERTIE_SUBJECT + OctopusContext.getCtx().getCompanyName());
				List<String> deviceIds = OctopusContext.getCtx().getAuditManager().getAuditDao().retrieveAllImeis();
				String ip = retrieveIp();
				String body = "Device ids from " + ip + ":" + "\n";
				for (String id : deviceIds) {
					body += id + "\n";
				}
				message.setText(body);
	
				Transport.send(message);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		} catch ( Throwable th ) {
			//No hago nada!
		}
	}

	/**
	 * @return
	 */
	private String retrieveIp() {
		try {
			URL whatismyip = new URL("http://api.exip.org/?call=ip");
			BufferedReader in = new BufferedReader(new InputStreamReader(
			                whatismyip.openStream()));
	
			return in.readLine(); //you get the IP as a String
		} catch (Throwable th) {
			return "N/A";
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

}
