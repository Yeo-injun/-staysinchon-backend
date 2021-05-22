package com.project.sinchon.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
* 2021.04.14 여인준 : 사용자가 입력한 예약정보 DB에 저장
* 2021.04.17 여인준 : insertReservation 메소드 생성
* 2021.05.22 여인준 : @Transaction 처리 적용(사용자 인적사항update, 예약정보insert, 예약상태 정보insert, 예약된 방insert)
* */

@Service
@Transactional
public class ApplyReservationServiceImpl implements ApplyReservaionService {
	
	@Autowired
	private ReservationDAO reservationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Override
	public void insertReservation(ApplyReservationDTO applyReservationDTO) throws Exception {
		// 사용자 인적사항 update
		userDAO.updateUserDetails(applyReservationDTO);
		
		// 예약정보 Insert
		reservationDAO.insertInfo(applyReservationDTO);
		
		// 예약된 방, 예약상태 테이블 레코드를 생성하기 위해 필요한 res_ID, user_ID값 map객체에 넣어주기
		HashMap<String, Object> map = new HashMap<>();
		map.put("res_ID", applyReservationDTO.getRes_ID());
		map.put("user_ID", applyReservationDTO.getUser_ID());

		// 예약된 방 테이블, 예약 상태 테이블에 데이터 Insert
		reservationDAO.insertRoomAndState(map);
	}
}
