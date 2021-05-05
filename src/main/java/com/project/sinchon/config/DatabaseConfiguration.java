package com.project.sinchon.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;


/*
 *
 * title : 데이터베이스 설정구간(커넥션 풀)
 * author : 정효인
 * date : 2021.03.24
 *
 * */

@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {


    @Autowired
    ApplicationContext applicationContext;



//    hikari 라는 여석은 Connection pool을 말한다

//    사용자의 요청에 따라 Connection 을 생성하다 보면 많은 수의 연결이 발생했을 때 서버에 과부하가 걸리게 된다 .
//    이러한 상황을 방지하기 위해 미리 일정수의 Connection 을 만들어 pool 에 담아 뒀다가
//    사용자의 요청이 발생하면 연결을 해주고 연결종료 시 pool 에 다시 반 환하여 보관하는 것이다 .
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }


    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new HikariDataSource(hikariConfig());
        log.info("datasource : {}", dataSource);
        return dataSource;
    }

//    실직적으로 MySql 서버와 mybatis를 연결해주는 건  sqlSessionFactory 이 객체이다(스프링 지원 라이브러리임)
//    이를 이용해 mybatis를 사용할수 있게 경로를 지정하여 사용하는 구간이다.(이 프로젝트에서는 resources/mapper/testDB.xml 을 사용하고 있다.)
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
