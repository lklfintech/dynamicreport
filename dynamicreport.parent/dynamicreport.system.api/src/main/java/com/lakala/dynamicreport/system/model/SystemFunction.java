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
package com.lakala.dynamicreport.system.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lakala.dynamicreport.common.model.BaseEntity;
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 功能实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_function")
@ApiModel(value = "功能实体类",description = "功能实体类(SystemFunction),Table(sys_function)",
		parent = BaseEntity.class)
public class SystemFunction extends BaseEntity {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private String id;

	@ApiModelProperty(value = "功能名称",name="name")
	private String name;

	@ApiModelProperty(value = "功能父类",name="parent")
	private SystemFunction parent;

	@ApiModelProperty(value = "排序",name="sequence")
	private Integer sequence;

	@ApiModelProperty(value = "描述",name="description")
	private String description;

	@ApiModelProperty(value = "类型：菜单还是按钮",name="type")
	private String type;

	@ApiModelProperty(value = "是否选中",name="selected")
	private Boolean selected;

	@ApiModelProperty(value = "模块下关联的功能",name="childrens")
	@IgnoreSwagger2Parameter
	private List<SystemFunction> childrens = new ArrayList<>();


	@Id
	@Column(name = "func_id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "func_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(name = "func_parent")
	public SystemFunction getParent() {
		return parent;
	}

	public void setParent(SystemFunction parent) {
		this.parent = parent;
	}

	@Column(name = "func_sequence")
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Column(name = "func_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "parent")
	public List<SystemFunction> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<SystemFunction> childrens) {
		this.childrens = childrens;
	}

	@Transient
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

  	@Column(name = "func_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "func_status")
	public String getStatus() {
		return status;
	}

	@Column(name = "func_created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "func_created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Column(name = "func_modified_user")
	public String getModifiedUser() {
		return modifiedUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "func_modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Version
	@Column(name = "func_version_no")
	public int getVersionNo() {
		return versionNo;
	}

}
