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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lakala.dynamicreport.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <p>
 * 缓存服务实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_cache_service")
@ApiModel(value = "缓存服务实体类",description = "缓存服务实体类(CacheService),Table(sys_cache_service)",
		parent = BaseEntity.class)
public class CacheService extends BaseEntity {

	private static final long serialVersionUID = 1317118270171584666L;


	@ApiModelProperty(value = "数据主键",name="id")
	private Long id;

	@ApiModelProperty(value = "上下文路径",name="contextPath")
 	private String  contextPath;

	@ApiModelProperty(value = "请求路径",name="servicePath")
	private String  servicePath;

	@ApiModelProperty(value = "请求方式",name="httpmethod")
	private String  httpmethod;

	@ApiModelProperty(value = "服务配置实体",name="serverList")
	private ServerList  serverList;

	@ApiModelProperty(value = "描述",name="description")
	private String  description;
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "snowFlakeId")
	@GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
	@Column(name = "cs_id")
	public Long getId() { 
		return id;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public void setId(Long id) {
		this.id = id;
	}
  	
	@ManyToOne
	@JoinColumn(name = "cs_server")
	public ServerList getServerList() {
		return serverList;
	}

	public void setServerList(ServerList serverList) {
		this.serverList = serverList;
	}

	@Column(name = "cs_context_path")
	public String getContextPath() {
		return contextPath;
	}
	
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	@Column(name = "cs_service_path")
	public String getServicePath() {
		return servicePath;
	}
	
	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}
	
	@Column(name = "cs_http_method")
	public String getHttpmethod() {
		return httpmethod;
	}
	
	public void setHttpmethod(String httpmethod) {
		this.httpmethod = httpmethod;
	}
	
	@Column(name = "cs_description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
  
	@Column(name = "cs_status")
	public String getStatus() {
		return status;
	}

	@Column(name = "cs_created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "cs_created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Column(name = "cs_modified_user")
	public String getModifiedUser() {
		return modifiedUser;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "cs_modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Version
	@Column(name = "cs_version_no")
	public int getVersionNo() {
		return versionNo;
	}
  

}
