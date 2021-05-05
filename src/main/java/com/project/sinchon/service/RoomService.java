package com.project.sinchon.service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import com.project.sinchon.dto.RoomDTO;

/*
*
* title : room 서비스 레이어 인터페이스
* author : 여인준
* create date : 2021.04.03
* update 
* 2021.04.11 : 여인준 / 예약가능한 방 기본값 및 사용자 입력값에 따라 조회
* 2021.04.18 : 여인준 / 호스트가 등록한 모든 방 조회
* 
* */

public interface RoomService {
	 /**
     * @description 호스트가 등록한 모든 방 조회
     */
	List<RoomDTO> getList() throws Exception;
	 
	/**
     * @description 예약가능한 방 리스트 출력 (기본값 : 접속일 기준 1박2일로 예약가능한 방 조회)
     */
	List<RoomDTO> getAbleList() throws Exception;

	 /**
     * @param dateMap 
	 * @description 예약가능한 방 리스트 출력 (사용자 입력값에 따라 출력)
     */	
	List<RoomDTO> getSearchList(HashMap<String, Date> dateMap) throws Exception;


}
