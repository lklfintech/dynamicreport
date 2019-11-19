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
package com.lakala.dynamicreport.common.model;

import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 基础实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "基础实体类",description = "基础实体类(BaseEntity)")
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 6669572347645050554L;

	@ApiModelProperty(value = "状态",name = "status")
	protected String status;

	@ApiModelProperty(value = "创建人",name="createdUser")
	protected String createdUser;

	@ApiModelProperty(value = "创建日期",name="createdDate")
	@IgnoreSwagger2Parameter
	protected Timestamp createdDate;

	@ApiModelProperty(value = "修改人",name="modifiedUser")
	protected String modifiedUser;

	@ApiModelProperty(value = "修改日期",name="modifiedDate")
	@IgnoreSwagger2Parameter
	protected Timestamp modifiedDate;

	@ApiModelProperty(value = "版本号",name="versionNo")
	@IgnoreSwagger2Parameter
	protected int versionNo;

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

}
