package com.aa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.annotation.Rollback;

import com.aa.domain.Setting;
import com.aa.domain.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired
	private SettingRepository settingRepo;
	
	@Test
	@Description("It should create E-mail settings.")
	void save() {
		List<Setting> settings = new ArrayList<>();
		Collections.addAll(settings,
				new Setting("SMTP_HOST", "smtp.gmail.com", SettingCategory.MAIL_SERVER),
				new Setting("SMTP_PORT", "587", SettingCategory.MAIL_SERVER),
				new Setting("USERNAME", "aa01asd@gmail.com", SettingCategory.MAIL_SERVER),
				new Setting("PASSWORD", "felupkzzpgdutpqg", SettingCategory.MAIL_SERVER),
				new Setting("EMAIL_FROM", "aa01asd@gmail.com", SettingCategory.MAIL_SERVER),
				new Setting("EMAIL_SUBJECT", "Confirm registration", SettingCategory.MAIL_TEMPLATE),
				new Setting("EMAIL_CONTENT", "You have been registred successfuly!", SettingCategory.MAIL_TEMPLATE)
				);
		
		settingRepo.saveAll(settings);
		
		List<Setting> listSettings = settingRepo.findAll();
		
		assertNotNull(listSettings);
		assertEquals(settings.size(), listSettings.size());
	}
	
	@Test
	void listAll() {
		List<Setting> listSetings = settingRepo.findAll();
		
		assertNotNull(listSetings);
		assertThat(listSetings.size()).isGreaterThan(0);
		
		listSetings.forEach(setting -> System.out.println(setting));
	}
	
	@Test
	void listByCategory() {
		SettingCategory category = SettingCategory.MAIL_TEMPLATE;
		
		List<Setting> listSettings = settingRepo.findByCategory(category);
		
		assertNotNull(listSettings);
		assertThat(listSettings.size()).isGreaterThan(0);
		listSettings.forEach(setting -> assertThat(setting.getCategory()).isEqualTo(category));
		
		listSettings.forEach(setting -> System.err.println(setting));
	}
	
}
