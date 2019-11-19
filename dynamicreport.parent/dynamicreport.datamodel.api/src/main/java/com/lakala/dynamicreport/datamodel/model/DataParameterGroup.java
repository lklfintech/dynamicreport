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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * 数据参数组实体
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "dm_data_parameter_group")
@ApiModel(value = "数据参数组实体",description = "数据参数组实体(DataParameterGroup),Table(dm_data_parameter_group)",
        parent = BaseEntity.class)
public class DataParameterGroup extends BaseEntity {

    private static final long serialVersionUID = 1317118270171584666L;


    @ApiModelProperty(value = "数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "唯一标识",name="identifier")
    private String identifier;

    @ApiModelProperty(value = "参数名称",name="name")
    private String name;

    @ApiModelProperty(value = "参数内容",name="content")
    private String content;

    @ApiModelProperty(value = "描述",name="description")
    private String description;

    @ApiModelProperty(value = "是否选中",name="selected")
    private boolean selected;

    @ApiModelProperty(value = "模块下关联的数据参数",name="dataParameter")
    private List<DataParameter> dataParameter;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "dpg_id")
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "dpg_identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Column(name = "dpg_name")
    public String getName() {
        return name;
    }

    @Column(name = "dpg_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "dpg_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dm_param_group_rel_param", joinColumns = {@JoinColumn(name = "pgrp_data_parameter_group", referencedColumnName = "dpg_id")}, inverseJoinColumns = {@JoinColumn(name = "pgrp_data_parameter", referencedColumnName = "dp_id")})
    public List<DataParameter> getDataParameter() {
        return dataParameter;
    }

    public void setDataParameter(List<DataParameter> dataParameter) {
        this.dataParameter = dataParameter;
    }


    @Column(name = "dpg_status")
    public String getStatus() {
        return status;
    }

    @Column(name = "dpg_created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dpg_created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @Column(name = "dpg_modified_user")
    public String getModifiedUser() {
        return modifiedUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dpg_modified_date")
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    @Version
    @Column(name = "dpg_version_no")
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
