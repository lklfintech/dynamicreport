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
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * <p>
 * 报表页面查询参数
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "报表页面查询参数",description = "报表页面查询参数(CfgPageQuery)",
		parent = BaseQuery.class)
public class CfgPageQuery extends BaseQuery {
	
	private static final long serialVersionUID = -1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private String id;

	@ApiModelProperty(value = "标题",name="title")
	private String title;

	@ApiModelProperty(value = "状态",name="status")
	private String status;

	@ApiModelProperty(value = "创建人",name="createdUser")
	private String createdUser;

	@ApiModelProperty(value = "版本号",name="versionNo")
	private Integer versionNo;

	@ApiModelProperty(value = "用户",name="users")
	@IgnoreSwagger2Parameter
	private List<String> users;
	
	public String getId() {
		return this.id;
	}
	public void setId(String value) {
		this.id = value;
	}	
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String value) {
		this.title = value;
	}	
	@Override
	public String getStatus() {
		return this.status;
	}
	@Override
	public void setStatus(String value) {
		this.status = value;
	}	
	public String getCreatedUser() {
		return this.createdUser;
	}
	public void setCreatedUser(String value) {
		this.createdUser = value;
	}	
	public Integer getVersionNo() {
		return this.versionNo;
	}
	public void setVersionNo(Integer value) {
		this.versionNo = value;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
}