package com.project.sinchon.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
public class MyReservationDTO {
    private int resId;
    private Date resDate;
    private Date checkIn;
    private Date checkOut;
    private String stayPurpose;
    private int numOfGuests;
    private String message;
    private int payment;
    private String userId;
    private int roomId;
    private String roomName;
    private int state;     // 예약상태 테이블의 state속성값 받아주는 필드
}
