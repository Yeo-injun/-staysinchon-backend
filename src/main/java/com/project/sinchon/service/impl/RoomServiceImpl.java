package com.project.sinchon.service.impl;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.sinchon.dao.RoomDAO;
import com.project.sinchon.dto.RoomDTO;
import com.project.sinchon.service.RoomService;

/**
*
* title : room 서비스 layer 구현부
* author : 여인준
* create date : 2021.04.03
* update
* 2021.04.11 : 여인준 / 예약가능한 방 기본값 및 사용자 입력값에 따라 조회
* 2021.04.18 : 여인준 / 호스트가 등록한 모든 방 조회
* 
**/

@Service
public class RoomServiceImpl implements RoomService {
	@Autowired
	private RoomDAO roomDAO;
	
	 /**
     * @description 호스트가 등록한 모든 방 조회
     */
	@Override
	public List<RoomDTO> getList() throws Exception {
		return roomDAO.getList();
	}
	
	/**
     * @description 예약가능한 방 리스트 출력 (기본값 : 접속일 기준 1박2일로 예약가능한 방 조회)
     */	
	@Override
	public List<RoomDTO> getAbleList() throws Exception {
		return roomDAO.getAbleList();
	}

	 /**
     * @description 예약가능한 방 리스트 출력 (사용자 입력값에 따라 출력)
     */	
	@Override
	public List<RoomDTO> getSearchList(HashMap<String, Date> dateMap) {
		return roomDAO.getSearchList(dateMap);
	}
}
