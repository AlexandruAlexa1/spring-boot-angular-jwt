package com.aa.constant;

public class SecurityConstant {
	public static final String[] PUBLIC_URLS = { "/auth/login", "/auth/register" };
	public static final String ISSUER = "AA";
	public static final String AUDIENCE = "User Management";
	public static final long EXPIRATION_TIME = 432_000_000; // 5 days
	public static final String AUTHORITIES = "authorities";
	public static final String JWT_HEADER = "JWT";
	public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
	public static final String JWT_PREFIX = "Bearer ";
	public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
}
