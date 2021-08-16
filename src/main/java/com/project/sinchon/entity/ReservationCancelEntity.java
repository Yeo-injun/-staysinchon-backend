package com.project.sinchon.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationCancelEntity {
	private int resId;
	private String content;
	private Date cancelDate;
}
