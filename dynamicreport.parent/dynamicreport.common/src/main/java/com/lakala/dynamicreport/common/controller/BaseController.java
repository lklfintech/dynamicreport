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
package com.lakala.dynamicreport.common.controller;

import com.lakala.dynamicreport.common.editor.DateEditor;
import com.lakala.dynamicreport.common.editor.TimestampEditor;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.model.ResultSysLog;
import com.lakala.dynamicreport.common.utils.DateUtils;
import com.lakala.dynamicreport.common.utils.IPAddressUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * 基础controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class BaseController {
 
	
	@InitBinder
	protected void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		webDataBinder.registerCustomEditor(Date.class, new DateEditor(true));
		webDataBinder.registerCustomEditor(Timestamp.class, new TimestampEditor(true));
	}

	/**
	 * 成功操作
	 * 
	 * @param msg
	 * @return
	 */
	protected ResultJson success(String msg) {
		ResultJson result = new ResultJson();
		result.setSuccess(true);
		result.setMsg(msg);
		return result;
	}

	/**
	 * 成功操作
	 *
	 * @param msg
	 * @return
	 */
	protected ResultJson success(String msg,String url) {
		ResultJson result = new ResultJson();
		result.setSuccess(true);
		result.setMsg(msg);
		result.setUrl(url);
		return result;
	}

	/**
	 * 失败操作
	 * 
	 * @param msg
	 * @return
	 */
	protected ResultJson failure(String msg) {
		ResultJson result = new ResultJson();
		result.setSuccess(false);
		result.setMsg(msg);
		return result;
	}

	/**
	 * 失败操作
	 * 
	 * @param msg
	 * @param e
	 * @return
	 */
	protected ResultJson failure(String msg, Exception e) {
		ResultJson result = new ResultJson();
		result.setSuccess(false);
		if (e != null && e.getCause() instanceof ConstraintViolationException) {
			result.setMsg("外键关联，删除失败，要删除此记录请先删除关联表数据！");
		} else {
			result.setMsg(msg);
		}
		return result;
	}
	
	
	/**
	 * 记录日志
 	 * @param msg
	 * @return ResultSysLog
	 */
	protected ResultSysLog saveSysLog(String msg,String params,String userName,String type) {
		ResultSysLog sysLog=new ResultSysLog();
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
 		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		String ip = IPAddressUtil.getClientIpAddress(request);
        sysLog.setUserName(userName);
        sysLog.setUrl(request.getRequestURI());
        sysLog.setIp(ip);
        sysLog.setMessage(msg);
        sysLog.setType(type);
        sysLog.setParams(params);
        sysLog.setCreateDate(DateUtils.nowTimeStamp());
        return sysLog;
	}
}
