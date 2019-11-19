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

import java.io.File;

/**
 * <p>
 * 文件删除工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class FileDelUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileDelUtils.class);

	// hide public constructors
	private FileDelUtils() {
		throw new IllegalStateException("FileDelUtils class");
	}

	/**
	 * 删除文件
	 *
	 * @param fileName
	 *            要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			logger.info("删除文件失败:{}不存在！", fileName);
			return false;
		} else {
			return deleteFile(fileName);
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @param fileName
	 *            要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			file.delete();
			// logger.info("删除单个文件{}成功！", fileName);
			return true;
		} else {
			logger.info("删除单个文件失败：{}不存在！", fileName);
			return false;
		}
	}

}
