package com.project.sinchon.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.sinchon.config.security.auth.User;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.entity.UserEntity;


/*
*
* title : 사용자관련 DAO 레이어 
* author : 여인준
* create date : 2021.04.14
* update 
* 2021.04.14 : 여인준 / 예약신청시 회원이 입력한 데이터를 기준으로 회원정보 수정(update) 
* 
* */

@Repository
public class UserDAO {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.project.sinchon.mapper.user";

    
    // 아이디 중복 체크
	public String checkDuplicationForId (String userEntity) {
		 return sqlSession.selectOne(namespace + ".checkDuplicationForId", userEntity);
	} // 21.08.21 수정

	
    // 회원가입
	public void insertUser(UserEntity userEntity) {
		sqlSession.insert(namespace + ".insertUser", userEntity);
	} // 21.08.21 수정
	
	
	// 로그인 : 로그인 정보 비교를 위한 사용자 DB정보 가져오기
	public User findByUserId(String userId) {
		return sqlSession.selectOne(namespace + ".findByUserId", userId);
	}


	// 사용자 인적사항 가져오기
	public UserEntity getUserProfile(String userId) {
		return sqlSession.selectOne(namespace + ".getUserProfile", userId);
	} // 21.08.23 수정
	
	
	/* 예약신청시 입력하는 사용자 정보 저장 @@@@@ 21.08.16 대체된 코드 @@@@@ */
	public int updateUserDetails(UserDTO userDTO) {
		return sqlSession.update(namespace + ".updateUserDetails", userDTO);
	}
	
	
	// 회원정보 수정 /* 대체 코드 */ 기존 MyPage에서만 사용했지만 예약신청할때도 동일하게 사용
	public int updateUserProfile(UserEntity userEntity) {
		return sqlSession.update(namespace + ".updateUserProfile", userEntity);
	}
	
	
	// 회원정보 수정을 위한 비밀번호 확인
	public String checkPasswordForProfileUpdate(String userId) {
		return sqlSession.selectOne(namespace + ".checkPasswordForProfileUpdate", userId);
	} // 21.08.23 수정

} // End
