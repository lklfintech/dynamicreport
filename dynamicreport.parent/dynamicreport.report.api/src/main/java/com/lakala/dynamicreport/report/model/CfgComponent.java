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
package com.lakala.dynamicreport.report.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lakala.dynamicreport.common.model.BaseEntity;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * 报表组件实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_component")
@NamedEntityGraph(name = "CfgComponent.Graph", attributeNodes = {@NamedAttributeNode("type"), @NamedAttributeNode("cfgChartTemplate")})
@ApiModel(value = "报表组件实体类",description = "报表组件实体类(CfgComponent),Table(rpt_cfg_component)",
        parent = BaseEntity.class)
public class CfgComponent extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    @ApiModelProperty(value = "数据集实体",name="dataList")
    private DataList dataList;

    @ApiModelProperty(value = "列固定",name="fixedCol")
    private String fixedCol;

    @ApiModelProperty(value = "是否分页",name="pagination")
    private String pagination;

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    @ApiModelProperty(value = "报表组件类型实体类",name="type")
    @IgnoreSwagger2Parameter
    private CfgComponentType type;

    @ApiModelProperty(value = "报表图形模板实体类",name="cfgChartTemplate")
    @IgnoreSwagger2Parameter
    private CfgChartTemplate cfgChartTemplate;

    @ApiModelProperty(value = "是否选中",name="selected")
    private boolean selected;

    public CfgComponent() {
    }

    public CfgComponent(String id) {
        this.id = id;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Id
    @Column(name = "cmpt_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    @Column(name = "cmpt_name")
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @ManyToOne
    @JoinColumn(name = "cmpt_data_list_id")
    public DataList getDataList() {
        return this.dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    @Column(name = "cmpt_fixed_col")
    public String getFixedCol() {
        return fixedCol;
    }

    public void setFixedCol(String fixedCol) {
        this.fixedCol = fixedCol;
    }

    @Column(name = "cmpt_pagination")
    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    @Column(name = "cmpt_remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "cmpt_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cmpt_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "cmpt_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cmpt_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "cmpt_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "cmpt_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }

    public void setType(CfgComponentType type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "cmpt_type")
    public CfgComponentType getType() {
        return type;
    }

    public void setCfgChartTemplate(CfgChartTemplate cfgChartTemplate) {
        this.cfgChartTemplate = cfgChartTemplate;
    }

    @ManyToOne
    @JoinColumn(name = "cmpt_template")
    public CfgChartTemplate getCfgChartTemplate() {
        return cfgChartTemplate;
    }

    @Transient
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}

