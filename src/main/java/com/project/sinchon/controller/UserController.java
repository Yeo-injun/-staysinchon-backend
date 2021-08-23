package com.project.sinchon.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.sinchon.config.security.PrincipalDetailsService;
import com.project.sinchon.config.security.auth.User;
import com.project.sinchon.config.security.jwt.JwtTokenProvider;
import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.entity.UserEntity;
import com.project.sinchon.service.UserService;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;

/*
*
* title : 사용자관련 Controller
* author : 여인준
* create date : 2021.04.21
* update 
* 2021.08.07 여인준 : [GET] user/profile API 구현 (스프링 시큐리티에서 권한 체크 후 principal객체 반환. 이 객체의 .getName() 메소드를 활용하여 userId값 가져오기
* */

@RequiredArgsConstructor // 변수 선언시 Null값을 넣어줘야 하는 것들에 자동으로 Null값 할당 
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	private UserService userService;
	
	// 비밀번호 암호화 처리 객체 선언 : SecurityConfig에 Bean으로 등록돼 있어야 함.
    private final BCryptPasswordEncoder passwordEncoder;
    
    // 토큰 생성 및 검증용 객체 선언
    private final JwtTokenProvider jwtTokenProvider;

    
    // 회원가입
    @PostMapping("/register")
    public void registerUser(@RequestBody UserEntity userEntity) throws Exception
    {
    	userEntity.setPwd(passwordEncoder.encode(userEntity.getPwd()));
    	userService.regiterUser(userEntity);
    } // 수정 21.08.21
    
    
    // 아이디 중복 체크
    @PostMapping("/check")
    public void checkDuplicationForId(@RequestBody UserEntity userEntity) throws Exception {
    	userService.checkDuplicationForId(userEntity);
    } // 수정 21.08.22
    
    
    // 회원정보 보여주기 : 스프링 시큐리티에서 Auth체크를 진행하고, 통과하면 Principal객체를 반환함. 이 객체에 userID정보가 있어 이를 가져와서 회원정보를 가져오기
    @GetMapping(value = "/profile", produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserEntity getUserProfile(Principal principal) throws Exception 
    {
    	String userId = principal.getName();	
    	return userService.getUserProfile(userId);
    } // 수정 21.08.23
    
    
    // 회원정보 수정하기
    @PutMapping(value = "/profile", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateUserProfile(@RequestBody UserEntity userEntity, Principal principal) throws Exception 
    {
    	String userId = principal.getName();
    	userEntity.setUserId(userId);
    	userService.updateUserProfile(userEntity);
    } // 수정 21.08.16 
    
    
    // 회원정보 수정시 비밀번호 확인하기
    @PostMapping(value="/profile/check", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public boolean checkPasswordForProfileUpdate(@RequestBody UserEntity userEntity, Principal principal ) throws Exception {
    	String userId = principal.getName();
    	String encodedPassword =  userService.checkPasswordForProfileUpdate(userId);
    	return passwordEncoder.matches(userEntity.getPwd(), encodedPassword);
    } // 21.08.23 수정
}
