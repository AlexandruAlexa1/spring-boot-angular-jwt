package com.aa.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aa.domain.Setting;
import com.aa.service.SettingService;

@RestController
@RequestMapping("/api/v1/settings")
public class SettingRestController {
	
	@Autowired
	private SettingService service;

	@GetMapping
	public ResponseEntity<List<Setting>> listAll() {
		List<Setting> listSettings = service.findAll();
		
		if (listSettings.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		return new ResponseEntity<>(listSettings, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Setting> update(@RequestBody Setting setting) {
		Setting updatedSetting = service.update(setting);
		
		return new ResponseEntity<>(updatedSetting, HttpStatus.OK);
	}
}
