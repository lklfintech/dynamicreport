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
package com.lakala.dynamicreport.datamodel.query;

import com.lakala.dynamicreport.common.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 数据集关联组查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "数据集关联组查询对象",description = "数据集关联组查询对象(DataListRelParamterGroupQuery)",
		parent = BaseQuery.class)
public class DataListRelParamterGroupQuery extends BaseQuery {

	private static final long serialVersionUID = 2784529794293373570L;


	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "集合ID",name="dataList")
	private Long dataList;

	@ApiModelProperty(value = "组ID",name="name")
	private Long dataParameterGroup;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDataList() {
		return dataList;
	}
	public void setDataList(Long dataList) {
		this.dataList = dataList;
	}
	public Long getDataParameterGroup() {
		return dataParameterGroup;
	}
	public void setDataParameterGroup(Long dataParameterGroup) {
		this.dataParameterGroup = dataParameterGroup;
	}

}
