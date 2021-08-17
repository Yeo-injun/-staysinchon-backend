package com.project.sinchon.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReservationInfoEntity {
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
}
