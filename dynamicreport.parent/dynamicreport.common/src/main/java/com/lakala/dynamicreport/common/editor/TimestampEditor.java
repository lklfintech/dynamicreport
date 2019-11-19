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
package com.lakala.dynamicreport.common.editor;

import com.lakala.dynamicreport.common.utils.DateUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * <p>
 * 日期格式转换（Timestamp）
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class TimestampEditor extends PropertyEditorSupport {

	private boolean emptyAsNull;
	
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public TimestampEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public TimestampEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getAsText() {
		Timestamp date = (Timestamp) getValue();
		return date != null ? new SimpleDateFormat(this.dateFormat)
				.format(date) : "";
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String str = text.trim();
			if ((this.emptyAsNull) && ("".equals(str))) {
				setValue(null);
			} else {
				setValue(DateUtils.formatFromYYYYMMDDhhmmss(str));
			}
		}
	}
}
