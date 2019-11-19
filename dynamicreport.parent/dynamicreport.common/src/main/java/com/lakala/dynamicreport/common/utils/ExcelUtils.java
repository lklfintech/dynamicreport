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

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * <p>
 * Excel工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ExcelUtils {

	private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0");// 格式化
																				// number为整

	private static final DecimalFormat DECIMAL_FORMAT_PERCENT = new DecimalFormat("##.00%");// 格式化分比格式，后面不足2位的用0补齐

	private static final FastDateFormat FAST_DATE_FORMAT = FastDateFormat.getInstance("yyyy/MM/dd");

	private static final DecimalFormat DECIMAL_FORMAT_NUMBER = new DecimalFormat("0.00E000"); // 格式化科学计数器

	private static final Pattern POINTS_PATTERN = Pattern.compile("0.0+_*[^/s]+"); // 小数匹配

	// hide public constructors
	private ExcelUtils() {
		throw new IllegalStateException("ExcelUtils class");
	}
	/**
	 * 对外提供读取excel 的方法
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static List<List<Object>> readExcel(MultipartFile file) throws IOException {
		String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase();
		if (Objects.equals("xls", extension) || Objects.equals("xlsx", extension)) {
			return readExcel(file.getInputStream());
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取 office excel
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static List<List<Object>> readExcel(InputStream inputStream) throws IOException {
		List<List<Object>> list = new LinkedList<>();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(inputStream);
			int sheetsNumber = workbook.getNumberOfSheets();
			for (int n = 0; n < sheetsNumber; n++) {
				Sheet sheet = workbook.getSheetAt(n);
				Object value = null;
				Row row = null;
				Cell cell = null;
				for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) { // 从第二行开始读取
					row = sheet.getRow(i);
					if (StringUtils.isEmpty(row)) {
						continue;
					}
					List<Object> linked = new LinkedList<>();
					for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
						cell = row.getCell(j);
						if (StringUtils.isEmpty(cell)) {
							continue;
						}
						value = getCellValue(cell);
						linked.add(value);
					}
					list.add(linked);
				}
			}
		} catch (Exception e) {
			log.error("[ExcelUtils : readExcel]", e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(inputStream);
		}
		return list;
	}

	/**
	 * 获取excel 单元格数据
	 *
	 * @param cell
	 * @return
	 */
	private static Object getCellValue(Cell cell) {
		Object value = null;
		switch (cell.getCellType()) {
		case _NONE:
			break;
		case STRING:
			value = cell.getStringCellValue();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) { // 日期
				value = FAST_DATE_FORMAT.format(DateUtil.getJavaDate(cell.getNumericCellValue()));// 统一转成
																									// yyyy/MM/dd
			} else if ("@".equals(cell.getCellStyle().getDataFormatString()) || "General".equals(cell.getCellStyle().getDataFormatString()) || "0_ ".equals(cell.getCellStyle().getDataFormatString())) {
				// 文本 or 常规 or 整型数值
				value = DECIMAL_FORMAT.format(cell.getNumericCellValue());
			} else if (POINTS_PATTERN.matcher(cell.getCellStyle().getDataFormatString()).matches()) { // 正则匹配小数类型
				value = cell.getNumericCellValue(); // 直接显示
			} else if ("0.00E+00".equals(cell.getCellStyle().getDataFormatString())) {// 科学计数
				value = cell.getNumericCellValue(); // 待完善
				value = DECIMAL_FORMAT_NUMBER.format(value);
			} else if ("0.00%".equals(cell.getCellStyle().getDataFormatString())) {// 百分比
				value = cell.getNumericCellValue(); // 待完善
				value = DECIMAL_FORMAT_PERCENT.format(value);
			} else if ("# ?/?".equals(cell.getCellStyle().getDataFormatString())) {// 分数
				value = cell.getNumericCellValue(); // //待完善
			} else { // 货币
				value = cell.getNumericCellValue();
				value = DecimalFormat.getCurrencyInstance().format(value);
			}
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case BLANK:
			break;
		default:
			value = cell.toString();
		}
		return value;
	}

}