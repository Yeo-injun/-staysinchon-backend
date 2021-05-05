package com.project.sinchon.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.sinchon.config.security.User;
import com.project.sinchon.dto.ApplyReservationDTO;


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
    
    // 회원가입
	public int registerUser(User userInfo) {
		System.out.println("실행됨!!!!!----------D");
		return sqlSession.insert(namespace + ".registerUser", userInfo);
	}
	
	// 로그인 : 로그인 정보 비교를 위한 사용자 DB정보 가져오기
	public User findByUserId(String user_ID) {
		return sqlSession.selectOne(namespace + ".findByUserId", user_ID);
	}

	// 예약신청시 입력하는 사용자 정보 저장
	public void updateUserDetails(ApplyReservationDTO applyReservationDTO) {
		sqlSession.update(namespace + ".updateUserDetails", applyReservationDTO);
	}



}
