package com.project.sinchon.dto;

import java.util.Date;

import lombok.Data;

/**
 * title : 리뷰 정보 DTO
 * author : 정효인
 * date : 2021.03.24
 *
 * */

@Data
public class ReviewDTO {
    private int res_ID;
    private String user_ID;
    private String contents;
    private int grade;
    private Date reg_date;
    private int regroup_ID;
    private int reparents_ID;
    private int depth;
}
