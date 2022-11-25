package com.innople.devpleyground.dpfpapi.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Log implements Serializable {
    private static final long serialVersionUID = -5014673261545532911L;

    private String targetType;
    private String serverIp;
    private String methodName;
    private String arguments;

    private String startTime;
    private String endTime;
    private Long duration;

    private String requestURL;
    private String requestMethod;

    private String clientIP;
    private String userId;

    private String exception;
    private String exceptionInfo;


    public Log(String initTime){
        this.targetType = "";
        this.serverIp = "";
        this.methodName = "";
        this.arguments = "";
        this.startTime = initTime;
        this.endTime = initTime;
        this.duration = 0L;
        this.requestURL = "";
        this.requestMethod = "";
        this.clientIP = "";
        this.userId = "";
        this.exception = "";
        this.exceptionInfo = "";
    }
}