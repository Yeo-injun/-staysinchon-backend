package com.project.sinchon.dto;

import lombok.Data;

import java.util.Date;

/**
 * title : 예약정보 테이블 DTO
 * author : 정효인
 * create_date : 2021.03.24
 * update 
 * 2021.04.21 : 여인준 / 결제금액(payment) 속성 추가, join테이블 VO 속성 추가
 *
 * */
@Data
public class ReservationInfoDTO {
    private int res_ID;
    private Date res_date;
    private Date check_in;
    private Date check_out;
    private String stay_purpose;
    private int num_of_guests;
    private String message;
    private int payment;
    private String user_ID;
    private int room_ID;
    
    // 예약상태 테이블의 state속성값 받아주는 필드
    private int state; 
    
    // join 데이터 받아올 객체 속성 추가
    private UserDTO userDTO;
    private ReservationStateDTO reservationStateDTO;
    
}
