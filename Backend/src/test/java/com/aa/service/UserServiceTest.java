package com.aa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.aa.domain.Address;
import com.aa.domain.Role;
import com.aa.domain.User;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.UserNotFoundException;
import com.aa.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@InjectMocks
	private UserService service;
	
	@Mock
	private UserRepository repo;

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
	void save() throws DuplicateEmailException {
		when(repo.save(any(User.class))).thenReturn(user_1);
		
		User savedUser = service.save(user_1);
		
		assertNotNull(savedUser);
		assertEquals("aa@yahoo.com", savedUser.getEmail());
	}
	
	@Test
	void findAll() {
		int pageNum = 0;
		int pageSize = 5;
		
		List<User> list = new ArrayList<>();
		list.add(user_1);
		list.add(user_2);
		
		Page<User> pageImpl = new PageImpl<>(list);
		
		when(repo.findAll(PageRequest.of(pageNum, pageSize))).thenReturn(pageImpl);
		
		Page<User> page = service.findAll(pageNum, pageSize);
		
		List<User> listUsers = page.getContent();
		
		assertNotNull(listUsers);
		assertEquals(2, listUsers.size());
		
		listUsers.forEach(user -> System.err.println(user));
	}
	
	
	@Test
	void get() throws UserNotFoundException {
		when(repo.findById(anyInt())).thenReturn(Optional.of(user_1));
		
		User findedUser = service.get(10);
		
		assertNotNull(findedUser);
		assertEquals("aa@yahoo.com", findedUser.getEmail());
	}
	
	@Test
	void getForException() throws UserNotFoundException {
		when(repo.findById(1)).thenReturn(Optional.of(user_1));
		
		assertThrows(Exception.class, () -> {
			service.get(10);
		});
	}
	
	@Test
	void delete() throws UserNotFoundException {
		when(repo.existsById(anyInt())).thenReturn(true);
		
		doNothing().when(repo).deleteById(anyInt());
		
		service.delete(1);
		
		verify(repo, times(1)).deleteById(1);
	}
	
	@Test
	void search() {
		String keyword = "Admin";

		when(repo.search(anyString())).thenReturn(List.of(user_1, user_2));
		
		List<User> listUsers = service.search(keyword );
		
		assertNotNull(listUsers);
		assertEquals(2, listUsers.size());
	}
}















