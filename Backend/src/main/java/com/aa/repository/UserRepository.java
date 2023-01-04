package com.aa.repository; 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aa.domain.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.firstName LIKE ?1 "
			+ "OR u.lastName LIKE ?1 "
			+ "OR u.email LIKE ?1")
	public List<User> search(String keyword);
}
