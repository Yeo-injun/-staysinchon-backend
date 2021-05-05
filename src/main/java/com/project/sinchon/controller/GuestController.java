package com.project.sinchon.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.sinchon.dto.ApplyReservationDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.dto.ReviewDTO;
import com.project.sinchon.dto.RoomDTO;
import com.project.sinchon.dto.SNSInfoDTO;
import com.project.sinchon.service.ApplyReservaionService;
import com.project.sinchon.service.ReservationService;
import com.project.sinchon.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *
 * title : Guest관련 컨트롤러
 * author : 여인준
 * create date : 2021.04.03
 * update 
 * 2021.04.11 : 여인준 / 예약가능한 방 기본값 및 사용자 입력값에 따라 조회 API 구현
 * 2021.04.12 : 여인준 / 예약신청 폼 이동시 날짜값 반환 API 구현 (DB작업 X)
 * 2021.04.14 : 여인준 / 예약하기 API 구현(post 메소드, DB insert)
 * 2021.04.17 : 여인준 / POST /reservation 예약신청 정보 DB저장 Controller 구현 완료 
 * 2021.04.17 : 여인준 / 예약신청 폼화면으로 이동 요청 메소드 변경 (GET에서 POST로)
 * 2021.04.18 : 여인준 / 호스트가 등록한 모든 방 조회(예약하기 페이지에서 기본적으로 제공하는 데이터)
 * 
 * */

@RestController
@RequestMapping(value = "/*")
public class GuestController {
    
    @Autowired // 확인사항 : Inject과 차이
    private RoomService roomService;
    
    @Autowired
    private ApplyReservaionService applyReservationService;
    
    @Autowired
    private ReservationService reservationService;
    
    /**
     * @description [예약페이지] 모든 방 조회
     */
    @GetMapping(value = "/rooms", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> roomList() throws Exception{
        return roomService.getList();
    }
    
    /**
     * @description [예약페이지] 예약가능한 방 기본값 조회(기본값 : 현재일 기준 1박 2일 예약가능한 방)
     */
    @GetMapping(value = "/rooms/available", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> roomAbleList() throws Exception{
        return roomService.getAbleList();
    }
    
    /**
     * @description [예약페이지] 예약가능한 방 검색(파라미터 : check in 날짜, check out 날짜) 
     */
    @GetMapping(value = "/rooms/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> searchRoomList(@RequestParam("check_in") String checkIn,
    									@RequestParam("check_out") String checkOut) throws Exception{
    	// 사용자가 입력한 날짜를 담을 map객체 생성
    	HashMap<String, Date> dateMap = new HashMap<String, Date>();
        
    	// 사용자 입력 날짜 자료형 변형(String to Date)
    	Date dateCheckIn = Date.valueOf(checkIn);
        Date dateCheckOut = Date.valueOf(checkOut);
        
        // 입력받은 날짜 map객체에 담아주기
        dateMap.put("checkIn", dateCheckIn);
        dateMap.put("checkOut", dateCheckOut);
    	
        // Service 레이어 호출시 입력받은 날짜가 담긴 dateMap을 인자로 넣어서 호출
    	return roomService.getSearchList(dateMap);
    }


    /**
     * @description [예약페이지] 예약신청 폼(form) 화면으로 이동 
     *  <21.04.17 수정사항>
     * 1. 예약하기 페이지에서 선택한 check_in, check_out 값 POST로 넘겨주기
     *  <수정 예정사항>
     * 2. 예약정보가 있다면 회원정보 데이터를 같이 보내주기 (User Table에 회원정보 입력여부 컬럼 추가)
     */
    @PostMapping(value = "/reservation/form", produces = {MediaType.APPLICATION_JSON_VALUE})
    public JsonObject reservationForm(@RequestBody Map<String, Object> dateInOut ) throws Exception{
    	// POST방식으로 받은 JSON 데이터 Map으로 받기 
    	// Map으로 받은 데이터 JSON형태로 변환 : Gson모듈내 JSON클래스 활용
    	JsonObject jsonObj = new JsonObject();
    	
    	// Map으로 받은 check_in값 과 check_out값 JSON으로 다시 넘겨주기
    	jsonObj.addProperty("check_in", (String) dateInOut.get("check_in"));
    	jsonObj.addProperty("check_out", (String) dateInOut.get("check_out"));
    	
    	// 회원 아이디로 회원저
        // JSON객체를 String으로 반환 
    	return jsonObj;
    }

    /**
     * @description [예약페이지] 예약신청하기 
     * 프론트엔드와 통신시 RequestBody의 데이터 형태 확인
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.
     */
    @PostMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void applyReservation(@RequestBody ApplyReservationDTO applyReservationDTO) throws Exception {
    	// 사용자 입력 데이터가 JSON형태로 들어와서
    	// @RequestBody를 거쳐서 VO객체의 변수들과 매핑됨
    	// 매핑된 VO객체를 Service레이어의 인자값으로 넘겨줌
    	applyReservationService.insertReservation(applyReservationDTO);
    }

    /**
     * @throws Exception 
     * @description [마이페이지] 본인 예약 이력 및 현황 확인하기
     * 2021.04.21 ver. user_ID를 url로 받아오기 (로그인 구현후 수정 예정)     
     */
    @GetMapping(value = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReservationInfoDTO> getReservations(@RequestParam("user_id") String userID) throws Exception {
    	// user_ID는 url 쿼리스트링으로 받아와서
    	// map자료구조에 user_ID값을 담고
    	// map을 인자로 넣어줘 Service 레이어 호출
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("user_ID", userID);
    	List<ReservationInfoDTO> mypageList = reservationService.getMypageList(map);
    	System.out.println(mypageList.size());
    	return mypageList;
    }
    
    /**
     * @description [마이페이지] 예약수정하기 (수정할 예약정보 가져오기) 
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 권한관리 (비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.) / 반환되는 JSON객체의 null값 제거하기!
     */
    @GetMapping(value = "/reservation/{res_ID}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ReservationInfoDTO getReservationForUpdate(@PathVariable("res_ID") int res_ID) throws Exception {
    	// 수정예정사항 : 권한관리 필요.. 반환되는 JSON객체 NULL값 제거...
    	return reservationService.getReservationForUpdate(res_ID);
    }
    
    /**
     * @description [마이페이지] 예약수정하기 (사용자가 입력한 예약정보로 수정하기) 
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 권한관리 (비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.) / 수정데이터를 받을 객체 수정(수정항목만 받을 객체로 생성)
     */
    @PutMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updateReservation(@RequestBody ReservationInfoDTO reservataionInfoVO) throws Exception {
    	int isSuccess = reservationService.updateReservation(reservataionInfoVO);
    	
    	if (isSuccess == 1) {return "수정완료입니다!";} 
    	else {return "수정이 안됐습니다.. ㅠ 재시도해주세요";}
    }
    
    /**
     * @description [마이페이지] 예약취소하기  
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 권한관리 (비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.) / 수정데이터를 받을 객체 수정(수정항목만 받을 객체로 생성) / DAO호출 2개하는 것을 프로시저로 작성!
     */
    @PutMapping(value = "/reservation/cancel", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String cancelReservation(@RequestBody ReservationCancelDTO reservationCancelVO) throws Exception {
    	int isSuccess = reservationService.cancelReservation(reservationCancelVO);
    	return "성공!";
//    	if (isSuccess == 1) {return "정상적으로 예약이 취소됐습니다!";} 
//    	else {return "취소가 안됐습니다.. ㅠ 재시도해주세요";}
    }
}// End
