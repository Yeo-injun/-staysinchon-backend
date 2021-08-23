package com.project.sinchon.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.dto.UserProfileDTO;
import com.project.sinchon.entity.UserEntity;
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
    * @description 회원 아이디 중복확인
    */
	@Override
	public void checkDuplicationForId(UserEntity userEntity) throws Exception {
		String sqlResert = userDAO.checkDuplicationForId(userEntity.getUserId());
		if (sqlResert != null) { // 기존에 아이디가 존재하면 Error를 던짐
			throw new Exception();
		}
	}
	
	
    /**
    * @description 회원가입하기
    */
	@Override
	public void regiterUser(UserEntity userEntity) throws Exception {		
		userDAO.insertUser(userEntity);
	}
	
	
	 /**
     * @description 회원 인적사항 가져오기
     */
	@Override
	public UserEntity getUserProfile(String userId) throws Exception {		
		return userDAO.getUserProfile(userId);
	}

	/**
     * @description 회원 인적사항 수정하기
     */
	@Override
	public int updateUserProfile(UserEntity userEntity) throws Exception {
		return userDAO.updateUserProfile(userEntity);
	}

	
	/**
     * @description 회원 정보 수정시 비밀번호 확인
     */
	public String checkPasswordForProfileUpdate(String userId) throws Exception {
		return userDAO.checkPasswordForProfileUpdate(userId);
	}

	
	 /**
     * @description 회원 프로필 가져오기 : 예약할때 입력된 회원정보가 있을 경우 회원정보 return
     */
	@Override
	public UserProfileDTO getUserProfileForReservation(String userId) {
		// return으로 넘겨줄 DTO객체 생성
		UserProfileDTO result = new UserProfileDTO();
		UserEntity userEntity = userDAO.getUserProfile(userId);
		
		// reg_date와 update_date가 다른지 확인 : reg_date - update_date 값이 다르면 사용자 인적사항 return해주기
		if (!userEntity.getRegDate().equals(userEntity.getUpdateDate())) {
			result.setUserInfoYn(true);
			result.setUserEntity(userEntity);
			return result;			
		} else {
			result.setUserInfoYn(false);
			return result;
		}
	} // 21.08.23 수정
} // End
