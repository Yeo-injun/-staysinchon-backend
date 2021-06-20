package com.project.sinchon.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* 어노테이션 방식의 필터 등록
 * @Component이 있다면 @SpringBootApplication 실행파일에 @ServletComponentScan 작성할 필요 없음
 * @SpringBootApplication 내부에 @ComponentScan이 내장되어 있기 때문
 * 		관련 자료 : https://taetaetae.github.io/2020/04/06/spring-boot-filter/
 * 다른 방식 : WebMvcConfigurer를 구현하는 FilterConfig 자바 파일에 FilterRegistrationBean으로 등록하면 됨
 * */
@Component //  
@WebFilter(urlPatterns= "/**") 
public class SpringCorsFilter implements Filter {

private final Logger log = LoggerFactory.getLogger(SpringCorsFilter.class);

public SpringCorsFilter() {
    log.info("_____________CorsFilter init");
}

@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    log.info("_____________CorsFiler execute!!");
	HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); // (인준) 특정 값으로 한정시킬 필요 있음
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
    System.out.println(response.toString());
    chain.doFilter(req, res);
}

@Override
public void init(FilterConfig filterConfig) {
}

@Override
public void destroy() {
}

}
