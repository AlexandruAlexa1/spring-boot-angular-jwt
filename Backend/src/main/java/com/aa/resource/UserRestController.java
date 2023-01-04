package com.aa.resource;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aa.domain.User;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.UserNotFoundException;
import com.aa.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

	@Autowired
	private UserService service;
	
	@GetMapping()
	public ResponseEntity<Page<User>> listAll(@RequestParam Optional<Integer> pageNum, @RequestParam Optional<Integer> pageSize) {
		Page<User> page = service.findAll(pageNum.orElse(0), pageSize.orElse(5));
		
		if (page.getContent().isEmpty()) return ResponseEntity.noContent().build();
		
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> get(@PathVariable("id") Integer id) throws UserNotFoundException {
		
		return new ResponseEntity<>(service.get(id), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<User> add(@RequestBody @Valid User user) throws DuplicateEmailException {
		User savedUser = service.save(user);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).body(savedUser);
	}
	
	@PutMapping
	public ResponseEntity<User> update(@RequestBody @Valid User user) throws DuplicateEmailException {
		User updatedUser = service.save(user);
		
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws UserNotFoundException {
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
//	@GetMapping("/{keyword}")
//	public ResponseEntity<List<User>> search(@PathVariable("keyword") String keyword) {
//		List<User> listUsers = service.search(keyword);
//		
//		if (listUsers.isEmpty()) return ResponseEntity.noContent().build();
//		
//		return new ResponseEntity<>(listUsers, HttpStatus.OK);
//	}
}








