package com.project.sinchon.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	// 사용자가 예약한 예약현황 및 상태 정보 가져오기 
	@Override
	public List<ReservationInfoDTO> getMyReservationList(HashMap<String, String> map) throws Exception {
		return reservationDAO.getMyReservationList(map);
	}
	
	// 수정할 예약정보 가져오기
	@Override
	public ReservationInfoDTO getReservationForUpdate(Map map) throws Exception {
		return reservationDAO.getReservationForUpdate(map);
	}
	
	// 예약한 사용자인지 확인하고, 입력받은 예약정보로 수정하기
	@Override
	public String updateReservation(Map map) throws Exception {
		// 예약한 사용자인지 확인 : 예약한 사용자일 경우에만 update실행
		Map ReservationUserID = reservationDAO.getReservationUserID((Integer) map.get("res_ID"));
		if (ReservationUserID.get("user_ID").equals((String) map.get("user_ID"))) {
			reservationDAO.updateReservation(map);
			return "수정 완료입니다.";
		}
		return "수정할 수 있는 권한이 없습니다.";
	}

	// 
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
