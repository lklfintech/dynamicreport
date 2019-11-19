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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_user")
@ApiModel(value = "用户实体类",description = "用户实体类(User),Table(sys_user)",
		parent = BaseEntity.class)
public class User extends BaseEntity {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private String   id;

	@ApiModelProperty(value = "用户登录名",name="name")
	private String   name;

	@ApiModelProperty(value = "用户昵称",name="nickName")
	private String   nickName;

	@ApiModelProperty(value = "邮箱",name="email")
	private String   email;

	@ApiModelProperty(value = "电话",name="phone")
	private String   phone;

	@ApiModelProperty(value = "姓别",name="sex")
	private Integer  sex;

	@ApiModelProperty(value = "密码",name="password")
	private String   password;

	@ApiModelProperty(value = "地址",name="address")
	private String   address;

	@ApiModelProperty(value = "生日",name="birthday")
	private  Date    birthday;

	@ApiModelProperty(value = "是否锁定",name="locked")
	private Integer  locked;

	@ApiModelProperty(value = "用户地址",name="department")
	private Integer  department;

	@ApiModelProperty(value = "备注",name="remark")
	private String   remark;
	
	@ApiModelProperty(value = "是否选中",name="selected")
	private Boolean selected;

	@ApiModelProperty(value = "旧密码",name="oldPassword")
	private String oldPassword;

	@ApiModelProperty(value = "模型下面关联的角色",name="roles")
	@IgnoreSwagger2Parameter
	private List<Role> roles;
	

 	  
	@Id
  	@Column(name = "usr_id")
  	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
    
	
	@Column(name = "usr_name")
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "usr_nick_name")
	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name = "usr_email")  
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "usr_phone")  
	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "usr_sex")  
	public Integer getSex() {
		return sex;
	}


	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "usr_password")  
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "usr_address")  
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "usr_birthday") 
	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "usr_locked")  
	public Integer getLocked() {
		return locked;
	}


	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	@Column(name = "usr_department")  
	public Integer getDepartment() {
		return department;
	}


	public void setDepartment(Integer department) {
		this.department = department;
	}

	@Column(name = "usr_remark")  
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
     
 	@Column(name = "usr_status")
	public String getStatus() {
		return status;
	}
	
	@Transient
	public Boolean getSelected() {
		return selected;
	}

	
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
 
	@Column(name = "usr_created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "usr_created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Column(name = "usr_modified_user")
	public String getModifiedUser() {
		return modifiedUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "usr_modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Version
	@Column(name = "usr_version_no")
	public int getVersionNo() {
		return versionNo;
	}
	

	@JsonIgnore
	@ManyToMany  
	@JoinTable(name = "sys_user_role", joinColumns = { @JoinColumn(name = "ur_user", referencedColumnName = "usr_id") }, inverseJoinColumns = { @JoinColumn(name = "ur_role", referencedColumnName = "role_id") })
 	public List<Role> getRoles() {
		return roles;
	}
    
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	
	@Transient
	public String getOldPassword() {
		return oldPassword;
	}


	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
