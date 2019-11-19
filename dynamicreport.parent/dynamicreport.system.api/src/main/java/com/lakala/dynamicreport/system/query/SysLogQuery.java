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
package com.lakala.dynamicreport.system.query;

import com.lakala.dynamicreport.common.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;

/**
 * <p>
 * 用户操作日志查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "用户操作日志查询对象",description = "用户操作日志查询对象(SysLogQuery)",
        parent = BaseQuery.class)
public class SysLogQuery extends BaseQuery {
  
    private static final long serialVersionUID = 6375107541581233678L;


    @ApiModelProperty(value = "ip",name="ip")
    private String ip;

    @ApiModelProperty(value = "ulr地址",name="ulr")
    private String ulr;

    @ApiModelProperty(value = "用户名",name="userName")
    private String userName;

    @ApiModelProperty(value = "起始时间",name="startDateTime")
    private Timestamp startDateTime;

    @ApiModelProperty(value = "结束时间",name="endDateTime")
    private Timestamp endDateTime;

    @ApiModelProperty(value = "类型",name="type")
    private String type; 
    
    @ApiModelProperty(value = "操作行为",name="action")
    private String action; 

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

 
    public String getUlr() {
        return ulr;
    }

    public void setUlr(String ulr) {
        this.ulr = ulr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

 
	public Timestamp getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Timestamp startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Timestamp getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Timestamp endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
    
 
}
