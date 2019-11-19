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
package com.lakala.dynamicreport.datamodel.util;

import java.io.Serializable;
import java.util.List;
/**
 * <p>
 * 分页结果
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class PageResult<T> implements Serializable {
	private static final long serialVersionUID = -1750386840274995765L;

	/**
	 * 总记录数
	 */
	private long total;

	/**
	 * 查询出的结果数
	 */
	private transient List<T> rows;
 
	public PageResult() {
		super();
	}
 
	public PageResult(long total, List<T> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
 
	@Override
	public String toString() {
		return "PageResult [total=" + total + ", rows=" + rows + "]";
	}
 
	public long getTotal() {
		return total;
	}
 
	public void setTotal(long total) {
		this.total = total;
	}
 
	public List<T> getRows() {
		return rows;
	}
 
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
 
}
