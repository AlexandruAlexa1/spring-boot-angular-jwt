package com.aa.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.aa.domain.Role;
import com.aa.service.RoleService;

@WebMvcTest
public class RoleRestControllerTest {
	
	private static final String URI = "http://localhost:8080/api/v1/roles";

	@MockBean
	private RoleService service;
	
	@Autowired 
	private MockMvc mockMvc;
	
	@Test
	void listAll() throws Exception {
		Role admin = new Role("Admin", "Manage everithing");
		Role manager = new Role("Admin", "List users, add new users & update users");
		
		when(service.findAll()).thenReturn(List.of(admin, manager));
		
		this.mockMvc.perform(get(URI))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()", is(2)));
	}
}




