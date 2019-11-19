/*
 * Copyright (c) 2019-2021, LaKaLa.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lakala.dynamicreport.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.utils.DateUtils;
import com.lakala.dynamicreport.common.utils.IPAddressUtil;
import com.lakala.dynamicreport.system.model.SysLog;
import com.lakala.dynamicreport.system.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 日志拦截器，用于拦截Controller类中的方法
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Aspect
@Component
public class ControllerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Autowired
    ISysLogService sysLogService;

    /**
     * 定义拦截规则：拦截Controller类中
     */
    @Pointcut("execution(public * com.lakala.dynamicreport.*.controller..*.*(..))")
    public void controllerMethodPointcut() {
    }

    /**
     * 请求方法前进行拦截
     *
     * @param joinPoint
     * @throws Exception 
     */
    @Before("controllerMethodPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String requestURL=request.getRequestURI();
        //日志输出
        getLogOut(request,joinPoint);
        //whiteList 白名单用于存储需要拦截的URL,假如whiteList.add("/tenant/save")，将拦截多租户系统中租户(Controller)保存时的信息
        List<String> whiteList=new ArrayList<String>();
        //whiteList.add("/tenant/save");
         for(String url:whiteList){
            if(requestURL.indexOf(url)!=-1){
                SysLog sysLog = new SysLog();
                Session session = SecurityUtils.getSubject().getSession();
                ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
                String userName="";
                if (userInfo != null) {
                    userName = userInfo.getUsername();
                } else {
                    userName = "System";
                }
                //请求的参数
                Object[] args = joinPoint.getArgs();
                StringBuffer sb = new StringBuffer();
                if(args!=null){
                    for (Object object : args){
                         if(object != null){
                             if (object instanceof MultipartFile||object instanceof ServletRequest||object instanceof ServletResponse){
                                 continue;
                             }
                             sb.append(JSONObject.toJSONString(object)).append(GlobalConstants.COMMA); 
                           }
                    }
                }
                sysLog.setParams(sb.toString());
                String ip = IPAddressUtil.getClientIpAddress(request);
                sysLog.setUserName(userName);
                sysLog.setUrl(request.getRequestURI());
                sysLog.setIp(ip);
                sysLog.setType(GlobalConstants.MSG_TYPE_NORMAL);
                sysLog.setCreateDate(DateUtils.nowTimeStamp());
                sysLogService.save(sysLog);
            }
          }
       }

    private void getLogOut(HttpServletRequest request,JoinPoint joinPoint) {
        // 请求 url
        logger.info("URL            : {}", request.getRequestURL().toString());
        //  Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        // 调用 controller 的全路径以及执行方法
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 请求的 IP
        logger.info("IP             : {}", request.getRemoteAddr());
        //类描述
        Class<?> controller = joinPoint.getThis().getClass();
        Api ann = controller.getAnnotation(Api.class);
//        logger.info("Controller Description     : {}",ann.tags());

        // 操作描述
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        ApiOperation webLogger = method.getAnnotation(ApiOperation.class);
//        logger.info("Operation Description     : {}", webLogger.value());

        //请求的参数
        Object[] args = joinPoint.getArgs();
        StringBuffer sb = new StringBuffer();
        if(args!=null){
            for (Object object : args){
                if(object != null){
                    if (object instanceof MultipartFile||object instanceof ServletRequest||object instanceof ServletResponse){
                        if (object instanceof ServletRequest) {
                            logger.info("Request Params         :{}",JSONObject.toJSONString(((ServletRequest) object).getParameterMap()));
                        }
                        continue;
                    }
                    sb.append(JSONObject.toJSONString(object)).append(GlobalConstants.COMMA);
                }
            }
        }
        logger.info("Operation Description     : {}-{}-{}",ann.tags(),webLogger.value(),sb.toString());
    }
}
 
