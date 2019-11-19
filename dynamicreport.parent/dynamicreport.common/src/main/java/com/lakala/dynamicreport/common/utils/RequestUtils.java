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
package com.lakala.dynamicreport.common.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 请求工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class RequestUtils {

	/**
	 * 获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest request() {
		HttpServletRequest request = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			request = ((ServletRequestAttributes) requestAttributes).getRequest();
		}
		return request;
	}

	/**
	 * 获取参数值
	 * 
	 * @param param
	 * @return
	 */
	public static String getRequestParam(String param) {
		HttpServletRequest request = request();
		if (request != null) {
			return request.getParameter(param);
		}
		return null;
	}

	/**
	 * 获取response
	 * 
	 * @return
	 */
	public static HttpServletResponse response() {
		HttpServletResponse response = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			response = ((ServletRequestAttributes) requestAttributes).getResponse();
		}
		return response;
	}

	/**
	 * 获取session
	 * 
	 * @return
	 */
	public static HttpSession session() {
		HttpSession session = null;
		HttpServletRequest request = request();
		if (request != null) {
			session = request.getSession();
		}
		return session;
	}

	/**
	 * 获取session值
	 * 
	 * @param key
	 * @return
	 */
	public static Object getSessionValue(String key) {
		HttpSession session = session();
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	/**
	 * 设置session值
	 * 
	 * @param key
	 * @param value
	 */
	public static void setSessionValue(String key, Object value) {
		HttpSession session = session();
		if (session != null) {
			session.setAttribute(key, value);
		}
	}
}
