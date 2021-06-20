package com.project.sinchon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
 *
 * title : cors 설정구간
 * author : 정효인
 * date : 2021.03.13
 *		21.06.20 인준 : Security에서 CorsConfig 설정해서 해당 내용 삭제(주석처리)
 * */

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//        		.allowedMethods("*")
//        		.allowCredentials(true)
//        		.maxAge(3600L);
//        		
//    }
//
//}