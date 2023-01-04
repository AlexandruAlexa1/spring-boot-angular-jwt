package com.aa.service;

import static com.aa.constant.EmailConstant.SMTP_AUTH;
import static com.aa.constant.EmailConstant.SMTP_HOST;
import static com.aa.constant.EmailConstant.SMTP_PORT;
import static com.aa.constant.EmailConstant.SMTP_STARTTLS_ENABLE;
import static com.aa.constant.EmailConstant.SMTP_STARTTLS_REQUIRED;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.utility.EmailSettingBag;

@Service
public class EmailService {
	
	@Autowired private SettingService settingService;
		
	public void sendEmail(String email) {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		try {
			MimeMessage message = new MimeMessage(getSession(emailSettings));
			message.setFrom(new InternetAddress(emailSettings.getFromAddress()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject(emailSettings.getSubject());
			message.setText(emailSettings.getContent());
			message.setSentDate(new Date());
			message.saveChanges();
			
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	private Session getSession(EmailSettingBag emailSettings) {
		Properties properties = System.getProperties();
		properties.put(SMTP_HOST, emailSettings.getHost());
		properties.put(SMTP_PORT, emailSettings.getPort());
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_STARTTLS_ENABLE, true);
		properties.put(SMTP_STARTTLS_REQUIRED, true);
		
		Session session = Session.getInstance(properties, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(emailSettings.getUsername(), emailSettings.getPassword());
		    }
		});
		
		return session;
	}
	
}
