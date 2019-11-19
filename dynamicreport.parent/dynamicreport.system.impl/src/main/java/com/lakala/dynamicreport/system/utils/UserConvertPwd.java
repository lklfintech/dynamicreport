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
package com.lakala.dynamicreport.system.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * <p>
 * 用户密码加密工具类（MD5）
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class UserConvertPwd {
	// hide public constructors
	private UserConvertPwd() {
		throw new IllegalStateException("UserConvertPwd class");
	}
	/**
	 * 加密
	 * 
	 * @param password
	 * @return
	 */
	public static String encrypt(String password) {
		String hashAlgorithmName = "md5";
		String credentials = password;
		int hashIterations = 2;
		Object obj = new SimpleHash(hashAlgorithmName, credentials, null, hashIterations);
		return obj.toString();
	}


}
