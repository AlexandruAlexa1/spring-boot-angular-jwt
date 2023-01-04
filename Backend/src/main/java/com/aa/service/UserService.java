package com.aa.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aa.domain.Role;
import com.aa.domain.User;
import com.aa.domain.UserPrincipal;
import com.aa.exception.DuplicateEmailException;
import com.aa.exception.UserNotFoundException;
import com.aa.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired private UserRepository repo;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private EmailService emailService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repo.findByEmail(email);
		
		if (user == null) {
			throw new UsernameNotFoundException("User not found by E-mail: " + email);
		} else {
			user.setLastLoginDate(user.getLastLoginDate());
			
			return new UserPrincipal(user);
		}
	}
	
	public Page<User> findAll(int pageNum, int pageSize) {
		return repo.findAll(PageRequest.of(pageNum, pageSize));
	}
	
//	public List<User> findAll(int pageNum) {
//		Pageable pageable = PageRequest.of(pageNum, USERS_PER_PAGE);
//		Page<User> page = repo.findAll(pageable);
//		
//		return page.getContent();
//	}
	
//	public List<User> listAll() {
//		return repo.findAll();
//	}
	
	public User get(Integer id) throws UserNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Cloud not find any user with ID: " + id);
		}
	}

	public User save(User userInForm) throws DuplicateEmailException {
		boolean isEditMode = (userInForm.getId() != null);
		
		if (isEditMode) {
			System.out.println("Edit Mode");
			User userInDB = repo.findById(userInForm.getId()).get();
			
			checkEmail(userInForm, userInDB);
			checkPassword(userInForm, userInDB);
		} else {
			checkDuplicateEmail(userInForm);
			encodePassword(userInForm);
		}
		
		return repo.save(userInForm);
	}
	
	private void checkPassword(User userInForm, User userInDB) {
		boolean isPasswordEmpty = (userInForm.getPassword().isEmpty());
		
		if (isPasswordEmpty) {
			System.out.println("Password is Empty");
			

			userInForm.setPassword(userInDB.getPassword());
			
			System.out.println("Password is Empty password: " + userInForm.getPassword());
		} else {
			System.out.println("Password was changed");
			encodePassword(userInForm);
			
			System.out.println("Password was changed: " + userInForm.getPassword());
		}
	}

	private void checkEmail(User userInForm, User userInDB) throws DuplicateEmailException {
		boolean isTheSameEmail = (userInDB.getEmail().contentEquals(userInForm.getEmail()));
		
		if (isTheSameEmail) {
			System.out.println("Email is the same");
			userInForm.setEmail(userInDB.getEmail());
		} else {
			System.out.println("Email has been changed");
			checkDuplicateEmail(userInForm);
		}
	}
	
	private void checkDuplicateEmail(User userInForm) throws DuplicateEmailException {
		User userInDB = repo.findByEmail(userInForm.getEmail());
		
		if (userInDB != null) {
			throw new DuplicateEmailException("This E-mail: " + userInForm.getEmail() + " already exist. Please choose another E-mail!");
		}
	}

	private void encodePassword(User user) {
		String passwordEncoded = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordEncoded);
	}
	
	public void delete(Integer id) throws UserNotFoundException {
		if (!repo.existsById(id)) {
			throw new UserNotFoundException("Cloud not find any user with ID: " + id);
		}
		
		repo.deleteById(id);
	}
	
	public User findByEmail(String email) {
		return repo.findByEmail(email);
	}
	
	public List<User> search(String keyword) {
		return repo.search(keyword);
	}

	public User register(User user) throws DuplicateEmailException {
		checkDuplicateEmail(user);
		encodePassword(user);
		
		user.setJoinDate(new Date());
		user.setEnabled(true);
		user.setNotLocked(true);
		user.addRole(new Role(4));
		
		emailService.sendEmail(user.getEmail());
		
		return repo.save(user);
	}

}
