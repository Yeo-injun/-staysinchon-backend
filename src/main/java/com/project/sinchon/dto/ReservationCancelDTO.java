package com.project.sinchon.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReservationCancelDTO {
	private int res_ID;
	private String content;
	private Date cancel_date;
}
