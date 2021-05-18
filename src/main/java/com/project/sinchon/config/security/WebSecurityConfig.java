package com.project.sinchon.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.sinchon.config.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CorsConfig corsConfig;
	
    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http	
                .addFilter(corsConfig.corsFilter())
        		.csrf().disable() 			// csrf 보안 토큰 disable처리.               
        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용X
            .and()
            	.httpBasic().disable() 		// rest api 만을 고려하여 기본 설정은 해제
                .formLogin().disable() 		// /login은 작동하지 않음. /login을 작동시키기 위해서 새로운 Filter를 등록해야 함
                
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))			// /login을 동작시키기 위한 새로운 Filter 추가 (UsernamePasswordAuthenticationFilter
                
                .authorizeRequests() 		// 요청에 대한 사용권한 체크
	                .antMatchers("/admin/**").hasRole("ADMIN")
	                .antMatchers("/rooms/**").hasRole("USER")
	                .anyRequest().permitAll(); // 그외 나머지 요청은 누구나 접근 가능
    }

}