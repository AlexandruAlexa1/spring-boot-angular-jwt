package com.aa.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.aa.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		HttpResponse httpResponse = new HttpResponse(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase().toUpperCase());
		
		response.setContentType("APPLICATION_JSON_VALUE");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		
		OutputStream outputStream = response.getOutputStream();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, httpResponse);
		
		outputStream.flush();
	}

}
