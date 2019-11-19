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
package com.lakala.dynamicreport.common.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分页实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class PageBean<T> implements Serializable {
	
	private static final long serialVersionUID = 5229229913348492552L;

	/**
	 * 当前是第几页，可写
	 */
	@ApiModelProperty(value = "当前页",name="pageNo")
	private int pageNo = 1;
	/**
	 * 一共有多少页，可读
	 */
	@ApiModelProperty(value = "总页数",name="pageTotal")
	private int pageTotal;
	/**
	 * 一共有多少行，可读
	 */
	@ApiModelProperty(value = "总行数",name="rowsCount")
	private Long rowsCount = 0L;
	/**
	 * 每页多少行，可写
	 */
	@ApiModelProperty(value = "每页显示条数",name="rowsPerPage")
	private int rowsPerPage = 10;
	/**
	 * 本页中显示的数据集合，可读
	 */
	@ApiModelProperty(value = "数据集合",name="data")
	private List<T> data;
	/**
	 * 排序方式，可写
	 */
	@ApiModelProperty(value = "排序方式",name="sortOrder")
	private String sortOrder = "asc";
	/**
	 * 排序字段，可写，必须
	 */
	@ApiModelProperty(value = "排序字段",name="sort")
	private String sort = "";
	
	public PageBean() {
		super();
	}
	public boolean isNotNullOrEmpty(String s) {
		if (StringUtils.isBlank(s)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	
	/**
	 * @return the curPage
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * @param pageNo
	 *            the page number to set
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo < 1 ? 1 : pageNo;
	}

	/**
	 * 得到总共有多少页
	 * 
	 * @return the maxPage
	 */
	public int getPageTotal() {
		pageTotal = (this.rowsCount.intValue() - 1) / this.getRowsPerPage() + 1;
		return pageTotal;
	}

	/**
	 * @param pageTotal
	 *            the maxPage to set
	 */
	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	/**
	 * @return the maxRow
	 */
	public Long getRowsCount() {
		return rowsCount;
	}

	/**
	 * @param rowsCount
	 *            the maxRow to set
	 */
	public void setRowsCount(Long rowsCount) {
		this.rowsCount = rowsCount;
	}

	/**
	 * 得到每页显示多少条记录
	 * 
	 * @return the rowsPerPage
	 */
	public int getRowsPerPage() {
		return rowsPerPage <= 0 ? 5 : rowsPerPage;
	}

	/**
	 * @param rowsPerPage
	 *            the rowsPerPage to set
	 */
	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

}
