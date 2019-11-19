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

import com.lakala.dynamicreport.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 组关联数据参数实体查询
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "组关联数据参数实体查询",description = "组关联数据参数实体查询(ParameterGroupRelParameterQuery)",
		parent = BaseEntity.class)
public class ParameterGroupRelParameterQuery extends BaseEntity {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "参数",name="dataParameter")
	private Long dataParameter;

	@ApiModelProperty(value = "参数组",name="dataParameterGroup")
	private Long dataParameterGroup;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDataParameter() {
		return dataParameter;
	}
	public void setDataParameter(Long dataParameter) {
		this.dataParameter = dataParameter;
	}
	public Long getDataParameterGroup() {
		return dataParameterGroup;
	}
	public void setDataParameterGroup(Long dataParameterGroup) {
		this.dataParameterGroup = dataParameterGroup;
	}
	 
}
