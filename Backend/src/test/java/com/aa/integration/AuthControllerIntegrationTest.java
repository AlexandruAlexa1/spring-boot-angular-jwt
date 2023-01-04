package com.aa.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.aa.domain.AuthRequest;
import com.aa.domain.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

	@LocalServerPort
	private int port;
	
	private String url = "http://localhost";
	
	private  static RestTemplate restTemplate;
	
	@BeforeAll
	static void init() {
		restTemplate = new RestTemplate();
	}
	
	@BeforeEach
	void beforeEach() {
		url = url + ":" + port + "/auth";
	}
	
	@Test
	void login() {
		AuthRequest authRequest = new AuthRequest("admin@yahoo.com", "password");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<AuthRequest> entity = new HttpEntity<>(authRequest, headers);
		
		User authUser = restTemplate.postForObject(url + "/login", entity, User.class);
		
		assertNotNull(authUser);
		assertEquals(authRequest.getEmail(), authUser.getEmail());
	}
	
	@Test
	void register() {
		User user = new User("aa@yahoo.com", "password", "First Name", "Last Name", new Date(), new Date(), true, true, null, null);
		
		User registeredUser = restTemplate.postForObject(url + "/register", user, User.class);
		
		assertNotNull(registeredUser);
		assertEquals(user.getEmail(), registeredUser.getEmail());
		assertEquals(user.getFirstName(), registeredUser.getFirstName());
	}
}












