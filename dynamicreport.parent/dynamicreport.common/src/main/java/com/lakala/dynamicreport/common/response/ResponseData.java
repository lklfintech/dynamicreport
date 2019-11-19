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
package com.lakala.dynamicreport.common.response;

import java.io.Serializable;

/**
 * <p>
 * 响应对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ResponseData implements Serializable {

	private static final long serialVersionUID = 6575959539490110308L;

	/**
	 * 返回编码
	 */
	private String return_code;

	/**
	 * 返回数据
	 */
	private String return_data;

	/**
	 * 描述
	 */
	private String return_desc;

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_data() {
		return return_data;
	}

	public void setReturn_data(String return_data) {
		this.return_data = return_data;
	}

	public String getReturn_desc() {
		return return_desc;
	}

	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
	}

}
