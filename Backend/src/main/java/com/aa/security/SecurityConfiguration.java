package com.aa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aa.constant.SecurityConstant;
import com.aa.filter.JWTAccessDeniedHandler;
import com.aa.filter.JWTAuthenticationEntryPoint;
import com.aa.filter.JWTAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired private UserDetailsService userDetailsService;
	@Autowired private JWTAuthorizationFilter jwtAuthorizationFilter;
	@Autowired private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	@Autowired private JWTAccessDeniedHandler jwtAccessDeniedHandler;
	@Autowired private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeRequests()
			.antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
			.antMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("Admin")
			.antMatchers(HttpMethod.POST, "/api/v1/users").hasAnyAuthority("Admin", "Manager")
			.antMatchers(HttpMethod.PUT, "/api/v1/users").hasAnyAuthority("Admin", "Manager", "Editor")
			
			.antMatchers(HttpMethod.GET, "/api/v1/roles").hasAnyAuthority("Admin", "Manager", "Editor")
			
			.antMatchers(HttpMethod.GET, "/api/v1/settings").hasAuthority("Admin")
			.antMatchers(HttpMethod.PUT, "/api/v1/settings").hasAuthority("Admin")
			.anyRequest().authenticated()
			.and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.and()
			.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
			.and()
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	
}
