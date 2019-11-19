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

import java.util.List;

/**
 * <p>
 * 数据参数查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "数据参数查询对象",description = "数据参数查询对象(DataParameterQuery)",
		parent = BaseQuery.class)
public class DataParameterQuery extends BaseQuery {

	public DataParameterQuery(){
		
	}
	public DataParameterQuery(String identifier){
		this.identifier=identifier;
	}


	private static final long serialVersionUID = 2784529794293373570L;

	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "唯一标识",name="identifier")
	private String identifier;

	@ApiModelProperty(value = "参数英文名",name="paramName")
	private String paramName;

	@ApiModelProperty(value = "参数中文名",name="name")
	private String name;

	@ApiModelProperty(value = "参数内容",name="paramContent")
	private String paramContent;

	@ApiModelProperty(value = "ID集合",name="ids")
	private List<Long> ids;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParamContent() {
		return paramContent;
	}
	public void setParamContent(String paramContent) {
		this.paramContent = paramContent;
	}
	public List<Long> getIds() {
		return ids;
	}
	public void setIds(List<Long> ids) {
		this.ids = ids;
	}
}
