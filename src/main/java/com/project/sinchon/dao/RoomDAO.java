package com.project.sinchon.dao;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.sinchon.dto.*;

/*
*
* title : Room테이블 관련 DAO
* author : 여인준
* create date : 2021.04.03
* update 
* 	2021.04.18 : 여인준 / 호스트가 등록한 모든 방 조회
* 
* */

@Repository
public class RoomDAO {
	
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.project.sinchon.mapper.RoomMapper";
    
	 /**
     * @description 호스트가 등록한 모든 방 조회
     */
	public List<RoomDTO> getRoomList() {
		return sqlSession.selectList(namespace + ".getRoomList");
	}
    
	 /**
     * @description 예약가능한 방 리스트 출력(기본값 : 접속일 기준 1박2일로 예약가능한 방 조회)
     */
	public List<RoomDTO> getReservableRoomListDefaultSearch() {
        return sqlSession.selectList(namespace + ".getReservableRoomListDefaultSearch");
	}

	 /**
     * @description 예약가능한 방 리스트 출력 (사용자 입력값에 따라 출력)
     */	
	public List<RoomDTO> getSearchReservableRoomList(Map<String, Date> paramsMap) {
		return sqlSession.selectList(namespace + ".getSearchReservableRoomList", paramsMap);
	}

}
