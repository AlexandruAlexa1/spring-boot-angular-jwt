package com.aa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aa.domain.Role;
import com.aa.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

	@InjectMocks
	private RoleService service;
	
	@Mock
	private RoleRepository repo;
	
	@Test
	void findAll() {
		Role admin = new Role("Admin", "Manage everithing");
		Role manager = new Role("Admin", "List users, add new users & update users");
		
		when(repo.findAll()).thenReturn(List.of(admin, manager));
		
		List<Role> listRoles = service.findAll();
		
		assertEquals(2, listRoles.size());
	}
}
