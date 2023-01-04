package com.aa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.aa.domain.Address;
import com.aa.domain.Role;
import com.aa.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repo;
	
	private User firstUser;
	private User secondUser;

	@BeforeEach
	void init() {
		Address address = new Address("city", "state", "country", "postalCode", "phoneNumber");
		Role role = new Role(1);
		
		firstUser = new User("admin@yahoo.com", "password", "Admin", "Admin", new Date(), new Date(), true, true, address, role);
		secondUser = new User("manager@yahoo.com", "password", "firstName", "lastName", new Date(), new Date(), true, true, address, role);
	}
	
	@Test
	void save() {
		Set<Role> listRoles = secondUser.getRoles();
		listRoles.add(new Role(2));
		listRoles.add(new Role(3));
		listRoles.add(new Role(4));
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = this.firstUser.getPassword();
		String encodedPassword = passwordEncoder.encode(password);
		
		firstUser.setNotLocked(true);
		firstUser.setPassword(encodedPassword);
		
		User savedUser = repo.save(firstUser);
		
		assertNotNull(savedUser);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	void listAll() {
		List<User> listUsers = repo.findAll();
		
		assertNotNull(listUsers);
		assertThat(listUsers.size()).isGreaterThan(0);
		
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	void listByPage() {
		int pageNum = 0;
		int pageSize = 5;
		
		Page<User> page = repo.findAll(PageRequest.of(pageNum, pageSize));
		List<User> listUsers = page.getContent();
		
		assertNotNull(listUsers);
		assertThat(listUsers.size()).isGreaterThan(0);
		
		listUsers.forEach(user -> System.err.println(user));
	}
	
	@Test
	void get() {
		Integer id = 1;
		
		User findedUser = repo.findById(id).get();
		
		assertNotNull(findedUser);
	}
	
	@Test
	void update() {
		String password = "password";
		
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(password);
		
		User findedUser = repo.findById(1).get();
		findedUser.setPassword(encodedPassword);
		
		User updatedUser = repo.save(findedUser);
		
		assertThat(updatedUser.getPassword()).isEqualTo(findedUser.getPassword());
	}
	
	@Test
	void delete() {
		Integer id = 22;
		
		repo.deleteById(id);
		
		Optional<User> findedUser = repo.findById(id);
		
		assertThat(!findedUser.isPresent());
	}
	
	@Test
	void findByEmail() {
		String email = "aa@yahoo.com";
		
		User findedUser = repo.findByEmail(email);
		
		assertNotNull(findedUser);
		assertEquals(email, findedUser.getEmail());
	}
	
	@Test
	void search() {
		String keyword = "editor";
		
		List<User> listUsers = repo.search(keyword);
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertNotNull(listUsers);
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}




















