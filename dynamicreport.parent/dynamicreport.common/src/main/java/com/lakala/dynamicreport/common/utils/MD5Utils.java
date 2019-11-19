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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * <p>
 * MD5加密工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class MD5Utils {
	
	private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);
	
	private MD5Utils(){}

	/**
	 * MD5加密
	 *
	 * @param inputStr
	 * @return
	 */
	public static String getMD5Str(String inputStr) {
		StringBuilder md5StrBuilder = new StringBuilder();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(inputStr.getBytes(StandardCharsets.UTF_8));
			byte[] byteArray = messageDigest.digest();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					md5StrBuilder.append("0");
					md5StrBuilder.append(Integer.toHexString(0xFF & byteArray[i]));
				} else {
					md5StrBuilder.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
		} catch (Exception e) {
			log.info("error when transferring md5, error message is {}", e.getMessage());
		}

		return md5StrBuilder.toString();
	}
}
