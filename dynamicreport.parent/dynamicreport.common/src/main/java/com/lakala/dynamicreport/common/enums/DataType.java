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
package com.lakala.dynamicreport.common.enums;

import com.lakala.dynamicreport.common.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 数据类型
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public enum DataType {
	/**
	 * 字符串
	 */
	string,
	/**
	 * 数字
	 */
	decimal,
	/**
	 * 枚举 enum
	 */
	e,
	/**
	 * boolean布尔类型
	 */
	bool,
	/**
	 * 日期
	 */
	date;
	
	public static String geteType(Object value){
		if (value instanceof Integer) {
			return DataType.decimal.name();
		}else if (value instanceof Double) {
			return DataType.decimal.name();
		} else if (value instanceof Float) {
			return DataType.decimal.name();
		} else if (value instanceof Long) {
			return DataType.decimal.name();
		}else if (value instanceof Boolean) {
			return DataType.bool.name();
		} else if (value instanceof Date) {
			return DataType.date.name();
		}else{
			return DataType.string.name();
		}
	}
	
	public static Object transferParam(String name,String type,Object obj){
		String value=String.valueOf(obj);
		try {
			switch (type) {
			case "string":
				return String.valueOf(value);
			case "int":
				return Integer.valueOf(value);
			case "integer":
				return Integer.valueOf(value);
			case "decimal":
				return BigDecimal.valueOf(Double.valueOf(value));
			case "float":
				return Float.valueOf(value);
			case "long":
				return Long.valueOf(value);
			case "double":
				return Double.valueOf(value);
			case "boolean":
				return Boolean.valueOf(value);
			case "date":
				return DateUtils.stringToDate(value);
			case "array":
				return value.split(",");
			default:
				return "";
			} 
		} catch (Exception e) {
			throw new RuntimeException(String.format("【%s】转型失败【%s】==>【%s】,%s",name,value,type,e.getMessage()));
		}
	}
}
