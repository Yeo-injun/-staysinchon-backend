package com.project.sinchon.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.sinchon.dto.MyReservationDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.entity.ReservationInfoEntity;

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
	
	public int insertReservationInfo(ReservationInfoEntity reservationInfoEntity) {
		return sqlSession.insert(namespace + ".insertReservationInfo", reservationInfoEntity);		
	} /* insertInfo 수정 21.08.16 */
	
	
	// 예약된 방 테이블, 예약상태 테이블 insert >> insert_room_and_state 프로시저 호출
	public int insertRoomAndState(Map<String, Object> paramsMap) {
		return sqlSession.insert(namespace + ".insertRoomAndState", paramsMap);
	} /* 수정 21.08.16 */

	
	// 게스트의 예약 현황 및 상태 정보 가져오기
	public List<MyReservationDTO> getMyReservationList(String userId) {
		return sqlSession.selectList(namespace + ".getMyReservationList", userId);
	} /* 수정 21.08.16 */

	
	// 수정할 예약 정보 가져오기
	public ReservationInfoEntity getReservationInfoForUpdate(Map map) {
		return sqlSession.selectOne(namespace + ".getReservationInfoForUpdate", map);
	} /* 수정 21.08.17 */

	
	// 입력한 예약정보 수정하기
	public int updateReservationInfo(ReservationInfoEntity reservationInfoEntity) {
		return sqlSession.update(namespace + ".updateReservationInfo", reservationInfoEntity);
	} /* 수정 21.08.19 */
	
	
	// 예약취소 테이블에 취소된 예약ID 삽입하기
	public int insertReasonForCancelReservation(ReservationCancelDTO reservationCancelDTO) {
		return sqlSession.insert(namespace + ".insertReasonForCancelReservation", reservationCancelDTO);
	} /* 수정 21.08.20 */
	
	
	// 취소된 예약의 예약상태 변경
	public int updateReservationStateToCancel(ReservationCancelDTO reservationCancelDTO) {
		return sqlSession.update(namespace + ".updateReservationStateToCancel", reservationCancelDTO);
	} /* 수정 21.08.20 */

	
	// 예약 취소된 res_ID로 예약된 방테이블 방정보 제거
	public int deleteReservationRoom(ReservationCancelDTO reservationCancelDTO) {
		return sqlSession.delete(namespace + ".deleteReservationRoom", reservationCancelDTO);
	} /* 수정 21.08.20 */

}
