package com.project.sinchon.dto;

import java.util.Date;

import lombok.Data;

/**
 * title : 예약상태 테이블 DTO
 * author : 여인준
 * create_date : 2021.04.21
 * update 
 * 
 * */
@Data
public class ReservationStateDTO {
	private int res_ID;
	private String user_ID;
	private int state; // 1 : 예약신청, 2 : 예약확정, 3 : 예약취소
	private Date apply_date;
	private Date confirm_date;
}
