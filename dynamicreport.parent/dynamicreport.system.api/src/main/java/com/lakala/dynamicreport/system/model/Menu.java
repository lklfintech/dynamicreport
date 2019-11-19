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
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_menu")
@ApiModel(value = "菜单实体类",description = "菜单实体类(Menu),Table(sys_menu)",
		parent = BaseEntity.class)
public class Menu extends BaseEntity {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "菜单名称",name="name")
	private String name;

	@ApiModelProperty(value = "菜单父类",name="parent")
	private Menu parent;

	@ApiModelProperty(value = "排序",name="sequence")
	private Integer sequence;

	@ApiModelProperty(value = "描述",name="description")
	private String description;

	@ApiModelProperty(value = "菜单的路径",name="path")
	private String  path;

	@ApiModelProperty(value = "菜单的图标",name="icon")
	private String  icon;

	@ApiModelProperty(value = "菜章属于的功能",name="systemFunction")
 	private SystemFunction systemFunction;

	@ApiModelProperty(value = "菜单下的子类",name="childrens")
	@IgnoreSwagger2Parameter
  	private List<Menu> childrens = new ArrayList<>();

  	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "snowFlakeId")
	@GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
	@Column(name = "menu_id")
	public Long getId() {
		return id;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "menu_name") 
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	@ManyToOne
	@JoinColumn(name = "menu_parent_menu")
	public Menu getParent() {
		return parent;
	}
	
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	@Column(name = "menu_sequence") 
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Column(name = "menu_description") 
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
	@Column(name = "menu_path") 
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonIgnore
	@OneToMany(mappedBy = "parent")
	@OrderBy("sequence asc")
	public List<Menu> getChildMenu() {
		return childrens;
	}

  	@Column(name = "menu_icon") 
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setChildMenu(List<Menu> childMenu) {
		this.childrens = childMenu;
	}

	
	@OneToOne
	@JoinColumn(name = "menu_function")
	public SystemFunction getSystemFunction() {
		return systemFunction;
	}

	public void setSystemFunction(SystemFunction systemFunction) {
		this.systemFunction = systemFunction;
	}

	@Column(name = "menu_status")
	public String getStatus() {
		return status;
	}
    
	@Column(name = "menu_created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "menu_created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Column(name = "menu_modified_user")
	public String getModifiedUser() {
		return modifiedUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "menu_modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Version
	@Column(name = "menu_version_no")
	public int getVersionNo() {
		return versionNo;
	}
 	
    
}
