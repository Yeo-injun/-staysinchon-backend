package com.project.sinchon.config.security;

import com.project.sinchon.config.security.auth.User;
import com.project.sinchon.dao.UserDAO;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;	
	
	// 로그인시 토큰 검증용 
	@Override
	public PrincipalDetails loadUserByUsername(String user_ID) throws UsernameNotFoundException {
		User user = userDAO.findByUserId(user_ID);
		if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with user '%s'.", user_ID));
        } else {
            return new PrincipalDetails(user);
        }
	}
}
