# 신촌 쉐어하우스 예약 관리 시스템 구축 프로젝트
### 구성원: 박지영, 여인준, 조주영, 정효인
___



## 기술스택
___

<백엔드>

작업자 : 여인준, 정효인
* springBoot
* mybatis
* mysql(mariaDB)
* java 1.15 version  
* 로컬에서 작업시 서버 localhost:8080


<프론트엔드>

작업자 : 박지영, 조주영 
* react
* 로컬에서 작업시 서버 localhost:3000

## 초기설정
___

> Intelli J 의 springBoot stater를 이용하여 템플릿 구성

sinchon (루트 디텍터리)  
|  
ㄴㅡㅡㅡfrontend (프론트엔드 (리액트 프로젝트))  
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;         |  
|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   ㄴㅡㅡㅡ src (리액트 프로젝트 존재)   
|  
ㄴㅡ src   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ   main  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ java...  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ config  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ controller  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ dao  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ service    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ vo  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ SinchonApplication.class  

config/DatabaseConfiguration  --> mybatis를 사용하기 위한 sqlSession 설정 및 마이바티스 설정 파일  
config/SwaggerConfig --> 프론트엔드와 협업을 위해서 restful데이터 체크  
config/WebConfig --> cors(교차 출처 리소스 공유) 리액트(브라우저)와 스프링 사이의 통신을 위한 설정  

SinchonApplication.class 스프링 스타트 클래스(스타트 메서드)


controller --> mvc 구조중 c(controller) 역할을 하는 폴더  
service --> mvc 구조중 m(model)역할을 하는 폴더  
dao --> mybatis에게 sql를 통한 데이터 요청을 하는 폴더 (이또한 모델 역할)  
vo --> getter/setter 역할을 하는 폴더  
  
resources/application.properties --> db환경설정이 포함되어 있다.(설정 관리 파일)  
resources/mapper/**.xml --> mybatis 라이브러리를 사용하는 구간(SQL문법을 이용하여 DB와의 데이터 작업을 한다.)박

___

> backend start
 
 SinchonApplication.class 파일을 (스타트클래스이자 메서드입니다.) run해준다.  
 방법은 우측 상단 스타트버튼 || 해당 클래스의 오른쪽 버튼 클릭후 run
 
> frontend start

frontend의 위치까지 터미널을 이용하여 이동한다.   

> npm run start

 swagger 사용방법

> http://localhost:8080/swagger-ui.html#