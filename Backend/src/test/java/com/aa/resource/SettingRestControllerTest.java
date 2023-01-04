package com.aa.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import com.aa.domain.Setting;
import com.aa.domain.SettingCategory;
import com.aa.filter.JWTAccessDeniedHandler;
import com.aa.filter.JWTAuthenticationEntryPoint;
import com.aa.service.RoleService;
import com.aa.service.SettingService;
import com.aa.utility.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@MockBeans(value = { 
		@MockBean(JWTProvider.class),
		@MockBean(AuthController.class),
		@MockBean(RoleService.class),
		@MockBean(UserRestController.class),
		@MockBean(UserDetailsService.class),
		@MockBean(JWTAuthenticationEntryPoint.class),
		@MockBean(JWTAccessDeniedHandler.class),
})
public class SettingRestControllerTest {
	
	private static final String URI = "http://localhost:8080/api/v1/settings";

	@MockBean
	private SettingService service;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	private Setting setting_1;
	private Setting setting_2;
	
	@BeforeEach
	void init() {
		setting_1 = new Setting("key", "value", SettingCategory.MAIL_SERVER);
		setting_2 = new Setting("key", "value", SettingCategory.MAIL_SERVER);
	}
	
	@Test
	void listAll() throws Exception {
		List<Setting> listSettings = new ArrayList<>();
		listSettings.add(setting_1);
		listSettings.add(setting_2);
		
		when(service.findAll()).thenReturn(listSettings);
		
		this.mockMvc.perform(get("/api/v1/settings"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(listSettings.size())));
	}
	
	@Test
	void update() throws Exception {
		when(service.update(any(Setting.class))).thenReturn(setting_1);
		
		mockMvc.perform(put(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(setting_1)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.key", is(setting_1.getKey())))
		.andExpect(jsonPath("$.value", is(setting_1.getValue())))
		.andExpect(jsonPath("$.category", is(SettingCategory.MAIL_SERVER)));
	}
}














