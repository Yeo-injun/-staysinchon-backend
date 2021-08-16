package com.project.sinchon.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RoomEntity {
    private int roomId;
    private String roomName;
    private String roomType;
    private String roomImg;
    private String bed;
    private String bathroom;
    private	int capacity;
    private int priceDay;
    private int priceMonth;
}
