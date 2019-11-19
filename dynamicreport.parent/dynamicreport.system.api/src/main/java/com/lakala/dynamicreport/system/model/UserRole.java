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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * 用户角色关联实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_user_role")
@ApiModel(value = "用户角色关联实体类",description = "用户角色关联实体类(UserRole),Table(sys_user_role)")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 8296748402340778121L;


	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "用户实体",name="user")
	private User user;

	@ApiModelProperty(value = "角色实体",name="role")
	private Role role;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "snowFlakeId")
	@GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
	@Column(name = "ur_id")
	public Long getId() {
		return id;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public void setId(Long id) {
		this.id = id;
	}
    
	@ManyToOne
	@JoinColumn(name = "ur_user")
	public User getUser() {
		return user;
	}

 	public void setUser(User user) {
		this.user = user;
	}

	
	
	@ManyToOne
	@JoinColumn(name = "ur_role")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
 
}
