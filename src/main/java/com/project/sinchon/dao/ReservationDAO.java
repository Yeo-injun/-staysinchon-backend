package com.project.sinchon.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.sinchon.dto.ApplyReservationDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;

/*
*
* title : 예약관련 DAO 레이어 
* author : 여인준
* create date : 2021.04.14
* update 
* 2021.04.14 : 여인준 / 사용자가 입력한 예약정보 DB에 저장
* 2021.04.17 : 여인준 / insertRoomAndState 메소드 생성(프로시저 호출)
* */

@Repository
public class ReservationDAO {
    @Autowired
    private SqlSession sqlSession;
    private static final String namespace = "com.project.sinchon.mapper.reservation";
	
	// 예약정보 테이블 insert
	public int insertInfo(ApplyReservationDTO applyReservationDTO) {
		return sqlSession.insert(namespace + ".insertInfo", applyReservationDTO);
	}

	// 예약된 방 테이블, 예약상태 테이블 insert >> insert_room_and_state 프로시저 호출
	public void insertRoomAndState(HashMap<String, Object> map) {
		sqlSession.insert(namespace + ".insertRoomAndState", map);
		
	}

	// 게스트의 예약 현황 및 상태 정보 가져오기
	public List<ReservationInfoDTO> getMypageList(HashMap<String, String> map) {
		return sqlSession.selectList(namespace + ".getMypageList", map);
	}

	// 수정할 예약 정보 가져오기
	public ReservationInfoDTO getReservationForUpdate(int res_ID) {
		return sqlSession.selectOne(namespace + ".getReservationForUpdate", res_ID);
	}

	// 입력한 예약정보 수정하기
	public int updateReservation(ReservationInfoDTO reservationInfoDTO) {
		return sqlSession.update(namespace + ".updateReservation", reservationInfoDTO);
	}
	
	// 예약취소 테이블에 취소된 예약ID 삽입하기
	public int insertCancelReservation(ReservationCancelDTO reservationCancelDTO) {
		return sqlSession.insert(namespace + ".insertCancelReservation", reservationCancelDTO);
	}
	
	// 취소된 예약의 예약상태 변경
	public int updateStateToCancel(ReservationCancelDTO reservationCancelDTO) {
		return sqlSession.update(namespace + ".updateStateToCancel", reservationCancelDTO);
		
	}

}
