package com.project.sinchon.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Override
	public UserDTO getUserDetails(Map<String, String> map) throws Exception {		
		return userDAO.getUserDetails(map);
	}

}
