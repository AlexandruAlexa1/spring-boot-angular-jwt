package com.aa.filter;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.aa.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JWTAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2)
			throws IOException {
		
		HttpResponse httpResponse = new HttpResponse(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(),
				HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase());
		
		response.setContentType("APPLICATION_JSON_VALUE");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(outputStream, httpResponse);
		
		outputStream.flush();
	}

	
}
