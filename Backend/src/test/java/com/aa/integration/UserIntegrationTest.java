package com.aa.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.aa.domain.Address;
import com.aa.domain.Role;
import com.aa.domain.User;
import com.aa.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

	private static final String URI = "http:localhost";

	@LocalServerPort
	private int port;

	private String baseUrl = URI;

	private static RestTemplate restTemplate;

	@Autowired
	private UserRepository repo;

	private User user_1;
	private User user_2;

	@BeforeAll
	static void init() {
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	void beforeSetup() {
		baseUrl = baseUrl + ":" + port + "/api/v1/users";

		Address address = new Address("city", "state", "country", "postalCode", "phoneNumber");
		Role role = new Role(1);

		user_1 = new User("admin@yahoo.com", "password", "Admin", "Admin", new Date(), new Date(), true, true, address,
				role);
		user_2 = new User("manager@yahoo.com", "password", "firstName", "lastName", new Date(), new Date(), true, true,
				address, role);
	}

	@AfterEach
	void afterSetup() {
		repo.deleteAll();
	}

	@Test
	void add() {
		User savedUser = restTemplate.postForObject(baseUrl, user_1, User.class);

		assertNotNull(savedUser);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	void listAll() {
		repo.saveAll(List.of(user_1, user_2));

		ResponseEntity<List<User>> response = restTemplate.exchange(baseUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<User>>() {
				});

		List<User> listUsers = response.getBody();

		assertNotNull(listUsers);
		assertEquals(2, listUsers.size());
	}

	@Test
	void get() {
		User savedUser = repo.save(user_1);

		User existingUser = restTemplate.getForObject(baseUrl + "/" + savedUser.getId(), User.class);

		assertNotNull(existingUser);
		assertEquals("Password", existingUser.getPassword());
	}

	@Test
	void getForException() {
		repo.save(user_1);

		assertThrows(Exception.class, () -> {
			restTemplate.getForObject(baseUrl + "/" + 2, User.class);
		});
	}

	@Test
	void update() {
		User savedUser = repo.save(user_1);
		savedUser.setEmail("alex@yahoo.com");

		restTemplate.put(baseUrl, savedUser);

		User existingUser = restTemplate.getForObject(baseUrl + "/" + savedUser.getId(), User.class);

		assertNotNull(existingUser);
		assertEquals("alex@yahoo.com", existingUser.getEmail());
	}

	@Test
	void delete() {
		repo.save(user_1);
		repo.save(user_2);

		restTemplate.delete(baseUrl + "/" + user_1.getId());

		int count = repo.findAll().size();

		assertEquals(1, count);
	}

	@Test
	void deleteForException() {
		repo.save(user_1);

		assertThrows(Exception.class, () -> {
			restTemplate.delete(baseUrl + "/" + 2);
		});
	}
}