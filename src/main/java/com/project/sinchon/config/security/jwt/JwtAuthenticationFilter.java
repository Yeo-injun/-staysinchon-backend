package com.project.sinchon.config.security.jwt;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.sinchon.config.security.PrincipalDetails;
import com.project.sinchon.config.security.auth.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;

/* 참고 영상 : https://www.youtube.com/watch?v=E5brrYYjrwI&list=PL93mKxaRDidERCyMaobSLkvSPzYtIk0Ah&index=24

   /login Request가 들어오면 
 * 1. UsernamePasswordAuthenticationFilter가 로그인 정보 확인(사용자 입력값 - DB값)
 * 2. 로그인 정보가 유효할 경우 토큰 생성 (JwtTokenProvider 클래스로 생성)
 * 3. 생성된 토큰은 Authentication 객체에 담아주고 이를 반환
 * 
 */

/* 
 * Username/Password Authentication에서 지원하는 Storage방식 4가지 
 * 1. 인메모리 인증 방식:Simple Storage with In-Memory Authentication
 * 2. JDBC관계형 DB 인증 방식 : Relational Databases with JDBC Authentication
 * 3. 커스텀 데이터 인증 방식 : Custom data stores with UserDetailsService (구현!)
 * 4. LDAP 스토리지 인증 방식 : LDAP storage with LDAP Authentication 
 * 
 * 공식문서 URL> https://docs.spring.io/spring-security/site/docs/5.4.6/reference/html5/#servlet-authentication-unpwd)
 */ 
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	/* SecurityConfig에서 JwtAuthenticationFilter를 등록할 때 AuthenticationManager를 인자로 넘겨주어야 함.
	 * AuthenticationManager는 UserDetailsService 인터페이스를 구현하는 클래스의 loadUserByUsername() 함수를 호출.
	 * 이를 통해 로그인 정보를 검증하고, 유효할 경우 발행된 토큰을 Authentication 객체에 넣어서 User 정보와 함께 반환
	 */
	private final AuthenticationManager authenticationManager;
	
	// /login 요청시에 실행되는 함수 : Authentication 객체를 리턴해줌. AuthenticationManager를 호출해서 로그인 검증 및 토큰 발행 처리
	@Override
	public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 진입");
		
		ObjectMapper om = new ObjectMapper();
		User user = null;
		try {
			// STEP1. 로그인 정보 가져오기 : request받은 정보(user_ID와 pwd)를 파싱해서 자바 Object로 받기 (JSON 데이터 파싱)
			user = om.readValue(request.getInputStream(), User.class);
			System.out.println("JwtAuthenticationFilter : " + user.getUser_ID());
	
			
			// STEP2. UsernamePasswordAuthenticationToken 생성 : Security Config에서 .formlogin이 비활성화 되어 있기 때문에 새로 등록해줘야 함
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUser_ID(), user.getPwd());
			System.out.println(authToken.toString());
			System.out.println("Token ++++++++++++++++++++++++");
			
			// STEP3. AuthenticationManager를 호출해서 PrincipalDetailsService.loadUserByUsername() 함수 실행
			Authentication auth = authenticationManager.authenticate(authToken);
			System.out.println(auth.toString());

			// Result. Authentication 객체가 session 영역에 저장 ->> 로그인 정상 처리됨 
			return auth;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/*  
	 * Authentication 성공시 response Header에 JWT Token 담아주기
	 */
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,Authentication authResult) 
											throws IOException, ServletException {
		System.out.println("________________ 인증 정상 처리됨!");
		PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject(principalDetailis.getUsername()) // 토큰 이름 설정
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME)) // 토큰 만료 시간 설정
				.withClaim("user_ID", principalDetailis.getUser().getUser_ID()) // 사용자 ID값 Authentication객체에 담아주기
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	
	}
}