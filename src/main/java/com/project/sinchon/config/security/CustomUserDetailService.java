package com.project.sinchon.config.security;

import com.project.sinchon.dao.UserDAO;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;
	
	// 회원가입 처리
	public int registerUser(User userInfo) {
		return userDAO.registerUser(userInfo);
	}
	
	// 로그인시 토큰 검증용 
	@Override
	public User loadUserByUsername(String user_ID) throws UsernameNotFoundException {
		User user = userDAO.findByUserId(user_ID);
		if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", user_ID));
        } else {
            return user;
        }
	}


}
