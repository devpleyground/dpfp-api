package com.innople.devpleyground.dpfpapi.common.aspects;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.innople.devpleyground.dpfpapi.common.dto.Log;
import com.innople.devpleyground.dpfpapi.common.utils.ObjectTransfer;
import com.innople.devpleyground.dpfpapi.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Aspect
@Slf4j
public class LogAspect {
    private final HttpServletRequest request;
    private final ZoneId serviceZoneId = ZoneOffset.UTC;
    private final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LogAspect(HttpServletRequest request) {
        this.request = request;
    }

    private Log logInfo;

    @Before("execution(* com.innople.devpleyground.dpfpapi.controllers..*(..)) " +
            "&& !execution(* com.innople.devpleyground.dpfpapi.*.getHealth())")
    public void doLoggingBefore(JoinPoint joinPoint) {
        try {
            List<String> list = new ArrayList<String>();
            logInfo = new Log(LocalDateTime.now(serviceZoneId).format(defaultDateTimeFormatter));

            logInfo.setTargetType(joinPoint.getSignature().getDeclaringTypeName());
            logInfo.setServerIp(request.getRemoteAddr());
            logInfo.setMethodName(joinPoint.getSignature().getName());
            logInfo.setClientIP(getRemoteIP(request));
            logInfo.setRequestMethod(request.getMethod());
            logInfo.setRequestURL(request.getServletPath());
            if(request.getHeader("X-Consumer-Username") != null){
                logInfo.setUserId(request.getHeader("X-Consumer-Username"));
            }

            // Make Parameters
            StringBuilder params = new StringBuilder();
            if(joinPoint.getArgs() != null){
                for(int i = 0; i < joinPoint.getArgs().length; i++){
                    if(joinPoint.getArgs()[i] instanceof String){
                        params.append(joinPoint.getArgs()[i]);
                    } else if (joinPoint.getArgs()[i] instanceof org.springframework.web.multipart.MultipartFile){
                        Map<String, String> fileInfo = new HashMap<String, String>();
                        org.springframework.web.multipart.MultipartFile file
                                = (org.springframework.web.multipart.MultipartFile) joinPoint.getArgs()[i];
                        fileInfo.put("fileName", file.getOriginalFilename());
                        fileInfo.put("fileSize", Long.toString(file.getSize()));
                        fileInfo.put("fileContentType", file.getContentType());
                        params.append(ObjectTransfer.toJson(fileInfo));
                    } else {
                        // encrypt password parameter for log
                        JsonElement jsonArg = ObjectTransfer.toObject(joinPoint.getArgs()[i], JsonElement.class);
                        StringUtil.encryptJsonElementByKey("password", jsonArg, list);
                        // masking for cellphone number
                        String stringArg = ObjectTransfer.toJson(jsonArg);
                        stringArg = stringArg.replaceAll("01{1}[016789]{1}-[0-9]{3,4}-[0-9]{4}", "***-****-****");
                        stringArg = stringArg.replaceAll("01{1}[016789]{1}[0-9]{7,8}", "***********");

                        params.append(stringArg);
                    }

                    if((i != joinPoint.getArgs().length -1)){
                        params.append("&");
                    }
                }
            }

            logInfo.setArguments(params.toString());

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    @After("execution(* com.eland.signtogetherapi.controllers..*(..)) && !execution(* com.eland.signtogetherapi.controllers.*.getHealth())")
    public void doLoggingAfter(JoinPoint joinPoint) {
        try{
            logInfo.setEndTime(LocalDateTime.now(serviceZoneId).format(defaultDateTimeFormatter));
            logInfo.setDuration(calculateDuration(logInfo.getEndTime(), logInfo.getStartTime()));
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @AfterReturning(pointcut="execution(* com.eland.signtogetherapi.controllers..*(..)) && !execution(* com.eland.signtogetherapi.controllers.*.getHealth())")
    public void doLoggingAfterReturning(JoinPoint joinPoint){
        try {
            log.info(ObjectTransfer.toJson(logInfo));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    @AfterThrowing(pointcut = "execution(* com.eland.signtogetherapi.controllers..*(..)) && !execution(* com.eland.signtogetherapi.controllers.*.getHealth())", throwing = "e")
    public void doLoggingAfterThrowing(JoinPoint joinPoint, Exception e){
        try {
            //Exception
            logInfo.setEndTime(LocalDateTime.now(serviceZoneId).format(defaultDateTimeFormatter));
            logInfo.setDuration(calculateDuration(logInfo.getEndTime(), logInfo.getStartTime()));
            logInfo.setException(e.getClass().getCanonicalName());
            logInfo.setExceptionInfo(e.getMessage());

            log.info(ObjectTransfer.toJson(logInfo));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Long calculateDuration(String before, String after){
        long duration = -1L;
        try {
            Duration durationSeed
                    = Duration.between(LocalDateTime.parse(after, defaultDateTimeFormatter), LocalDateTime.parse(before, defaultDateTimeFormatter));

            duration = durationSeed.getSeconds();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return duration;
    }

    private static String getRemoteIP(HttpServletRequest request){
        //X-Forwarded-For: client, proxy1, proxy2
        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }

        return ip;
    }
}
