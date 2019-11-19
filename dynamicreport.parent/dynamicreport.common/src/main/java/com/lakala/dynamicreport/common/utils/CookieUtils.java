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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * cookie工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class CookieUtils {
	
	private static final Logger log = LoggerFactory.getLogger(CookieUtils.class);
	public static final int COOKIE_MAX_AGE = 365 * 24 * 3600;
	public static final int COOKIE_HALF_HOUR = 30 * 60;
	// hide public constructors
	private CookieUtils() {
		throw new IllegalStateException("CookieUtils class");
	}
	/**
	 * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (ArrayUtils.isEmpty(cookies)) {
			return null;
		}
		Cookie cookie = null;
		for (Cookie c : cookies) {
			if (name.equals(c.getName())) {
				cookie = c;
				break;
			}
		}
		return cookie;
	}

	/**
	 * 根据Cookie名称直接得到Cookie值
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		if (cookie != null) {
			return cookie.getValue();
		}
		return null;
	}

	/**
	 * 移除cookie
	 * 
	 * @param request
	 * @param response
	 * @param name
	 *            这个是名称，不是值
	 */
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		if (null == name) {
			return;
		}
		Cookie cookie = getCookie(request, name);
		if (null != cookie) {
			cookie.setPath("/");
			cookie.setValue("");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}

	/**
	 * 添加一条新的Cookie，可以指定过期时间(单位：秒)
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param maxValue
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxValue) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		String par="";
		if (StringUtils.isNotEmpty(value)) {
			par=value;
		}
		Cookie cookie = new Cookie(name, par);
		cookie.setPath("/");
		if (maxValue != 0) {
			cookie.setMaxAge(maxValue);
		} else {
			cookie.setMaxAge(COOKIE_HALF_HOUR);
		}
		response.addCookie(cookie);
		/*try {
			response.flushBuffer();
		} catch (IOException e) {
			log.error("CookieUtils setCookie error", e);
		}*/
	}

	/**
	 * 添加一条新的Cookie，默认30分钟过期时间
	 * 
	 * @param response
	 * @param name
	 * @param value
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, COOKIE_HALF_HOUR);
	}

	/**
	 * 将cookie封装到Map里面
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<>();
		Cookie[] cookies = request.getCookies();
		if (!ArrayUtils.isEmpty(cookies)) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

}