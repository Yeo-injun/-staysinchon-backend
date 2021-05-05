package com.project.sinchon.service;

import java.util.HashMap;
import java.util.List;

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
	List<ReservationInfoDTO> getMypageList(HashMap<String, String> map) throws Exception;

	 /**
     * @param 
     * @return ReservationInfoVO
	 * @description 수정할 예약정보 가져오기 
     */
	ReservationInfoDTO getReservationForUpdate(int res_ID) throws Exception;

	 /**
     * @param
	 * @return int
	 * @description 입력받은 예약정보로 수정오기 
     */
	int updateReservation(ReservationInfoDTO reservationInfoDTO);

	 /**
     * @param
	 * @return int
	 * @description 예약 취소하기 
     */
	int cancelReservation(ReservationCancelDTO reservationCancelDTO);
}
