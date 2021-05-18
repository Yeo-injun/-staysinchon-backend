package com.project.sinchon.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.sinchon.config.security.PrincipalDetailsService;
import com.project.sinchon.config.security.auth.User;
import com.project.sinchon.config.security.jwt.JwtTokenProvider;
import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.UserDTO;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // 변수 선언시 Null값을 넣어줘야 하는 것들에 자동으로 Null값 할당 
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	// 비밀번호 암호화 처리 객체 선언 : SecurityConfig에 Bean으로 등록돼 있어야 함.
    private final BCryptPasswordEncoder passwordEncoder;
    
    // 토큰 생성 및 검증용 객체 선언
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    @PostMapping("/register")
    public String register(@RequestBody User user) {
    	// bcrypt 설정 추가 필요...
    	user.setPwd(passwordEncoder.encode(user.getPwd()));
    	int success = principalDetailsService.registerUser(user);
    	if (success != 0) {
    		return "회원가입 성공";
    	}
    	
    	return "회원가입 실패...";
    	
    }
//    
//    // 로그인
//    @PostMapping("/login")
//    public String login(@RequestBody Map<String, String> user) {
//    	
//    	// JwtAuthentication 클래스를 활용해서 수정작업해주기...! 21.05.15
//        User authUser = customUserDetailService.loadUserByUsername(user.get("user_ID"));
//        // 입력한 user_ID가 존재하지 않을 경우
//        if (authUser == null) {
//        	throw new IllegalArgumentException("가입되지 않은 회원 입니다.");
//        }
//       // 비밀번호가 틀릴 경우
//        if (!passwordEncoder.matches(user.get("pwd"), authUser.getPwd())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
//        System.out.println(authUser.getRole());
//        System.out.println(authUser.getRoles());
//        
//        return jwtTokenProvider.createToken(authUser.getUser_ID(), authUser.getRoles());
//         
//    }
}
