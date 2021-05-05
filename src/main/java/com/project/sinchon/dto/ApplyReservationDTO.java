package com.project.sinchon.dto;

import lombok.Data;

import java.util.Date;

/**
 * title : 게스트가 예약신청한 데이터를 받는 VO
 * author : 여인준
 * date : 2021.04.14
 *
 * */

@Data
public class ApplyReservationDTO {
	// User테이블 컬럼과 Mapping
	private String user_ID;
	private String firstname;
	private String lastname;
	private int sex; // DB 자료형기준 (0 : 남, 1 : 여)
	private String country;
	private int age_group;
	private String NA_foods;
	
	// reservation_info테이블 컬럼과 Mapping
	private int res_ID;
	private int room_ID;
	private Date check_in;
	private Date check_out;
	private String stay_purpose;
	private int num_of_guests;
	private int payment;
	private String message; // 기타

}
