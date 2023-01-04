package com.aa.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import com.aa.domain.Address;
import com.aa.domain.AuthRequest;
import com.aa.domain.Role;
import com.aa.domain.User;
import com.aa.filter.JWTAccessDeniedHandler;
import com.aa.filter.JWTAuthenticationEntryPoint;
import com.aa.service.RoleService;
import com.aa.service.SettingService;
import com.aa.service.UserService;
import com.aa.utility.JWTProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@MockBeans(value = {
		@MockBean(AuthenticationManager.class),
		@MockBean(UserService.class),
		@MockBean(JWTProvider.class),
		@MockBean(JWTAuthenticationEntryPoint.class),
		@MockBean(JWTAccessDeniedHandler.class),
		@MockBean(RoleService.class),
		@MockBean(SettingService.class),
})
public class AuthControllerTest {
	
	@MockBean private UserService userService;
	
	@Autowired MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	private User user_1;
	
	@BeforeEach
	void beforeEach() {
		Address address = new Address("city", "state", "country", "postalCode", "phoneNumber");
		Role role = new Role(1);
		
		user_1 = new User("admin@yahoo.com", "password", "Admin", "Admin", new Date(), new Date(), true, true, address, role);
	}
	
	@Test
	void login() throws Exception {
		when(userService.findByEmail(anyString())).thenReturn(user_1);
		
		AuthRequest authRequest = new AuthRequest("admin@yahoo.com", "password");
		
		mockMvc.perform(post("/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(authRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is(authRequest.getEmail())))
		.andExpect(jsonPath("$.password", is(authRequest.getPassword())));
	}
	
	@Test
	void register() throws JsonProcessingException, Exception {
		when(userService.register(any(User.class))).thenReturn(user_1);
		
		mockMvc.perform(post("/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user_1)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is(user_1.getEmail())))
		.andExpect(jsonPath("$.password", is(user_1.getPassword())));
	}
	
	
}
