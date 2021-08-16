package com.project.sinchon.controller;

import java.security.Principal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.project.sinchon.dto.MyReservationDTO;
import com.project.sinchon.dto.ReservationApplicationInfoDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.dto.RoomDTO;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.service.ReservationService;
import com.project.sinchon.service.RoomService;
import com.project.sinchon.service.UserService;


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
 * -------------------------------------------------------------------------
 * 리팩토링 ver.0.1 : 파라미터 타입 고정 : primitive type 또는 DTO객체. Map객체 절대 안됨.
 * */

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/*")
public class GuestController {
    
    @Autowired 
    private RoomService roomService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;
    
    /**
     * @description [예약페이지] 모든 방 조회
     */
    @GetMapping(value = "/rooms", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> getRoomList() throws Exception 
    {
        return roomService.getRoomList();
    }
    
    
    /**
     * @description [예약페이지] 예약가능한 방 기본값 조회(기본값 : 현재일 기준 1박 2일 예약가능한 방)
     */
    @GetMapping(value = "/rooms/available", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> getReservableRoomListDefaultSearch(Authentication auth) throws Exception 
    {
        return roomService.getReservableRoomListDefaultSearch();
    }
    
    
    /**
     * @description [예약페이지] 예약가능한 방 검색(파라미터 : check in 날짜, check out 날짜) 
     */
    @GetMapping(value = "/rooms/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> getSearchReservableRoomList(@RequestParam("check_in") String checkIn,
    												 @RequestParam("check_out") String checkOut) throws Exception 
    {	
    	Map<String, Date> paramsMap = new HashMap<String, Date>();
        paramsMap.put("checkIn", Date.valueOf(checkIn));
        paramsMap.put("checkOut", Date.valueOf(checkOut));
    	return roomService.getSearchReservableRoomList(paramsMap);
    }


    /**
     * @description [예약페이지] 예약신청 폼(form) 화면으로 이동 
     *  21.05.23 인준 : 사용자의 인적사항이 있다면 인적사항 데이터 같이 보내주기 구현(reg_date와 update_date가 다르면 인적사항이 있다는 것) 
     *  21.07.20 인준 : Post메소드에서 Get메소드로 전환(React에서 화면전환시 Link 태그에 데이터를 넘겨주고, 전환된 화면에서 {location}를 파라미터로 받아서 사용 가능해서) 
     */
    @GetMapping(value = "/reservation/form", produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserDTO getUserProfileForReservation(Principal principal) throws Exception
    {
    	// 인증된 사용자의 user데이터 가져오기  
    	String userId = principal.getName();
    	return userService.getUserProfileForReservation(userId);
    }

    
    /**
     * @description [예약페이지] 예약신청하기 
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함. 
     * 21.05.20 인준 : Security 구현으로 회원일 경우에만 접근가능하고, Principal객체를 매개변수로 받아서 요청 Header에 있는 토큰으로 user_ID 추출
     * 21.05.22 인준 : 예약 신청시 사용자의 인적사항도 함께 저장 및 트랜잭션 처리 구현 완료
     */
    @PostMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void applyReservation(@RequestBody ReservationApplicationInfoDTO dto, Principal principal) throws Exception 
    {
    	String userId = principal.getName();
    	dto.getUserEntity().setUserId(userId);
    	dto.getReservationInfoEntity().setUserId(userId);
    	reservationService.applyReservation(dto);
    }

    
    /**
     * @description [마이페이지] 본인 예약 이력 및 현황 확인하기
     * 2021.04.21 user_ID를 url로 받아와서 해당 사용자의 예약 이력정보를 조회 (로그인 구현후 수정 예정)
     * 2021.05.19 로그인 기능 구현 - Controller Method 매개변수로 Principal를 할당해서 .getName() 함수로 user_ID값 가져오기  
     */
    @GetMapping(value = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<MyReservationDTO> getMyReservations(Principal principal) throws Exception 
    {
    	String userId = principal.getName(); 
    	return reservationService.getMyReservationList(userId);
    }
    
    
    /**
     * @description [마이페이지] 예약수정하기 (수정할 예약정보 가져오기) 
     * <추가 수정 요구사항> 21.04.22 인준 : 권한관리 (비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.) / 반환되는 JSON객체의 null값 제거하기!
     * 21.05.21 인준 : Service 레이어에서 받아온 Data를 user_ID로 검증해서 사용자 본인 예약정보만 호출할 수 있도록 작업 / JsonObject를 새로 생성해서 return(Front에서 필요한 데이터만 .addProperty() 함수로 넘겨줌) 
     */
    @GetMapping(value = "/reservation/{res_ID}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public JsonObject getReservationForUpdate(@PathVariable("res_ID") int res_ID, Principal principal) throws Exception {
    	// 요청Header에서 Auth객체 받아와서 user_ID값 가져오기
    	String user_ID = principal.getName();
    	
    	// Service 레이어로 넘길 Map객체 생성
    	Map map = new HashMap<String, String>();
    	map.put("user_ID", user_ID);
    	map.put("res_ID", res_ID);
    	
    	// Service 레이어에서 Data 반환
    	ReservationInfoDTO result = reservationService.getReservationForUpdate(map);
    	JsonObject jsonObj = new JsonObject();

		// Service 레이어에서 받은 reservation_info 데이터의 user_ID 검증 : 유효하지 않을 경우 데이터 반환X
    	// String 자료형의 값의 일치 여부를 검증하기 위해 .equals() 함수를 이용 
		if (!result.getUser_ID().equals(user_ID)) {
			jsonObj.addProperty("error", "예약한 사용자가 아닙니다.");
			return jsonObj;
		}
		
		// res_ID값과 수정하고자 하는 값 json객체에 다시 담아주기
		jsonObj.addProperty("res_ID", res_ID);
		jsonObj.addProperty("stay_purpose", result.getStay_purpose());
		jsonObj.addProperty("num_of_guests", result.getNum_of_guests());
		jsonObj.addProperty("message", result.getMessage());
		return jsonObj;
    }
    
    /**
     * @description [마이페이지] 예약수정하기 (사용자가 입력한 예약정보로 수정하기) 
     * 21.05.23 인준 : 예약ID로 사용자ID조회해서 예약정보 수정요청을 보낸 사용자ID와 비교. 두 사용자ID가 동일하면 예약정보 Update 
     */
    @PutMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updateReservation(@RequestBody ReservationInfoDTO reservationInfoDTO, Principal principal) throws Exception {
    	// reservationInfoDTO 객체를 map형태로 변환하기 위한 ObjectMapper객체 선언
    	ObjectMapper objectMapper = new ObjectMapper();
    	Map map = objectMapper.convertValue(reservationInfoDTO, Map.class);
    	
    	// 사용자ID를 map객체에 추가로 삽입
    	String user_ID = principal.getName();
    	map.put("user_ID", user_ID);
    	
    	return reservationService.updateReservation(map);
    }
    
    /** principal 추가 요망!!!###########################____________________________
     * @description [마이페이지] 예약취소하기  
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 권한관리 (비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함.) / 수정데이터를 받을 객체 수정(수정항목만 받을 객체로 생성) / DAO호출 2개하는 것을 프로시저로 작성!
     * 21.08.02 인준 : principal을 통해 user_ID를 DTO에 담아서 Service Layer로 넘겨줌. 예약상태 테이블 변경할때 user_ID값도 조건으로 넣어서 SQL update실행
     */
    @PutMapping(value = "/reservation/cancel", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String cancelReservation(@RequestBody ReservationCancelDTO reservationCancelDTO, Principal principal) throws Exception {
    	// Principal에서 사용자 아이디 가지고 와서 DTO에 할당
    	String user_ID = principal.getName();
    	reservationCancelDTO.setUser_ID(user_ID);
    	    	
    	Boolean isSuccess = reservationService.cancelReservation(reservationCancelDTO);
    	if (isSuccess) {
        	return "성공!";
    	} else {
        	return "실패!";
    	}

    }
}// End
