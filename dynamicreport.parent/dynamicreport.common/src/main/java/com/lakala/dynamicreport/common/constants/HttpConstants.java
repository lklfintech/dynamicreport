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
package com.lakala.dynamicreport.common.constants;

/**
 * <p>
 * http请求常量类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class HttpConstants {

	/**
	 * get请求
	 */
	public static final String GET = "GET";

	/**
	 * post请求
	 */
	public static final String POST = "POST";
	
	/**
	 * 表单提交
	 */
	public static final String SERVICE_SUBMIT_FORM = "form";

	/**
	 * 主体提交
	 */
	public static final String SERVICE_SUBMIT_BODY = "body";
	
	/**
	 * DUBBO服务
	 */
	public static final String DUBBO = "Dubbo";
	
	/**
	 * WS
	 */
	public static final String WEB_SERVICE = "WebService";
	
	/**
	 * HTTP
	 */
	public static final String HTTP = "Http";
	
	public static final String SIGN_ONE = "//";
	
	public static final String SIGN_TWO = "/";
	
	public static final String SIGN_THREE = ":";
	
	/**
	 * 数据模型
	 */
	public static final String DATA_MODEL = "DataModel";

	// hide public constructors
	private HttpConstants() {
		throw new IllegalStateException("Http Constants class");
	}
}
