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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 响应JSON格式对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "返回结果对象",description = "返回结果对象(ResultJson)")
public class ResultJson {

	@ApiModelProperty(value = "是否成功",name="success")
	private boolean success = true;

	@ApiModelProperty(value = "提示信息",name="msg")
	private String msg = "操作成功";

	@ApiModelProperty(value = "url地址",name="url")
	private String url;

	@ApiModelProperty(value = "其他信息",name="obj")
	private Object obj = null;

	@ApiModelProperty(value = "其他参数",name="attributes")
	private Map<String, Object> attributes = new HashMap<>();

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * public String getJsonStr() { JSONObject obj = new JSONObject();
	 * obj.put("success", this.isSuccess()); obj.put("msg", this.getMsg());
	 * obj.put("obj", this.obj); obj.put("attributes", this.attributes); return
	 * obj.toJSONString(); }
	 **/
}
