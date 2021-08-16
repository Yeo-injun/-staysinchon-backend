package com.project.sinchon.service;

import java.util.Map;

import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.entity.UserEntity;

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
	public UserDTO getUserDetails(String userId) throws Exception;

	
	/**
     * @description 회원 인적사항 수정하기
     */
	public int updateUserProfile(UserEntity userEntity) throws Exception;

	
	/**
     * @description 회원 정보 수정시 비밀번호 확인
     */
	public String checkPasswordForProfileUpdate(String userId) throws Exception;

	
	 /**
     * @description 회원 프로필 가져오기 : 예약할때 입력된 회원정보가 있을 경우 회원정보 return
     */
	public UserDTO getUserProfileForReservation(String userId) throws Exception;

}
