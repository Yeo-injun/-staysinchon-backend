package com.project.sinchon.config.security.jwt;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.sinchon.config.security.PrincipalDetails;
import com.project.sinchon.config.security.auth.User;
import com.project.sinchon.dao.UserDAO;

/* 
 * 권한, 인증이 필요한 특정 주소로 접근할 경우 위 필터가 동작 : SecurityConfig에서 addFilter() 해주어야 함.
 * BasicAuthenticationFilter 상속받음
 * 
 * 참고자료 : https://velog.io/@sa833591/Spring-Security-5-Spring-Security-Filter-%EC%A0%81%EC%9A%A9
 */

// JWT 유효성 검증 작업 수행 : 인증된 사용자인지 검증하고, 인증 후에는 사용자의 권한 확인
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private UserDAO userDAO;
	
	// SecurityConfig에서 필터 사용시 생성자로 선언해줌. Filter에서 사용할 객체들을 인자값으로 넣어주기.
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDAO userDAO) {
		super(authenticationManager);
		this.userDAO = userDAO; // JwtAuthorizationFilter 생성시 UserDAO 의존성을 주입받아서 필드에 할당
	}

	// 인증 및 권한이 필요한 URL 접근히 해당 메소드 동작
	@Override
	protected void doFilterInternal(HttpServletRequest request
								  , HttpServletResponse response
								  , FilterChain chain)
			throws IOException, ServletException {
		System.out.println("인증 및 권한이 필요한 URL요청이 들어옴");
		
		// 요청 Header의 Authorization값 가져오기 : null이거나 제대로된 값이 들어오지 않을 경우 유효성 검증 미수행
		String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("jwtHeader : " + jwtHeader);
		
		if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		// 요청 Header의 JWT토큰 유효성 검증
		String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, ""); // 요청 Header에 있는 jwtToken 가져오기 
		String user_ID = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build() // jwt토큰 복호화를 위한 객체를 만들어서
							.verify(jwtToken)										  // 요청 Header에서 가져온 jwtToken을 검증하고
							.getClaim("user_ID").asString();						  // 정상적인 토큰일 경우, 토큰에 담긴 user_ID 값을 가져옴
		System.out.println("_____________user_ID : " + user_ID);
		
		/* 서명이 정상작동할 경우
		 * 1.권한 처리를 위한 Token생성 후 
		 * 2.Authentication 객체에 담아주고 
		 * 3.SecurityContextHolder를 통해 세션에 Authentication객체 저장
		 */
		if (user_ID != null) {
			// user_ID로 DB에 있는 User정보를 가져오고
			User user = userDAO.findByUserId(user_ID);
			System.out.println("___________user : " + user.toString());
			
			// User객체를 인자값으로 넣어서 PricipalDetails객체 생성
			// Security에서 권한처리를 수행하기 위해 토큰을 새로 만들어서 Authentication객체를 새로 생성
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			
			
			Authentication authentication = new UsernamePasswordAuthenticationToken
												(principalDetails, //  user에 대한 정보를 토큰에 넣어주어 Contoller에서 DI해주기 편함
												 null, // 인증이 아닌 권한 처리를 위한 작업이므로 Password는 null처리
												 principalDetails.getAuthorities());

			// 생성된 Authentication 객체를 세션에 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println("______________다음 필터로!");
		}
		
		//이후 다음 필터 실행
		chain.doFilter(request, response);
		
	}
}
