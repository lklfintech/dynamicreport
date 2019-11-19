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
package com.lakala.dynamicreport.exception;

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.utils.DateUtils;
import com.lakala.dynamicreport.common.utils.IPAddressUtil;
import com.lakala.dynamicreport.system.model.SysLog;
import com.lakala.dynamicreport.system.service.ISysLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用于全局的Exception处理,在controller也可以单独写try,catch。如果controller不写将统一跳到此类中处理
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    protected Logger log =  LoggerFactory.getLogger(this.getClass());
    
	private final static String MSG = "系统错误,请检查!";
	
	@Autowired
	ISysLogService sysLogService;
	
    @ExceptionHandler(value = Exception.class)
    public ResultJson defaultErrorHandler(Exception e, HttpServletRequest request) throws Exception {
    	ResultJson resultJson =new ResultJson();
    	if (log.isInfoEnabled()) {
    		log.info("请求地址：{}" + request.getRequestURL());
    		log.error("异常信息：{}",e);
		}
    	SysLog sysLog=new SysLog();
 		String ip = IPAddressUtil.getClientIpAddress(request);
 		Session session = SecurityUtils.getSubject().getSession();
        ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
        String userName="";
        if (userInfo != null) {
             userName = userInfo.getUsername();
         } else {
             userName = "System";
        }
        sysLog.setUserName(userName);
        sysLog.setUrl(request.getRequestURI());
        sysLog.setIp(ip);
        sysLog.setMessage(e.toString());
        sysLog.setType(GlobalConstants.MSG_TYPE_ERROR);
        sysLog.setCreateDate(DateUtils.nowTimeStamp());
        sysLogService.save(sysLog);
        resultJson.setMsg(MSG);
        resultJson.setSuccess(false);
        return resultJson;
    }

}
