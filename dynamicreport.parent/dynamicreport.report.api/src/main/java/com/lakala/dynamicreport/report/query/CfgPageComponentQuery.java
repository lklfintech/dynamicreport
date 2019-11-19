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
package com.lakala.dynamicreport.report.query;

import com.lakala.dynamicreport.common.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 报表页面组件查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "报表页面组件查询对象",description = "报表页面组件查询对象(CfgPageComponentQuery)",
		parent = BaseQuery.class)
public class CfgPageComponentQuery extends BaseQuery {
	
	private static final long serialVersionUID = -1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private String id;

	@ApiModelProperty(value = "页面",name="page")
    private String page;

	@ApiModelProperty(value = "组件",name="component")
    private String component;
	
	public String getId() {
		return this.id;
	}
	public void setId(String value) {
		this.id = value;
	}	
    public String getPage() {
		return this.page;
	}
	public void setPage(String value) {
		this.page = value;
	}
    public String getComponent() {
		return this.component;
	}
	public void setComponent(String value) {
		this.component = value;
	}
   
}