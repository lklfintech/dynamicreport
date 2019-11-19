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

/**
 * <p>
 * 分页对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class PageParam{
	/**
	 * 当前页码
	 */
	private int page = 1;

	/**
	 * 每页的记录数
	 */
	private int rows = 10;

	/**
	 * SQL查询起始INDEX
	 */
	private int startIndex;

	/**
	 * SQL查询结束INDEX
	 */
	private int endIndex;
 
	private void calculateIndex() {
		this.startIndex = (page - 1) * rows;
		this.endIndex = startIndex + rows;
	}
 
	public PageParam() {
		super();
		this.calculateIndex();
	}
 
	public PageParam(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
		this.calculateIndex();
	}
 
	public void setPage(int page) {
		this.page = page;
		this.calculateIndex();
	}
 
	public void setRows(int rows) {
		this.rows = rows;
		this.calculateIndex();
	}
 
	@Override
	public String toString() {
		return "PageParam [page=" + page + ", rows=" + rows + "]";
	}
 
	public int getStartIndex() {
		return startIndex;
	}
 
	public int getEndIndex() {
		return endIndex;
	}
	public int getPage() {
		return page;
	}
	
	public int getRows() {
		return rows;
	}
}

