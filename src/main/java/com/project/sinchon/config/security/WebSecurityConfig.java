package com.project.sinchon.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.project.sinchon.config.security.jwt.JwtAuthenticationFilter;
import com.project.sinchon.config.security.jwt.JwtAuthorizationFilter;
import com.project.sinchon.dao.UserDAO;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDAO userDAO; // UserDAO를 필터에서 쓸 수 있도록 DI
	
    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        		.cors()
        	.and()
        		.csrf().disable() 			// csrf 보안 토큰 disable처리.               
        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용X
            .and()
            	.httpBasic().disable() 		// rest api 만을 고려하여 기본 설정은 해제
                .formLogin().disable() 		// 기본 /login은 작동하지 않음. /login을 작동시키기 위해서 새로운 Filter를 등록해야 함
                
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))	// /login을 동작시키기 위한 새로운 Filter 추가 (UsernamePasswordAuthenticationFilter)
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDAO))	// 인증 및 권한이 필요한 url에 접근히 동작하는 필터 

                .authorizeRequests() // 요청에 대한 사용권한 체크
                	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()	// preFlight request는 인증이 필요없음
                	.antMatchers("/admin/**").hasRole("ADMIN") 			
	                .antMatchers("/reservations/**").hasRole("USER")
	                .antMatchers("/reservation/**").access("hasRole('ROLE_USER')") // .access()함수로 입력시 'ROLE_' prefix가 반드시 있어야 함
	                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
	        .and();
    }
    
    // CorsConfig를 등록함으로써 Server가 브라우저가 보낸 CORS요청을 인지함.
    // Server는 설정에 따라 브라우저 요청을 처리함 (Cors가 허가되지 않은 Origin의 요청일 경우 Server에서 처리하지 않음) 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("-------------------- 처리됨?");
    	final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("http://localhost:3000"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of("Access-Control-Allow-Origin", "Authorization", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // ExposedHeader 미설정시 브라우저에서는 기본 응답 헤더 4가지만 노출 
        configuration.setMaxAge(3600L); // preflight 요청을 처리하기 위함
        
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        System.out.println("-------------------- 호출 완료!?");
        return source;
    }
}