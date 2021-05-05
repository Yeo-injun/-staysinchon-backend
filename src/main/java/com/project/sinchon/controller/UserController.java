package com.project.sinchon.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.sinchon.config.security.CustomUserDetailService;
import com.project.sinchon.config.security.JwtTokenProvider;
import com.project.sinchon.config.security.User;
import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.UserDTO;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 변수 선언시 Null값을 넣어줘야 하는 것들에 자동으로 Null값 할당 
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	// 비밀번호 암호화 처리 객체 선언
    private final PasswordEncoder passwordEncoder;
    // 토큰 생성 및 검증용 객체 선언
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/register")
    public User join(@RequestBody Map<String, String> user) {
        // UserDetails 인터페이스를 상속받아 구현된 User객체를 Builder패턴으로 생성
    	// 회원가입을 위해 Builer 패턴으로 User객체에 데이터 담아주기
    	// DB에서 기본값으로 USER로 roles 속성값 생성 
    	User userInfo = User.builder()
				        	.user_ID(user.get("user_ID"))
				        	.pwd(passwordEncoder.encode(user.get("pwd")))
				        	.email(user.get("email"))
				            .build();
    	return userInfo;
    }
    
    // 로그인
    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        User authUser = customUserDetailService.loadUserByUsername(user.get("user_ID"));
        // 입력한 user_ID가 존재하지 않을 경우
        if (authUser == null) {
        	throw new IllegalArgumentException("가입되지 않은 회원 입니다.");
        }
       // 비밀번호가 틀릴 경우
        if (!passwordEncoder.matches(user.get("pwd"), authUser.getPwd())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(authUser.getUser_ID(), authUser.getRoles());
         
    }
}
