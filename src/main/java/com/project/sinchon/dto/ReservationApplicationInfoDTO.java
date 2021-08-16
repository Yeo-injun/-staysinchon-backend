package com.project.sinchon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import com.project.sinchon.entity.ReservationInfoEntity;
import com.project.sinchon.entity.UserEntity;


@Getter
@Setter
public class ReservationApplicationInfoDTO {
	private Boolean orUpdate; 
	
	// User테이블 컬럼과 Mapping
	private UserEntity userEntity;
	
	// reservation_info테이블 컬럼과 Mapping
	private ReservationInfoEntity reservationInfoEntity;

}
