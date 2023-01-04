package com.aa.utility;

import java.util.List;

import com.aa.domain.Setting;

public class EmailSettingBag extends SettingBag {

	public EmailSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	
	public String getHost() {
		return super.getValue("SMTP_HOST");
	}

	public Integer getPort() {
		return Integer.parseInt(super.getValue("SMTP_PORT"));
	}

	public String getUsername() {
		return super.getValue("USERNAME");
	}
	
	public String getPassword() {
		return super.getValue("PASSWORD");
	}
	
	public String getFromAddress() {
		return super.getValue("EMAIL_FROM");
	}
	
	public String getSubject() {
		return super.getValue("EMAIL_SUBJECT");
	}
	
	public String getContent() {
		return super.getValue("EMAIL_CONTENT");
	}
	
	
	
//	public String getServer() {
//		return super.getValue("SMTP_SERVER");
//	}
	
//	public String getSmtpStarttlsEnabled() {
//		return super.getValue("SMTP_STARTTLS_ENABLED");
//	}
//	
//	public String getSmtpStarttlsRequired() {
//		return super.getValue("SMTP_STARTTLS_REQUIRED");
//	}
////	
//	public String getDefaultFort() {
//		return super.getValue("SMTP_DEFAULT_PORT");
//	}
	
	
	
//	public String getSenderName() {
//		return super.getValue("SENDER_NAME");
//	}
}
