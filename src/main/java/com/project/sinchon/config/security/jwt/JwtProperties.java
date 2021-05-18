package com.project.sinchon.config.security.jwt;

public interface JwtProperties {
	String SECRET = "staysinchon"; // 우리 서버만 알고 있는 비밀값
	long EXPIRATION_TIME = 30 * 60 * 1000L; // 1000밀리초 == 1초, 60 * 1000L == 1분
	String TOKEN_PREFIX = "Bearer ";
	String HEADER_STRING = "Authorization";
}