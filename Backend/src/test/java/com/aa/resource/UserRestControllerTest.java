package com.aa.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import com.aa.domain.Address;
import com.aa.domain.Role;
import com.aa.domain.User;
import com.aa.filter.JWTAccessDeniedHandler;
import com.aa.filter.JWTAuthenticationEntryPoint;
import com.aa.service.RoleService;
import com.aa.service.SettingService;
import com.aa.service.UserService;
import com.aa.utility.JWTProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
@MockBeans(value = {
		@MockBean(AuthenticationManager.class),
		@MockBean(JWTProvider.class),
		@MockBean(JWTAuthenticationEntryPoint.class),
		@MockBean(JWTAccessDeniedHandler.class),
		@MockBean(RoleService.class),
		@MockBean(SettingService.class),
})
public class UserRestControllerTest {

	private static final String URI = "/api/v1/users";

	@MockBean
	private UserService service;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired 
	private ObjectMapper objectMapper;

	private User user_1;
	private User user_2;
	
	@BeforeEach
	void init() {
		Address address = new Address("city", "state", "country", "postalCode", "phoneNumber");
		Role role = new Role(1);
		
		user_1 = new User("admin@yahoo.com", "password", "Admin", "Admin", new Date(), new Date(), true, true, address, role);
		user_2 = new User("manager@yahoo.com", "password", "firstName", "lastName", new Date(), new Date(), true, true, address, role);
	}
	
	@Test
	void add() throws Exception {
		when(service.save(any(User.class))).thenReturn(user_1);
		
		this.mockMvc.perform(post(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user_1)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.email", is(user_1.getEmail())))
		.andExpect(jsonPath("$.password", is(user_1.getPassword())))
		.andExpect(jsonPath("$.firstName", is(user_1.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(user_1.getLastName())));
	}
	
	@Test
	void listAll() throws Exception {
		int pageNum = 0;
		int pageSize = 5;
		
		List<User> list = new ArrayList<>();
		list.add(user_1);
		list.add(user_2);
		
		Page<User> pageImpl = new PageImpl<>(list);
		when(service.findAll(pageNum, pageSize)).thenReturn(pageImpl);
		
		mockMvc.perform(get(URI + "?pageNum=" + pageNum + "&pageSize=" + pageSize))
			.andExpect(status().isOk());
	}
	
	@Test
	void getUser() throws Exception {
		when(service.get(anyInt())).thenReturn(user_1);
		
		this.mockMvc.perform(get(URI + "/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.email", is(user_1.getEmail())))
			.andExpect(jsonPath("$.firstName", is(user_1.getFirstName())));
			
	}
	
	
	@Test
	void update() throws Exception {
		when(service.save(any(User.class))).thenReturn(user_1);
		
		this.mockMvc.perform(put(URI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user_1)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", is(user_1.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(user_1.getLastName())));
	}
	
	
	@Test
	void deleteUser() throws Exception {
		doNothing().when(service).delete(anyInt());
		
		this.mockMvc.perform(delete(URI + "/{id}", 1))
			.andExpect(status().isNoContent());
	}
}














