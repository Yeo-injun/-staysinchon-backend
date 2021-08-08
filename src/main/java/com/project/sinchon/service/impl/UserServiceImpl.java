package com.project.sinchon.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.service.UserService;

/**
*
* title : user 서비스 layer 구현부
* author : 여인준
* create date : 2021.05.22
* update
* 
**/

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;
	
 
	
	
	 /**
     * @description 회원 인적사항 가져오기
     */
	@Override
	public UserDTO getUserDetails(Map<String, String> map) throws Exception {		
		return userDAO.getUserDetails(map);
	}

	/**
     * @description 회원 인적사항 수정하기
     */
	@Override
	public int updateUserProfile(UserDTO userDTO) {
		return userDAO.updateUserDetails(userDTO);
	}

	/**
     * @description 회원 정보 수정시 비밀번호 확인
     */
	public String checkPasswordForProfileUpdate(String userId) {
		
		
		return userDAO.checkPasswordForProfileUpdate(userId);
	}

}
