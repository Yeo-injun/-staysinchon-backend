package com.project.sinchon.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@Setter
public class UserEntity {
    private String userId;
    private String pwd;
    private String email;
    private Date regDate;
    private Date updateDate;
    private String firstname;
    private String lastname;
    private int sex;
    private String country;
    private int ageGroup;
    private String NaFoods;
}