package com.project.sinchon.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.sinchon.dto.MyReservationDTO;
import com.project.sinchon.dto.ReservationApplicationInfoDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.entity.ReservationInfoEntity;


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
	List<MyReservationDTO> getMyReservationList(String userId) throws Exception;

	 /**
     * @param 
     * @return ReservationInfoEntity
	 * @description 수정할 예약정보 가져오기 
     */
	ReservationInfoEntity getReservationInfoForUpdate(Map map) throws Exception;

	 /**
     * @param ReservationInfoEntity
	 * @return void
	 * @throws Exception 
	 * @description 예약한 사용자인지 확인하고, 입력받은 예약정보로 수정하기
     */
	void updateReservationInfo(ReservationInfoEntity reservationInfoEntity) throws Exception;

	 /**
     * @param
	 * @return int
	 * @description 예약 취소하기 
     */
	void cancelReservation(ReservationCancelDTO reservationCancelDTO) throws Exception;

	 /**
     * @param ReservationApplicationInfoDTO
	 * @return void
	 * @throws Exception 
	 * @description 예약 신청하기 
     */
	void applyReservation(ReservationApplicationInfoDTO dto) throws Exception;
}
