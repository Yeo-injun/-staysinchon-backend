package com.project.sinchon.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;


/*
*
* title : 예약관련 서비스 레이어 인터페이스
* author : 여인준
* create date : 2021.04.21
* update 
* 
* */

public interface ReservationService {
	 /**
     * @param
     * @return List<ReservationInfoVO
	 * @description 사용자가 예약한 예약현황 및 상태 정보 가져오기 
     */
	List<ReservationInfoDTO> getMyReservationList(HashMap<String, String> map) throws Exception;

	 /**
     * @param 
     * @return ReservationInfoVO
	 * @description 수정할 예약정보 가져오기 
     */
	ReservationInfoDTO getReservationForUpdate(Map map) throws Exception;

	 /**
     * @param
	 * @return int
	 * @throws Exception 
	 * @description 예약한 사용자인지 확인하고, 입력받은 예약정보로 수정하기
     */
	String updateReservation(Map map) throws Exception;

	 /**
     * @param
	 * @return int
	 * @description 예약 취소하기 
     */
	int cancelReservation(ReservationCancelDTO reservationCancelDTO);
}
