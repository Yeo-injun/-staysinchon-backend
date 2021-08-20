package com.project.sinchon.dto;

import java.util.Date;


import lombok.Getter;
import lombok.Setter;

/* 21.08.20 DTO유지 */
@Getter
@Setter
public class ReservationCancelDTO {
	private String userId;
	private int resId;
	private String content;
}
