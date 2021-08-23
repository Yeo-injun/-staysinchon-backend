package com.project.sinchon.dto;

import lombok.Builder;
import lombok.Data;
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
import org.springframework.stereotype.Component;

import com.project.sinchon.entity.UserEntity;


@Component
@Getter
@Setter
public class UserProfileDTO {
    
	private UserEntity userEntity;
	
    // 예약신청시 User Profile값 존재유무 값 체크
    private boolean userInfoYn;
}