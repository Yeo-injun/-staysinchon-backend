package com.project.sinchon.config.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/* 
 * 권한, 인증이 필요한 특정 주소로 접근할 경우 위 필터가 동작
 * BasicAuthenticationFilter 상속받음
 * 
 * 참고자료 : https://velog.io/@sa833591/Spring-Security-5-Spring-Security-Filter-%EC%A0%81%EC%9A%A9
 */

// JWT 유효성 검증 작업 수행 : 인증된 사용자인지 검증하고, 인증 후에는 사용자의 권한 확인
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}

}
