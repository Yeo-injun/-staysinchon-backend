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
     * @description 사용자 인적사항이 작성돼 있으면 인적사항 반환
     */
	public UserDTO getUserDetails(Map<String, String> map) throws Exception; 
}
