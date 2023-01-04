package com.aa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aa.domain.Setting;
import com.aa.domain.SettingCategory;
import com.aa.repository.SettingRepository;
import com.aa.utility.EmailSettingBag;

@ExtendWith(MockitoExtension.class)
public class SettingServiceTest {

	@InjectMocks
	private SettingService settingService;
	
	@Mock
	private SettingRepository settingRepo;

	private Setting setting_1;
	private Setting setting_2;
	
	@BeforeEach
	void beforeEach() {
		setting_1 = new Setting("SMTP_HOST", "smtp.gmail.com", SettingCategory.MAIL_SERVER);
		setting_2 = new Setting("SMTP_PORT", "587", SettingCategory.MAIL_SERVER);
	}
	
	@Test
	void findAll() {
		List<Setting> settings = settingRepo.findAll();
		
		when(settingRepo.findAll()).thenReturn(settings);
		
		List<Setting> listSettings = settingService.findAll();
		
		assertNotNull(listSettings);
		assertEquals(settings.size(), listSettings.size());
	}
	
	@Test
	void update() {
		Setting setting = new Setting("EMAIL_ADDRESS", "", SettingCategory.MAIL_SERVER);
		
		when(settingService.update(any(Setting.class))).thenReturn(setting);
		
		Setting savedSetting = settingRepo.save(setting);
		savedSetting.setValue("@yahoo.gamil");
		
		Setting updatedSetting = settingService.update(savedSetting);
		
		assertEquals("@yahoo.gamil", updatedSetting.getValue());
	}
	
	@Test
	void getEmailSettings() {
		List<Setting> listSettings = new ArrayList<>();
		listSettings.add(setting_1);
		listSettings.add(setting_2);
		
		when(settingRepo.findByCategory(any(SettingCategory.class))).thenReturn(listSettings);
		
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		
		assertNotNull(emailSettings);
		assertEquals(setting_1.getValue(), emailSettings.getHost());
		assertEquals(Integer.valueOf(setting_2.getValue()), emailSettings.getPort());
	}
}












