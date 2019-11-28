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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * 文件工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class CreateFileUtils {
	private static final Logger log = LoggerFactory.getLogger(CreateFileUtils.class);

	private CreateFileUtils(){}
	

	/**
	 * 生成.TXT格式文件,行数几乎无上限
	 *
	 * @param rows
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean createTxtFile(List<Object[]> rows, String filePath, String fileName) {
		// 标记文件生成是否成功
		boolean flag = true;
		PrintWriter pfp = null;	
		try {
			// 含文件名的全路径
			String fullPath = filePath + File.separator + fileName + ".txt";

			File file = new File(fullPath);
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file = new File(fullPath);
			file.createNewFile();

			// 格式化浮点数据
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMaximumFractionDigits(10); // 设置最大小数位为10

			// 格式化日期数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			// 遍历输出每行
			pfp = new PrintWriter(file, StandardCharsets.UTF_8.name()); // 设置输出文件的编码为utf-8
			for (Object[] rowData : rows) {
				StringBuilder thisLine = new StringBuilder();
				for (int i = 0; i < rowData.length; i++) {
					Object obj = rowData[i]; // 当前字段

					// 格式化数据
					String field = "";
					if (null != obj) {
						if (obj.getClass() == String.class) { // 如果是字符串
							field = (String) obj;
						} else if (obj.getClass() == Double.class || obj.getClass() == Float.class) { // 如果是浮点型
							field = formatter.format(obj); // 格式化浮点数,使浮点数不以科学计数法输出
						} else if (obj.getClass() == Integer.class || obj.getClass() == Long.class || obj.getClass() == Short.class || obj.getClass() == Byte.class) { // 如果是整形
							field += obj;
						} else if (obj.getClass() == Date.class) { // 如果是日期类型
							field = sdf.format(obj);
						}
					} else {
						field = " "; // null时给一个空格占位
					}

					// 拼接所有字段为一行数据，用tab键分隔
					if (i < rowData.length - 1) { // 不是最后一个元素
						thisLine.append(field).append("\t");
					} else { // 是最后一个元素
						thisLine.append(field);
					}
				}
				pfp.print(thisLine.toString() + "\n");
			}

		} catch (Exception e) {
			flag = false;
			log.error("生成txt文件失败：" + e.getMessage(), e);
		} finally {
			if (pfp != null) {
				pfp.close();
			}
		}
		return flag;
	}

	/**
	 * 生成.csv格式文件,行数几乎无上限
	 *
	 * @param rows
	 * @param filePath
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static boolean createCsvFile(List<List<Object>> rows, String filePath, String fileName) throws IOException {
		// 标记文件生成是否成功
		boolean flag = true;

		// 文件输出流
		BufferedWriter fileOutputStream = null;

		try {
			// 含文件名的全路径
			String fullPath = filePath + File.separator + fileName + ".csv";

			File file = new File(fullPath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file = new File(fullPath);
			file.createNewFile();

			// 格式化浮点数据
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMaximumFractionDigits(10); // 设置最大小数位为10

			// 格式化日期数据
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Pattern pattern = Pattern.compile("[0-9]{13,}");
			// 实例化文件输出流
			fileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GB2312"), 1024);
			for (List<Object> rowData : rows) {
				int i = 0;
				for (Object obj : rowData) {
					// 格式化数据
					String field = "";
					if (null != obj) {
						if (obj.getClass() == String.class) { // 如果是字符串
							field = (String) obj;
							field = trimChars(field);
						} else if (obj.getClass() == Double.class || obj.getClass() == Float.class) { // 如果是浮点型
							field = formatter.format(obj); // 格式化浮点数,使浮点数不以科学计数法输出
						} else if (obj.getClass() == Integer.class || obj.getClass() == Long.class || obj.getClass() == Short.class || obj.getClass() == Byte.class) { // 如果是整形
							field += obj;
						} else if (obj.getClass() == Date.class) { // 如果是日期类型
							field = sdf.format(obj);
						} else {
							field = String.valueOf(obj);
						}
					} else {
						field = ""; // null时给一个空格占位
					}
					if(pattern.matcher(field).matches()){
						field="'"+field;
					}
					
					// 拼接所有字段为一行数据
					if (i < rowData.size() - 1) { // 不是最后一个元素
						fileOutputStream.write("\"" + field + "\"" + ",");
					} else { // 是最后一个元素
						fileOutputStream.write("\"" + field + "\"");
					}
					i++;
				}
				// 创建一个新行
				fileOutputStream.newLine();
			}
			fileOutputStream.flush();
		} catch (Exception e) {
			flag = false;
			log.error("生成csv文件失败：" + e.getMessage(), e);
		} finally {
			 
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
			 
		}
		return flag;
	}

	/**
	 * 生成.xls格式文件,单页上限： 03版是65536行 ,07版的是1048576行, 10版不知
	 *
	 * @param rows
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static boolean createXlsFile(List<Object[]> rows, String filePath, String fileName) {
		// 标记文件生成是否成功
		boolean flag = true;

		try {
			// 创建一个webbook，对应一个Excel文件
			XSSFWorkbook wb = new XSSFWorkbook();

			// 在webbook中添加一个sheet,对应Excel文件中的sheet
			XSSFSheet sheet = wb.createSheet(fileName);

			// 遍历输出每行
			for (int i = 0; i < rows.size(); i++) {
				Object[] rowData = rows.get(i); // 每一行的数据
				XSSFRow row = sheet.createRow(i);
				for (int j = 0; j < rowData.length; j++) {
					XSSFCell cell = row.createCell(j);
					// 假设只有三种类型的数据
					if (rowData[j].getClass() == String.class) { // String类型数值
						cell.setCellValue((String) rowData[j]);
					} else if (rowData[j].getClass() == double.class) { // double类型数值
						cell.setCellValue((Double) rowData[j]);
					} else if (rowData[j].getClass() == int.class) { // int类型数值
						cell.setCellValue((Integer) rowData[j]);
					}else { // int类型数值
						cell.setCellValue(String.valueOf(rowData[j]));
					}
				}
			}

			String fullPath = filePath + File.separator + fileName + ".xls";// 含文件名的全路径

			File file = new File(fullPath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file = new File(fullPath);
			file.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(file); // 写出数据到文件
			wb.write(fileOut);
			fileOut.close();
			wb.close();
		} catch (Exception e) {
			flag = false;
			log.error("生成xls文件失败：" + e.getMessage(), e);
		}

		return flag;
	}

	/**
	 * 导出的时候需要对一格的内容进行检查,看是否有非法字符,以免串行
	 *
	 * @param content
	 * @return
	 */
	public static String trimChars(String content) {
		String newContent=content;
		if (newContent == null) {
			return "";
		}
		// @====> 1.包含,同事包含"，那么久先替换双引号"为两个""+然后在包裹在双引号里。
		if (newContent.contains(",") && newContent.contains("\"")) {
			newContent = newContent.replaceAll(",", "，"); // 逗号替换为空格
			newContent = newContent.replaceAll("\"", "\"\"");
			newContent = "\"" + newContent + "\"";
		} else if (newContent.contains(",")) {
			newContent = newContent.replaceAll(",", "，");
		} else {
			// @====> 2.如果仅仅包含逗号,则用引号包裹即可。
			if (newContent.contains(",") && !newContent.contains("\"")) {
				newContent = newContent.replaceAll(",", "，"); // 逗号替换为空格
				newContent = "\"" + newContent + "\"";
			}
			// @====> 3.如果仅仅包含引号,则双引号代替一个引号，再最外层在包裹即可。
			if (newContent.contains("\"") && !newContent.contains(",")) {
				newContent = "\"" + newContent.replaceAll("\"", "\"\"") + "\"";
			}
		}

		return newContent;
	}

	/**
	 * 文件读取
	 *
	 * @param fileInputStream
	 * @return
	 */
	public static  String readFile(InputStream fileInputStream) {
		BufferedReader reader = null;
		String laststr = "";
		try {
 			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			log.error("读取文件流失败",e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
                    log.error("关闭文件流失败",e);
				}
			}
		}
		return laststr;
	}
	
	public static  void saveFile(String sourcePath,String fileName,MultipartFile file) throws IOException {
 		//目标路径
		String filePath = sourcePath;
		File file3 = new File(filePath);
		//如果文件目录不存在，就执行创建		
		if(!file3.isDirectory()){
			file3.mkdirs();
		}
 		//目标文件名称
		String targetName =fileName;
 		//创建目标文件
		File targetFile = new File(filePath + targetName);
		FileOutputStream fos = new FileOutputStream(targetFile);
			//读取本地文件
		//File localFile = new File("E:"+File.separator+"1.jpg");
		//获取本地文件输入流
		InputStream stream = file.getInputStream();
		
		//写入目标文件
		byte[] buffer = new byte[1024*1024];
		int byteRead = 0;
		while((byteRead=stream.read(buffer))!=-1){
			fos.write(buffer, 0, byteRead);
			fos.flush();
		}
		fos.close();
		stream.close();
	}
	 
    
}