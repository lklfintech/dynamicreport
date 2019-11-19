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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * JSON工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class JsonUtils {

	static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	// hide public constructors
	private JsonUtils() {
		throw new IllegalStateException("JsonUtils class");
	}
	/**
	 * json转化为对象
	 * 
	 * @param json
	 * @param className
	 * @return
	 * @throws Exception
	 */
	public static Object parseJson(String json, String className) throws ClassNotFoundException{
		return JSON.parseObject(json, Class.forName(className));
	}

	/**
	 * json转化为map
	 * 
	 * @param json
	 * @param keyClzMap
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws Exception
	 */
	public static Map<String, Object> parseMap(String json, Map<String, String> keyClzMap) throws ClassNotFoundException{
		Map<String, Object> resultMap = new HashMap<>();
		if (json == null) {
			return resultMap;
		}
		Map<String, Object> map = (Map<String, Object>) JsonUtils.parseJson(json, "java.util.Map");
		for (Map.Entry<String,Object> entry : map.entrySet()) {
			String key = entry.getKey();
			if (keyClzMap != null && keyClzMap.get(key) != null) {
				Object obj = JsonUtils.parseJson(map.get(key).toString(), keyClzMap.get(key));
				resultMap.put(key, obj);
			} else {
				Object obj = map.get(key);
				if (obj instanceof JSONObject) {
					Map<String, Object> subMap = parseMap(((JSONObject) obj).toJSONString(), null);
					resultMap.putAll(subMap);
				} else if (obj instanceof String) {
					resultMap.put(key, String.valueOf(obj));
				} else if (obj instanceof Integer) {
					resultMap.put(key, Integer.valueOf(obj.toString()));
				} else {
					resultMap.put(key, obj);
				}
			}
		}
		return resultMap;
	}

	/**
	 * listToTree
	 * <p>
	 * 方法说明
	 * <p>
	 * 将JSONArray数组转为树状结构
	 * 
	 * @param arr
	 *            需要转化的数据
	 * @param id
	 *            数据唯一的标识键值
	 * @param pid
	 *            父id唯一标识键值
	 * @param child
	 *            子节点键值
	 * @return JSONArray
	 */
	public static JSONArray listToTree(JSONArray arr, String id, String pid, String child) {
		JSONArray r = new JSONArray();
		JSONObject hash = new JSONObject();
		// 将数组转为Object的形式，key为数组中的id
		for (int i = 0; i < arr.size(); i++) {
			JSONObject json = (JSONObject) arr.get(i);
			hash.put(json.getString(id), json);
		}
		// 遍历结果集
		for (int j = 0; j < arr.size(); j++) {
			// 单条记录
			JSONObject aVal = (JSONObject) arr.get(j);
			// 在hash中取出key为单条记录中pid的值
			Object object=aVal.get(pid);
			JSONObject hashVP =null;
			if(object!=null){
				hashVP = (JSONObject) hash.get(aVal.get(pid).toString());
			}
 			// 如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
			if (hashVP != null) {
				// 检查是否有child属性
				if (hashVP.get(child) != null) {
					JSONArray ch = (JSONArray) hashVP.get(child);
					ch.add(aVal);
					hashVP.put(child, ch);
				} else {
					JSONArray ch = new JSONArray();
					ch.add(aVal);
					hashVP.put(child, ch);
				}
			} else {
				r.add(aVal);
			}
		}
		return r;
	}

	/**
	 * 格式化json格式文件
	 *
	 * @param jsonString
	 * @return
	 */
	public static String formatJsonFile(String jsonString) {
		// 标记文件生成是否成功
  		// 拼接文件完整路径
		//String fullPath = filePath + File.separator + fileName + ".json";
 		// 生成json格式文件
		try {
		/*	// 保证创建一个新文件
			File file = new File(fullPath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();*/
 			if (jsonString.indexOf("'") != -1) {
				// 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
				jsonString = jsonString.replaceAll("'", "\\'");
			}
			if (jsonString.indexOf("\"") != -1) {
				// 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
				jsonString = jsonString.replaceAll("\"", "\\\"");
			}

			if (jsonString.indexOf("\r\n") != -1) {
				// 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
				jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
			}
			if (jsonString.indexOf("\n") != -1) {
				// 将换行转换一下，因为JSON串中字符串不能出现显式的换行
				jsonString = jsonString.replaceAll("\n", "\\u000a");
			}
 		/*	// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			write.write(jsonString );
			write.flush();
			write.close();*/
		} catch (Exception e) {
 			e.printStackTrace();
 			logger.error("生成json格式文件失败" + e.getMessage(), e);
		}
 		// 返回是否成功的标记
		return jsonString;
	}

}
