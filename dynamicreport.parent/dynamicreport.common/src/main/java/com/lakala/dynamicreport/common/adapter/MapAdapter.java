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
package com.lakala.dynamicreport.common.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * XML / JAVA 数据转换适配
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class MapAdapter extends
		XmlAdapter<MapConvertor, HashMap<String, Object>> {

	/**
	 * XML to JAVA
	 *
	 * @param map
	 * @return HashMap<String, Object>
	 * @throws Exception
	 */

	@Override
	public HashMap<String, Object> unmarshal(MapConvertor map) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, Object> result = new HashMap<String, Object>();

		// 遍历MapConvertor，将XML节点内容写入JavaBean Map对象
		for (MapConvertor.MapEntry e : map.getEntry()) {
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	/**
	 * JAVA to XML
	 * 
	 * @param map
	 * @return MapConvertor
	 * @throws Exception
	 */
	@Override
	public MapConvertor marshal(HashMap<String, Object> map) throws Exception {

		// 创建MapConvertor对象，盛放XML节点内容
		MapConvertor convertor = new MapConvertor();

		// 遍历map，将JavaBean中数据写入XML节点
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			// 创建空的MapEntry对象(该mapEntry应该放在循环内，防止重复使用同一个java对象引用)
			MapConvertor.MapEntry mapEntry = new MapConvertor.MapEntry();

			mapEntry.setKey(entry.getKey());
			mapEntry.setValue(entry.getValue());

			convertor.addEntry(mapEntry);
		}
		return convertor;
	}
}