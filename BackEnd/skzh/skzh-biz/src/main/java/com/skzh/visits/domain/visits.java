package com.skzh.visits.domain;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class visits {

    private Long infoId;


    private String userName;


    private String status;

    private String ipaddr;


    private String loginLocation;


    private String browser;


    private String os;


    private String msg;


    private LocalDateTime loginTime;





}
