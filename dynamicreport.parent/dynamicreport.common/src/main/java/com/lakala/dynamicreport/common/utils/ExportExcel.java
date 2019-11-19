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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Excel导入/导出工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ExportExcel {
	private final Logger log = LoggerFactory.getLogger(ExportExcel.class);

	private static final String REMARK="Created By Phil";
	
	private static final String DATE_FORMATE="yyyy/MM/dd";
	
	private static final String REG="^//d+(//.//d+)?$";
	
	private static final String ERR_MSG="[ExportExcel : exportHSExcelByColumn]";
	/**
	 * 导出 xls格式Excel HSSF
	 * 
	 * @param title
	 * @param headers
	 * @param columns
	 * @param dataset
	 * @param out
	 * @param pattern2
	 */
	public void exportHSExcelByColumn(String title, String[] headers, String[] columns, Collection<T> dataset, OutputStream out, String pattern2) {
		String pattern=pattern2;
		Workbook workbook = new SXSSFWorkbook(100);
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(24);
		// 生成一个 表格标题行样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 生成并设置另一个样式 内容的背景
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font.setBold(true);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		Drawing<?> patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置
		Comment comment = patriarch.createCellComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString(REMARK));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("phil");

		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			RichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_FORMATE;
		}
		FastDateFormat instance = FastDateFormat.getInstance(pattern);
		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		int count = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			count = headers.length < columns.length ? headers.length : columns.length;
			for (int i = 0; i < count; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(style2);
				String fieldName = columns[i];
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				try {
					Class<? extends Object> tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if (value instanceof Date) {
						Date date = (Date) value;
						textValue = instance.format(date);
					} else if (value instanceof byte[]) {
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						byte[] bsValue = (byte[]) value;
						ClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, SXSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理

					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile(REG);
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							RichTextString richString = new HSSFRichTextString(textValue);
							Font font3 = workbook.createFont();
							font3.setColor(IndexedColors.BLACK.index); // 内容
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException|NoSuchMethodException|IllegalArgumentException|IllegalAccessException|InvocationTargetException e) {
 					log.error(ERR_MSG, e);
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error(ERR_MSG, e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 导出 xlsx格式Excel XSSF
	 * 
	 * @param title
	 * @param headers
	 * @param columns
	 * @param dataset
	 * @param out
	 * @param pattern2
	 */
	public void exportXSExcelByColumn(String title, String[] headers, String[] columns, Collection<Map<String, Object>> dataset, OutputStream out, String pattern2) {
		String pattern=pattern2;
		Workbook workbook = new SXSSFWorkbook(100);
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(24);
		// 生成一个 表格标题行样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 生成并设置另一个样式 内容的背景
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font.setBold(true);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		Drawing<?> patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置
		Comment comment = patriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new XSSFRichTextString(REMARK));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("phil");

		// 产生表格标题行
		Row row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(style);
			RichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_FORMATE;
		}
		FastDateFormat instance = FastDateFormat.getInstance(pattern);
		// 遍历集合数据，产生数据行
		Iterator<Map<String, Object>> it = dataset.iterator(); // 多个Map集合
		int index = 0;
		int count = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Map<String, Object> map = it.next();
			count = headers.length < columns.length ? headers.length : columns.length;
			for (int i = 0; i < count; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(style2);
				try {
					Object value = map.get(columns[i]);
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					commonPro(workbook, sheet, patriarch, row, instance, index, i, cell, value, textValue);
				} catch (SecurityException e) {
					log.error("[ExportExcel : exportXSExcelByColumn]", e);
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("[ExportExcel : exportXSExcelByColumn]", e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * 导出 xlsx格式Excel XSSF
	 * 
	 * @param title
	 * @param dataset
	 * @param out
	 * @param pattern2
	 */
	public void exportXSExcel(String title, List<List<Object>> dataset, OutputStream out, String pattern2) {
		String pattern=pattern2;
		Workbook workbook = new SXSSFWorkbook(100);
		// 生成一个表格
		Sheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeightInPoints(24);
		// 生成一个 表格标题行样式
		CellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		Font font = workbook.createFont();
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);

		// 生成并设置另一个样式 内容的背景
		CellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		style2.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成另一个字体
		Font font2 = workbook.createFont();
		font.setBold(true);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		Drawing<?> patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置
		Comment comment = patriarch.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new XSSFRichTextString(REMARK));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("phil");

		// 产生表格标题行
		Row row = sheet.createRow(0);
		if (StringUtils.isEmpty(pattern)) {
			pattern = DATE_FORMATE;
		}
		FastDateFormat instance = FastDateFormat.getInstance(pattern);
		int index = 0;
		// 遍历集合数据，产生数据行
		if (CollectionUtils.isNotEmpty(dataset)) {
			for (List<Object> list : dataset) {
				row = sheet.createRow(index);
				index++;
				if (CollectionUtils.isNotEmpty(list)) {
					int i = 0;
					for (Object obj : list) {
						Cell cell = row.createCell(i);
						cell.setCellStyle(style2);
						try {
							Object value = obj;
							// 判断值的类型后进行强制类型转换
							String textValue = null;
							commonPro(workbook, sheet, patriarch, row, instance, index, i, cell, value, textValue);
						} catch (SecurityException e) {
							log.error("[ExportExcel : exportXSExcel]", e);
						}
						i++;
					}
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			log.error("[ExportExcel : exportXSExcel]", e);
		} finally {
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(out);
		}
	}

	private void commonPro(Workbook workbook, Sheet sheet, Drawing<?> patriarch, Row row, FastDateFormat instance, int index, int i, Cell cell, Object value, String textValue) {
		if (value instanceof Date) {
            Date date = (Date) value;
            textValue = instance.format(date);
        } else if (value instanceof byte[]) {
            row.setHeightInPoints(60);
            // 设置图片所在列宽度为80px,注意这里单位的一个换算
            sheet.setColumnWidth(i, (short) (35.7 * 80));
            byte[] bsValue = (byte[]) value;
            ClientAnchor anchor = new XSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
            anchor.setAnchorType(AnchorType.MOVE_DONT_RESIZE);
            patriarch.createPicture(anchor, workbook.addPicture(bsValue, Workbook.PICTURE_TYPE_JPEG));
        } else {
            // 其它数据类型都当作字符串简单处理
            if (value != null) {
                textValue = value.toString();
            } else {
                textValue = "";
            }
        }
		// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
		if (textValue != null) {
            Pattern p = Pattern.compile(REG);
            Matcher matcher = p.matcher(textValue);
            if (matcher.matches()) {
                // 是数字当作double处理
                cell.setCellValue(Double.parseDouble(textValue));
            } else {
                RichTextString richString = new XSSFRichTextString(textValue);
                Font font3 = workbook.createFont();
                font3.setColor(IndexedColors.BLACK.index); // 内容
                richString.applyFont(font3);
                cell.setCellValue(richString);
            }
        }
	}
}