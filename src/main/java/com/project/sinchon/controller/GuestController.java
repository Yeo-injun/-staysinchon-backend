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
import com.project.sinchon.dto.ApplyReservationDTO;
import com.project.sinchon.dto.ReservationCancelDTO;
import com.project.sinchon.dto.ReservationInfoDTO;
import com.project.sinchon.dto.RoomDTO;
import com.project.sinchon.dto.UserDTO;
import com.project.sinchon.service.ApplyReservaionService;
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
 * 
 * */

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/*")
public class GuestController {
    
    @Autowired // 확인사항 : Inject과 차이
    private RoomService roomService;
    
    @Autowired
    private ApplyReservaionService applyReservationService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private UserService userService;
    
    /**
     * @description [예약페이지] 모든 방 조회
     */
    @GetMapping(value = "/rooms", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> roomList() throws Exception{
    	System.out.println("URL 요청됨");
        return roomService.getList();
    }
    
    /**
     * @description [예약페이지] 예약가능한 방 기본값 조회(기본값 : 현재일 기준 1박 2일 예약가능한 방)
     */
    @GetMapping(value = "/rooms/available", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<RoomDTO> roomAbleList(Authentication auth) throws Exception{
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
     *  21.05.23 인준 : 사용자의 인적사항이 있다면 인적사항 데이터 같이 보내주기 구현(reg_date와 update_date가 다르면 인적사항이 있다는 것) 
     *  21.07.20 인준 : Post메소드에서 Get메소드로 전환(React에서 화면전환시 Link 태그에 데이터를 넘겨주고, 전환된 화면ㄷ에서 {location}를 파라미터로 받아서 사용 가능해서) 
     */
    @GetMapping(value = "/reservation/form", produces = {MediaType.APPLICATION_JSON_VALUE})
    public JsonObject reservationForm(Principal principal) throws Exception{
    	// Gson모듈내 JsonObject클래스로 객체 선언 : Map으로 받은 데이터 JSON형태로 변환하기 위함
    	JsonObject jsonObj = new JsonObject();
    	
    	// 인증된 사용자의 user데이터 가져오기  
    	Map<String, String> map = new HashMap<String, String>();
    	String user_ID = principal.getName();
    	map.put("user_ID", user_ID);
    	UserDTO userDTO = userService.getUserDetails(map);
    	
		// reg_date와 update_date가 다른지 확인 : reg_date - update_date 값이 다르면 사용자 인적사항 return해주기
		if (!userDTO.getReg_date().equals(userDTO.getUpdate_date())) {
			jsonObj.addProperty("userInfo", true);
			
			// 사용자 인적사항 JsonObj에 넣어주기
			jsonObj.addProperty("firstname", userDTO.getFirstname());
			jsonObj.addProperty("lastname", userDTO.getLastname());
			jsonObj.addProperty("sex", userDTO.getSex()); 
			jsonObj.addProperty("country", userDTO.getCountry());
			jsonObj.addProperty("NA_foods", userDTO.getNA_foods());
			jsonObj.addProperty("age_group", userDTO.getAge_group());			
		} else {
			jsonObj.addProperty("userInfo", false);
		}
    	
    	return jsonObj;
    }

    /**
     * @description [예약페이지] 예약신청하기 
     * 프론트엔드와 통신시 RequestBody의 데이터 형태 확인
     * <추가 수정 요구사항>
     * 21.04.22 인준 : 비회원에 대한 접근을 막고, 로그인한 user_ID 정보를 사용해야 함. 
     * 21.05.20 인준 : Security 구현으로 회원일 경우에만 접근가능하고, Principal객체를 매개변수로 받아서 요청 Header에 있는 토큰으로 user_ID 추출
     * 21.05.22 인준 : 예약 신청시 사용자의 인적사항도 함께 저장 및 트랜잭션 처리 구현 완료
     */
    @PostMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void applyReservation(@RequestBody ApplyReservationDTO applyReservationDTO, Principal principal) throws Exception {
    	// 사용자 입력 데이터가 JSON형태로 들어와서 @RequestBody를 거쳐서 VO객체의 변수들과 매핑됨
    	// user_ID는 Principal을 매개변수로 받아서 .getName() 함수로 user_ID값 받아옴
    	String user_ID = principal.getName();
    	applyReservationDTO.setUser_ID(user_ID);
    	
    	applyReservationService.insertReservation(applyReservationDTO);
    }

    /**
     * @description [마이페이지] 본인 예약 이력 및 현황 확인하기
     * 2021.04.21 user_ID를 url로 받아와서 해당 사용자의 예약 이력정보를 조회 (로그인 구현후 수정 예정)
     * 2021.05.19 로그인 기능 구현 - Controller Method 매개변수로 Principal를 할당해서 .getName() 함수로 user_ID값 가져오기  
     */
    @GetMapping(value = "/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<ReservationInfoDTO> getMyReservations(Principal principal) throws Exception {
    	// 로그인한 사용자의 Token에 있는 정보로 user_ID접근
    	String user_ID = principal.getName(); 
    	
    	// map자료구조에 user_ID값을 담고
    	// map을 인자로 넣어줘 Service 레이어 호출
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("user_ID", user_ID);
    	List<ReservationInfoDTO> myReservationList = reservationService.getMyReservationList(map);
    	System.out.println(myReservationList.size());
    	
    	return myReservationList;
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
