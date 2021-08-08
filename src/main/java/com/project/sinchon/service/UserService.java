package com.project.sinchon.service;

import java.util.Map;

import com.project.sinchon.dto.UserDTO;

/*
*
* title : user 서비스 레이어 인터페이스
* author : 여인준
* create date : 2021.05.22
* update 
* */

public interface UserService {
	 /**
     * @description 회원 인적사항 가져오기
     */
	public UserDTO getUserDetails(Map<String, String> map) throws Exception;

	/**
     * @description 회원 인적사항 수정하기
     */
	public int updateUserProfile(UserDTO userDTO);

	/**
     * @description 회원 정보 수정시 비밀번호 확인
     */
	public String checkPasswordForProfileUpdate(String userId); 
}
