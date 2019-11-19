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
 * 响应码枚举类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public enum ResponseCode {

	S000A000("执行成功"),S000F000("执行失败"),S000F001("加密失败");

	private String name;

	ResponseCode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
