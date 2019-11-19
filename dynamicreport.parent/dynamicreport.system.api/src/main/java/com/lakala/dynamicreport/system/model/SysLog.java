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
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 用户操作日志实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "sys_log")
@ApiModel(value = "用户操作日志实体类",description = "用户操作日志实体类(SysLog),Table(sys_log)")
public class SysLog implements Serializable {
 
    private static final long serialVersionUID = 3895960115349274862L;


    @ApiModelProperty(value = "数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "操作的用户名",name="userName")
    private String userName; 

    @ApiModelProperty(value = "操作URL",name="url")
    private String url;
    
    @ApiModelProperty(value = "参数",name="params")
    private String params; 
  
    @ApiModelProperty(value = "ip地址",name="ip")
    private String ip;

    @ApiModelProperty(value = "类型",name="type")
    private String type;
  
    @ApiModelProperty(value = "信息",name="message")
    private String message;

    @ApiModelProperty(value = "操作时间",name="createDate")
    @IgnoreSwagger2Parameter
    private Timestamp createDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "log_id")
    public Long getId() {
        return id;
    }
    
    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }
     
    @Column(name = "log_user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "log_url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "log_ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    @Column(name = "log_params")
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "log_created_date")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Column(name = "log_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "log_message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
    
   
}
