package com.project.sinchon.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractResponseResultDTO {
	private boolean isSuccess; // 서비스 처리 성공여부
	private boolean isDataExisted; // 서비스 처리 결과 Data존재여부 >> id중복여부, 프로필 존재여부 체크용
	// 페이징을 위한 필드값 추가 예정
}
