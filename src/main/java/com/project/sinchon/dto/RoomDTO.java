package com.project.sinchon.dto;

import lombok.Data;

/**
 * title : 방 정보 DTO
 * author : 정효인
 * date : 2021.03.24
 * update log 
 * 		2021.04.10 : 여인준 / DB재설계에 따른 capacity 속성 및 getter setter 추가  
 **/
@Data
public class RoomDTO {
    private int room_ID;
    private String room_type;
    private String room_img;
    private String bed;
    private String bathroom;
    private	int capacity;
    private int price_day;
    private int price_month;
}
