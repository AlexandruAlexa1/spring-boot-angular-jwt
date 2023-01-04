package com.aa.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aa.domain.HttpResponse;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String INCORECT_CREDENTIALS = "Username or password are incorect. Please try again";
	private static final String ACCESS_DENIED = "You do not have enough permision to access this page";

	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleUserNotFoundException(UserNotFoundException ex) {
		HttpResponse response = new HttpResponse(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
		HttpResponse response = new HttpResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleInternalServerException(Exception ex) {
		HttpResponse response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
		
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleBadCredentialsException(BadCredentialsException ex) {
		HttpResponse response = new HttpResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), INCORECT_CREDENTIALS);
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler
	public ResponseEntity<HttpResponse> handleAccessDeniedException(AccessDeniedException ex) {
		HttpResponse response = new HttpResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), ACCESS_DENIED);
		
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		
		List<String> listErrors = new ArrayList<>();
		
		for (FieldError fieldError : fieldErrors) {
			String errorMessage = fieldError.getDefaultMessage();
			listErrors.add(errorMessage);
		}
		
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("timeStamp", new Date());
		response.put("status", status);
		response.put("message", listErrors);
		
		return new ResponseEntity<>(response, headers, status);
	}
	
	
}
