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

import org.apache.commons.lang3.time.DateUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 日期格式转换（Date）
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class DateEditor extends PropertyEditorSupport {

	private boolean emptyAsNull;
	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private static final String[] DATE_PATTERNS = { "yyyy", "yyyy-MM", "yyyyMM",
			"yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd",
			"yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	public DateEditor(boolean emptyAsNull) {
		this.emptyAsNull = emptyAsNull;
	}

	public DateEditor(boolean emptyAsNull, String dateFormat) {
		this.emptyAsNull = emptyAsNull;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getAsText() {
		Date date = (Date) getValue();
		return date != null ? new SimpleDateFormat(this.dateFormat)
				.format(date) : "";
	}

	@Override
	public void setAsText(String text) {
		if (text == null) {
			setValue(null);
		} else {
			String str = text.trim();
			if ((this.emptyAsNull) && ("".equals(str)))
				setValue(null);
			else
				try {
					setValue(DateUtils.parseDate(str, DATE_PATTERNS));
				} catch (ParseException e) {
					setValue(null);
				}
		}
	}
}
