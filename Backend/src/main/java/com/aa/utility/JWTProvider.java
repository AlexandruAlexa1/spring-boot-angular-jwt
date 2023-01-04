package com.aa.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.aa.constant.SecurityConstant;
import com.aa.domain.UserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTProvider {
	
	@Value("${jwt.secret}")
	private String secret;

	public String generateJWT(UserPrincipal userPrincipal) {
		String[] claims = getClaims(userPrincipal);
		
		return JWT.create()
				.withSubject(userPrincipal.getUsername())
				.withArrayClaim(SecurityConstant.AUTHORITIES, claims)
				.withIssuer(SecurityConstant.ISSUER)
				.withAudience(SecurityConstant.AUDIENCE)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret));
	}

	private String[] getClaims(UserPrincipal userPrincipal) {
		List<String> authorities = new ArrayList<>();
		
		for (GrantedAuthority gratedAuthority : userPrincipal.getAuthorities()) {
			authorities.add(gratedAuthority.getAuthority());
		}
		
		return authorities.toArray(new String[0]);
	}

	public String getSubject(String token) {
		return getJWTVerifier().verify(token).getSubject();
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier jwtVerifier;
		
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			jwtVerifier = JWT.require(algorithm).withIssuer(SecurityConstant.ISSUER).build();
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
		}
		
		return jwtVerifier;
	}

	public boolean isTokenValid(String email, String token) {
		JWTVerifier verifier = getJWTVerifier();
		
		if (!email.isEmpty() && !isTokenExpired(verifier, token)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration = verifier.verify(token).getExpiresAt();
		
		return expiration.before(new Date());
	}

	public List<GrantedAuthority> getAuthorities(String token) {
		String[] claims = getClaimsFromTocken(token);
		
		List<GrantedAuthority> listAuthorities = new ArrayList<>();
		
		for (String claim : claims) {
			listAuthorities.add(new SimpleGrantedAuthority(claim));
		}
				
		return listAuthorities;
	}

	private String[] getClaimsFromTocken(String token) {
		JWTVerifier jwtVerifier = getJWTVerifier();
		
		return jwtVerifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
	}

	public Authentication getAuthentication(String email, List<GrantedAuthority> authorities,
			HttpServletRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, request, authorities);
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		return authenticationToken;
	}
}
