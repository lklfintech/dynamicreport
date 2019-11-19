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

/**
 * <p>
 * 异常工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ExceptionUtils {
	private ExceptionUtils(){}

	/**
	 * 获取错误消息
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionMsg(Exception e) {
		String msg = e.getMessage();
		if (e.getCause() != null) {
			msg += ",Cased by:" + e.getCause().getClass() + "," + e.getCause().getMessage();
		}
		return msg;
	}
}
