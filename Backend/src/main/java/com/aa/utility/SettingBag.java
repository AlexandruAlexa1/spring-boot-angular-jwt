package com.aa.utility;

import java.util.List;

import com.aa.domain.Setting;

public class SettingBag {
	
	private List<Setting> listSettings;
	
	public SettingBag(List<Setting> listSettings) {
		this.listSettings = listSettings;
	}
	
	public Setting get(String key) {
		
		for (Setting setting : listSettings) {
			// check if one of the settings contains the given key
			if (setting.getKey().contains(key)) {
			// if contains the given key return that setting
				return setting;
			}
		}
		
		// else return null
		return null;
	}
	
	public String getValue(String key) {
		Setting setting = get(key);
		
		// Check if setting exist
		if (setting != null) {
			// If exist return his valu as String
			return setting.getValue();
		}
		
		// Else return null
		return null;
	}
}
