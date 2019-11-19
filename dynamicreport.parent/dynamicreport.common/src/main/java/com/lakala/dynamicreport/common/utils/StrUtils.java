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

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 字符串工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class StrUtils {

	// hide public constructors
	private StrUtils() {
		throw new IllegalStateException("StrUtils class");
	}
	/**
	 * 去前后空格回车
	 * 
	 * @param str
	 * @return
	 */
	public static String getStringNoBlank(String str) {
		if (str != null && !"".equals(str)) {
			Pattern p = Pattern.compile("(^\\s*)|(\\s*$)");
			Matcher m = p.matcher(str.replaceAll("\n", ""));
			return m.replaceAll("");
		} else {
			return "";
		}
	}

	public static boolean isEmpty(String str) {
		return StringUtils.isBlank(str);
	}

	/**
	 * 判断value是否包含source，例如value的值为1，2，3，source值为1
	 *
	 * @param source
	 * @param value
	 * @return
	 */
	public static boolean contain(String source, String value) {
		if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(value)) {
			String[] splitStr = value.split(",");
			List<String> values = Arrays.asList(splitStr);
			if (values.contains(source)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 正则表达式匹配字符
	 *
	 * @param str 
	 * @param regEx
	 * @return
	 */
	public static boolean matches(String str,String regEx){
		if(StringUtils.isBlank(str)){
			return false;
		}
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    Matcher matcher = pattern.matcher(str);
	    // 字符串是否与正则表达式相匹配
	    return matcher.matches();
	}

}
