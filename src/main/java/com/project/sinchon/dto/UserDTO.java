package com.project.sinchon.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * title : User 테이블 DTO
 * author : 정효인
 * date : 2021.03.24
 *
 * */
@Component
@Data
public class UserDTO {
    private String user_ID;
    private String pwd;
    private String email;
    private Date reg_date;
    private Date update_date;
    private String firstname;
    private String lastname;
    private int sex;
    private String country;
    private int age_group;
    private String NA_foods;
    
}