package com.aa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.aa.domain.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository repo;
	
	@Test
	void save() {
		Role admin = new Role("Admin", "Manage everything");
		
		Role savedRole = repo.save(admin);
		
		assertNotNull(savedRole);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	void saveAll() {
		Role manager = new Role("Manager", "List, edit and add new users");
		Role editor = new Role("Editor", "List and edit users");
		Role user = new Role("User", "List users");
		
		List<Role> savedRoles = repo.saveAll(List.of(manager, editor, user));
		
		assertNotNull(savedRoles);
		assertEquals(3, savedRoles.size());
	}
	
	@Test
	void listAll() {
		List<Role> listRoles = repo.findAll();
		
		assertEquals(4, listRoles.size());
	}
}
