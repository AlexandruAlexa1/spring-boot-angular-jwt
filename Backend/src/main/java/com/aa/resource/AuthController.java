package com.aa.resource;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aa.constant.SecurityConstant;
import com.aa.domain.AuthRequest;
import com.aa.domain.User;
import com.aa.domain.UserPrincipal;
import com.aa.exception.DuplicateEmailException;
import com.aa.service.UserService;
import com.aa.utility.JWTProvider;

@RestController 
public class AuthController {

	@Autowired private AuthenticationManager authenticationManager;
	@Autowired private UserService userService;
	@Autowired private JWTProvider jwtProvider;
	
	
	@PostMapping("/auth/login")
	public ResponseEntity<User> login(@RequestBody @Valid AuthRequest request) {
		authenticate(request.getEmail(), request.getPassword());
		
		User logedUser = userService.findByEmail(request.getEmail());
		
		HttpHeaders jwtHeader = getJWTHeader(new UserPrincipal(logedUser));
		
		return new ResponseEntity<>(logedUser, jwtHeader, HttpStatus.OK);
	}

	private HttpHeaders getJWTHeader(UserPrincipal userPrincipal) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstant.JWT_HEADER, jwtProvider.generateJWT(userPrincipal));
		
		return headers;
	}

	private void authenticate(String email, String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}


	@PostMapping("/auth/register")
	public ResponseEntity<User> register(@RequestBody @Valid User user) throws DuplicateEmailException {
		return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
	}
}






