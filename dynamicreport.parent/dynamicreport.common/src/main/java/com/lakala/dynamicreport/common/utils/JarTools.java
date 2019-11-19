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

import java.io.File;

/**
 * <p>
 * 获取打包后jar的路径信息工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class JarTools {

	/**
	 * 获取当前路径
	 * 
	 * @return
	 */
	public static String getCurrentPath() {
		String filePath = getClassPath();
		File file = new File(filePath);
		if (!file.exists() && !file.isDirectory()) {
			filePath = getJarDir() + "/";
		}
		return filePath;
	}

	/**
	 * 获取class路径
	 * 
	 * @return
	 */
	public static String getClassPath() {
		String path = "";
		if (JarTools.class.getClassLoader().getResource("") != null) {
			path = JarTools.class.getClassLoader().getResource("").getPath();
		}
		return path;
	}

	/**
	 * 获取jar绝对路径
	 *
	 * @return
	 */
	public static String getJarPath() {
		File file = getFile();
		if (file == null)
			return null;
		return file.getAbsolutePath();
	}

	/**
	 * 获取jar目录
	 *
	 * @return
	 */
	public static String getJarDir() {
		File file = getFile();
		if (file == null)
			return null;
		return getFile().getParent();
	}

	/**
	 * 获取jar包名
	 *
	 * @return
	 */
	public static String getJarName() {
		File file = getFile();
		if (file == null)
			return null;
		return getFile().getName();
	}

	private static File getFile() {
		// 关键是这行...
		String path = JarTools.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");// 转换处理中文及空格
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return new File(path);
	}

}
