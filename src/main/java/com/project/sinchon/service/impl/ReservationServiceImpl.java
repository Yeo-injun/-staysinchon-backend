package com.project.sinchon.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.sinchon.dao.ReservationDAO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.service.ReservationService;

/*
*
* title : 예약관련 서비스Impl 레이어 
* author : 여인준
* create date : 2021.04.14
* update 
* */

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private ReservationDAO reservationDAO;

	@Override
	public List<ReservationInfoDTO> getMypageList(HashMap<String, String> map) throws Exception {
		return reservationDAO.getMypageList(map);
	}
	
	@Override
	public ReservationInfoDTO getReservationForUpdate(int res_ID) throws Exception {
		return reservationDAO.getReservationForUpdate(res_ID);
	}

	@Override
	public int updateReservation(ReservationInfoDTO reservationInfoVO) {
		return reservationDAO.updateReservation(reservationInfoVO);
	}

	@Override
	public int cancelReservation(ReservationCancelDTO reservationCancelVO) {
		//예약취소 테이블에 취소된 예약 입력하기
		int isOKInsert = reservationDAO.insertCancelReservation(reservationCancelVO);
		
		//예약상태 테이블 상태정보 변경 : 3 (예약취소상태)
		int isOKUpdate = reservationDAO.updateStateToCancel(reservationCancelVO);
		
		if (isOKInsert == 1 && isOKUpdate == 1) {return 1;}
		else {return 0;}
	}
}
