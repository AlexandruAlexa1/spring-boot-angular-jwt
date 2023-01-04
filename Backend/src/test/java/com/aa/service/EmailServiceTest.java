package com.aa.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
	
	@InjectMocks
	private EmailService emailService;

	@Mock
	private SettingService settingService;
	
	@Test
	void sendEmail() {
		when(settingService.getEmailSettings()).thenReturn(settingService.getEmailSettings());
		
		doNothing().when(emailService).sendEmail(null);
	}
}
