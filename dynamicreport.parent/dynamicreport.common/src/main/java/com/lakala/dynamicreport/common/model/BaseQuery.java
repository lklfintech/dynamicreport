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

import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 基础查询类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "基础查询类",description = "基础查询类(BaseQuery)")
public class BaseQuery implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "当前页",name="pageNumber")
	public int pageNumber = 0;

	@ApiModelProperty(value = "每页显示记录数",name="pageSize")
	public int pageSize = 10;

	@ApiModelProperty(value = "排序字段名",name="sortName")
	public String sortName = null;

	@ApiModelProperty(value = "排序规则(asc,desc)",name="sortOrder")
	public String sortOrder = null;

	@ApiModelProperty(value = "支持多字段排序(eg: date desc, sequence asc)",name="sortList")
	@IgnoreSwagger2Parameter
	public List<Sort> sortList;

	@ApiModelProperty(value = "下拉框查询",name="searchText")
	public String searchText;

	@ApiModelProperty(value = "状态",name="status")
	public String status;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public List<Sort> getSortList() {
		return sortList;
	}

	public void setSortList(List<Sort> sortList) {
		this.sortList = sortList;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
