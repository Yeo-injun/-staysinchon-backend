package com.project.sinchon.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewEntity {
    private int resId;
    private String userId;
    private String contents;
    private int grade;
    private Date regDate;
    private int regroupId;
    private int reparentsId;
    private int depth;
}
