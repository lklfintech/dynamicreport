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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * <p>
 * Map数据转换
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@XmlType(name = "MapConvertor")
@XmlAccessorType(XmlAccessType.FIELD)
public class MapConvertor {

	/**
	 * SOAP报文结构是一个Map的List
	 */
	private List<MapEntry> entry = new ArrayList<MapEntry>();

	public void addEntry(MapEntry entry) {
		this.entry.add(entry);
	}

	public List<MapEntry> getEntry() {
		return entry;
	}

	public void setEntry(List<MapEntry> entry) {
		this.entry = entry;
	}

	public static class MapEntry {
		private String key;

		private Object value;

		public MapEntry() {
			super();
		}

		public MapEntry(String key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}

		public MapEntry(Entry<String, Object> entry) {
			super();
			this.key = entry.getKey();
			this.value = entry.getValue();
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}
}