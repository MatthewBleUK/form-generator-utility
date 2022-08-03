package com.form.generator.utility.notifications;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GmailOperations {

	private final Logger LOGGER = LoggerFactory.getLogger(GmailOperations.class);

	private final String USER = "me";

	private final String headerTemplate;
	private final String footerTemplate;


	public GmailOperations(
			@Value("${notification.template.header}") String headerTemplate,
			@Value("${notification.template.footer}") String footerTemplate) {

		this.headerTemplate = headerTemplate;
		this.footerTemplate = footerTemplate;
	}

	public void sendMessage(Gmail service, String userId, MimeMessage email) throws MessagingException, IOException {

		Message message = createMessageWithEmail(email);
		message = service.users().messages().send(userId, message).execute();

		LOGGER.info("Message sent!");
	}

	private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		email.writeTo(baos);

		String encodedEmail = Base64.encodeBase64URLSafeString(baos.toByteArray());
		Message message = new Message();

		message.setRaw(encodedEmail);

		return message;
	}

	private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); //
		email.setSubject(subject);

		email.setText(bodyText);

		return email;
	}

	/**
	 * Method to send a plain text using Gmail api
	 *
	 * @param receiver the email you want to send the attachment to
	 * @param subject  the subject of the email (title)
	 * @param bodyText the text that will be shown in the body
	 */
	public void sendEmail(String receiver, String subject, String bodyText)
			throws IOException, GeneralSecurityException, MessagingException {

		Gmail service = GmailUtils.getGmailService();

		MimeMessage mimeMessage = createEmail(receiver, USER, subject, bodyText);

		Message message = createMessageWithEmail(mimeMessage);

		message = service.users().messages().send(USER, message).execute();

		LOGGER.info("Message sent to: {}", receiver);
	}

	/**
	 * Method to create the email structure of email with body attachment
	 *
	 * @param receiver      email you want to send the Email to
	 * @param subject email subject (title)
	 * @param html    type of attachment to send
	 */
	private MimeMessage createHTMLEmailBodyWithAttachment(String receiver, String subject, String html)
			throws MessagingException {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(USER));

		//For Multiple Email with comma separated ...

		String[] split = receiver.split(",");

		for (String s : split) {

			email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(s));
		}

		email.setSubject(subject);

		Multipart multiPart = new MimeMultipart("mixed");

		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent(html, "text/html; charset=utf-8");
		multiPart.addBodyPart(htmlPart, 0);

		email.setContent(multiPart);

		return email;
	}

	/**
	 * Method to send email with attachment using Gmail api
	 *
	 * @param receiver the email you want to send the attachment to
	 */
	public void sendEmailForConfirmRegistration(String receiver, String token)
			throws IOException, MessagingException, GeneralSecurityException {

		String subject = "Welcome!";

		String htmlText = generateConfirmationEmail(token);
		Gmail service = GmailUtils.getGmailService();

		MimeMessage mimeMessage = createHTMLEmailBodyWithAttachment(receiver, subject, htmlText);
		Message message = createMessageWithEmail(mimeMessage);
		message = service.users().messages().send(USER, message).execute();

		LOGGER.info("Email sent to: {}", receiver);
	}

	public String generateConfirmationEmail(String token) throws IOException {

		//HTML parse
		Document headerDoc = Jsoup.parse(new File(headerTemplate), "utf-8");
		Elements headerElement = headerDoc.getElementsByTag("html");

		Document footerDoc = Jsoup.parse(new File(footerTemplate), "utf-8");
		Elements footerElement = footerDoc.getElementsByTag("html");

		String header = Objects.requireNonNull(headerElement.first()).html();
		String footer = Objects.requireNonNull(footerElement.first()).html();

		return "<html>" + header +
				"<table role=\"presentation\" style=\"width:602px;margin: 0 auto;border-collapse:collapse;border:0;" +
				"border-spacing:0;\">\t\n" +
				"   <tr style=\"\">\n" +
				"       <td padding: 20px 0px;\">" +
				"           <a href=\"http://localhost:8080/confirmation/" + token + "\" style=\"margin:0 0 10px 0; " +
				"           background: #02ad88; color: #fff; padding: 13px; text-decoration: none; font-weight:" +
				"           600;display: inline-block;\">Verify Email Address</a>\n" +
				"       </td>\n" +
				"   </tr>\n" +
				"</table>" +
				footer + "</html>";
	}
}
