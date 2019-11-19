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
package com.lakala.dynamicreport.common.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 基础JSON格式响应类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class JsonResponse<T> implements Serializable {

	/**
	 * 响应码
	 */
	public static final String CODE_000001="000001";
	
	public static final String CODE_999999="999999";
	
	public static final String CODE_000000="000000";
	
	private static final long serialVersionUID = 8164540803665250293L;

	private String code = "000000";

	/**
	 * 提示信息
	 */
	private String msg = "操作成功";

	/**
	 * 响应数据
	 */
	private T data;

	/**
	 * 验签
	 */
	private String sign;
	
	public JsonResponse(String code) {
		this.code = code;
	}
	public JsonResponse() {

	}
	public JsonResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public JsonResponse(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		Map<String, Object> result = new HashMap<>();
		result.put("data", this.data);
		result.put("code", this.code);
		result.put("msg", this.msg);
		return JSON.toJSONString(result);
	}

}
