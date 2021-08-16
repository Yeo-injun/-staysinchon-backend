package com.project.sinchon.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationStateEntity {
	private int resId;
	private String userId;
	private int state; // 1 : 예약신청, 2 : 예약확정, 3 : 예약취소
	private Date applyDate;
	private Date confirmDate;
}
