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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lakala.dynamicreport.common.model.BaseEntity;
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 角色实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_role")
@ApiModel(value = "角色实体类",description = "角色实体类(Role),Table(sys_role)",
		parent = BaseEntity.class)
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1317118270171584666L;
	

	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "角色名称",name="name")
	private String name;

	@ApiModelProperty(value = "角色描述",name="description")
	private String description;

	@ApiModelProperty(value = "角色key",name="key")
	private String key;

	@ApiModelProperty(value = "是否选中",name="selected")
	private Boolean selected;


	@ApiModelProperty(value = "模型下面关联的角色",name="systemFunctions")
	@IgnoreSwagger2Parameter
	private List<SystemFunction> systemFunctions;

	@ApiModelProperty(value = "模型下面的用户",name="users")
	@IgnoreSwagger2Parameter
	private List<User> users;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "snowFlakeId")
	@GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
	@Column(name = "role_id")
	public Long getId() {
		return id;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "role_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "role_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "role_key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Transient
	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	@Column(name = "role_status")
	public String getStatus() {
		return status;
	}

	@Column(name = "role_created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "role_created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Column(name = "role_modified_user")
	public String getModifiedUser() {
		return modifiedUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "role_modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Version
	@Column(name = "role_version_no")
	public int getVersionNo() {
		return versionNo;
	}

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "ur_role", referencedColumnName = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "ur_user", referencedColumnName = "usr_id") })
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@JsonIgnore
	@ManyToMany  
	@JoinTable(name = "sys_role_function", joinColumns = { @JoinColumn(name = "rf_role", referencedColumnName = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "rf_function", referencedColumnName = "func_id") })
 	public List<SystemFunction> getSystemFunctions() {
		return systemFunctions;
	}

	public void setSystemFunctions(List<SystemFunction> systemFunctions) {
		this.systemFunctions = systemFunctions;
	}

}
