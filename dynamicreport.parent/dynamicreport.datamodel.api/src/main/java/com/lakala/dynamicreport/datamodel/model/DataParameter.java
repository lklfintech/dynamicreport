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
package com.lakala.dynamicreport.datamodel.model;

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
 * 数据参数实体
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "dm_data_parameter")
@ApiModel(value = "数据参数实体",description = "数据参数实体(DataParameter),Table(dm_data_parameter)",
        parent = BaseEntity.class)
public class DataParameter extends BaseEntity {

    private static final long serialVersionUID = 1317118270171584666L;


    @ApiModelProperty(value = "数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "唯一标识",name="identifier")
    private String identifier;

    @ApiModelProperty(value = "参数英文名",name="paramName")
    private String paramName;

    @ApiModelProperty(value = "参数中文名",name="name")
    private String name;

    @ApiModelProperty(value = "参数内容",name="paramContent")
    private String paramContent;

    @ApiModelProperty(value = "参数前缀",name="prefix")
    private String prefix;

    @ApiModelProperty(value = "参数后缀",name="suffix")
    private String suffix;

    @ApiModelProperty(value = "是否必要",name="mandatory")
    private String mandatory;

    @ApiModelProperty(value = "描述",name="description")
    private String description;

    @ApiModelProperty(value = "是否选中",name="selected")
    private boolean selected;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "dp_id")
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "dp_identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Column(name = "dp_param_name")
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Column(name = "dp_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "dp_param_content")
    public String getParamContent() {
        return paramContent;
    }

    public void setParamContent(String paramContent) {
        this.paramContent = paramContent;
    }

    @Column(name = "dp_prefix")
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Column(name = "dp_suffix")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Column(name = "dp_mandatory")
    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    @Column(name = "dp_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "dp_status")
    public String getStatus() {
        return status;
    }

    @Column(name = "dp_created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dp_created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @Column(name = "dp_modified_user")
    public String getModifiedUser() {
        return modifiedUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dp_modified_date")
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    @Version
    @Column(name = "dp_version_no")
    public int getVersionNo() {
        return versionNo;
    }

    @Transient
    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
