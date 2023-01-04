package com.aa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aa.domain.Setting;
import com.aa.domain.SettingCategory;
import com.aa.repository.SettingRepository;
import com.aa.utility.EmailSettingBag;

@Service
public class SettingService {

	@Autowired 
	private SettingRepository settingRepo;
	
	public List<Setting> findAll() {
		return settingRepo.findAll();
	}
	
	public Setting update(Setting setting) {
		return settingRepo.save(setting);
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = settingRepo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepo.findByCategory(SettingCategory.MAIL_TEMPLATE));
		
		return new EmailSettingBag(settings);
	}
}
