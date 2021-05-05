package com.project.sinchon.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.sinchon.dao.ReservationDAO;
import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.ApplyReservationDTO;
import com.project.sinchon.dto.RoomDTO;
import com.project.sinchon.service.ApplyReservaionService;

/*
*
* title : 예약신청 서비스 레이어 인터페이스
* author : 여인준
* create date : 2021.04.14
* update 
* 2021.04.14 : 여인준 / 사용자가 입력한 예약정보 DB에 저장
* 2021.04.17 : 여인준 / insertReservation 메소드 생성
* */

@Service
public class ApplyReservationServiceImpl implements ApplyReservaionService {
	
	@Autowired
	private ReservationDAO reservationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	// <수정요청사항> : 트랜잭션 처리(중간에 오류뜨면 실행했던 SQL RollBack 시키기!
	public void insertReservation(ApplyReservationDTO applyReservationDTO) throws Exception {
		
		// 예약 정보 테이블 : 예약정보 Insert
		reservationDAO.insertInfo(applyReservationDTO);
		
		// 예약정보 Insert후 입력된 res_ID값 받기
		int res_ID = applyReservationDTO.getRes_ID();
		
		// 추가 데이터를 넣어주기 위해 필요한 인자값 생성
		HashMap<String, Object> map = new HashMap<>();
		map.put("res_ID", res_ID);
		map.put("user_ID", applyReservationDTO.getUser_ID());

		// 예약된 방 테이블, 예약 상태 테이블에 데이터 Insert
		reservationDAO.insertRoomAndState(map);
	}
}
