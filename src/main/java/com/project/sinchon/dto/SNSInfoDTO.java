package com.project.sinchon.dto;

import lombok.Data;

/**
 * title : sns로그인 정보 DTO
 * author : 정효인
 * date : 2021.03.24
 *
 * */

@Data
public class SNSInfoDTO {
    private String SNS_ID;
    private String SNS_type;
    private String user_ID;
}
