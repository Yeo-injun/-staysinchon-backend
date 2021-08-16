package com.project.sinchon.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.sinchon.dao.ReservationDAO;
import com.project.sinchon.dao.UserDAO;
import com.project.sinchon.dto.MyReservationDTO;
import com.project.sinchon.dto.ReservationApplicationInfoDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.service.ReservationService;

import lombok.extern.slf4j.Slf4j;

/*
*
* title : 예약관련 서비스Impl 레이어 
* author : 여인준
* create date : 2021.04.14
* update 
* */

@Service
@Transactional
@Slf4j
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private ReservationDAO reservationDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	// 사용자가 신청한 예약내용 저장하기
	@Override
	public void applyReservation(ReservationApplicationInfoDTO dto) throws Exception {
		// 사용자 인적사항 update : orUpdate가 true면 Update
		if(dto.getOrUpdate()) {
			userDAO.updateUserProfile(dto.getUserEntity());
		}
		
		// 예약정보 Insert
		reservationDAO.insertReservationInfo(dto.getReservationInfoEntity());
		
		// 예약된 방, 예약상태 테이블 레코드를 생성하기 위해 필요한 res_ID, user_ID값 map객체에 넣어주기
		Map<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("resId", dto.getReservationInfoEntity().getResId());
		paramsMap.put("userId", dto.getUserEntity().getUserId());

		// 예약된 방 테이블, 예약 상태 테이블에 데이터 Insert
		reservationDAO.insertRoomAndState(paramsMap);
	}
	
	
	// 사용자가 예약한 예약현황 및 상태 정보 가져오기 
	@Override
	public List<MyReservationDTO> getMyReservationList(String userId) throws Exception {
		return reservationDAO.getMyReservationList(userId);
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
	public Boolean cancelReservation(ReservationCancelDTO reservationCancelDTO) {
		// 예약취소 테이블에 취소된 예약 입력하기
		int isOkInsert = reservationDAO.insertCancelReservation(reservationCancelDTO);
		
		// 예약상태 테이블 상태정보 변경 : 3 (예약취소상태)
		int isOkUpdate = reservationDAO.updateStateToCancel(reservationCancelDTO);
		
		// 예약된 방 테이블에서 해당 res_ID 값 삭제하기
		int isOkDelete = reservationDAO.deleteReservationRoom(reservationCancelDTO);
		log.info("삭제할 res_ID : {}", reservationCancelDTO.getRes_ID());
		log.info("삭제 DAO 정상작동? : {}", isOkDelete);
		
		if (isOkInsert == 1 && isOkUpdate == 1 && isOkDelete > 0) {return true;}
		else {return false;}
	}


}
