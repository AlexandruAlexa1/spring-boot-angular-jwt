package com.aa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aa.domain.Setting;
import com.aa.domain.SettingCategory;

public interface SettingRepository extends JpaRepository<Setting, String> {

	public List<Setting> findByCategory(SettingCategory settingCategory);
}
