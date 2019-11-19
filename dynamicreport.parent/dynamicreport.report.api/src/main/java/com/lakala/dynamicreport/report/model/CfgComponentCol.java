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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lakala.dynamicreport.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * 报表组件列实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_component_col")
@NamedEntityGraph(name = "CfgComponentCol.Graph", attributeNodes = {@NamedAttributeNode("component"), @NamedAttributeNode("group")})
@ApiModel(value = "报表组件列实体类",description = "报表组件列实体类(CfgComponentCol),Table(rpt_cfg_component_col)",
        parent = BaseEntity.class)
public class CfgComponentCol extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "报表组件列组实体类",name="group")
    private CfgComponentColGroup group;

    @ApiModelProperty(value = "报表组件实体类",name="component")
    private CfgComponent component;

    @ApiModelProperty(value = "报表页面实体类",name="page")
    private CfgPage page;

    @ApiModelProperty(value = "类型",name="type")
    private String type;

    @ApiModelProperty(value = "中文名称",name="name")
    private String name;

    @ApiModelProperty(value = "列名",name="colName")
    private String colName;

    @ApiModelProperty(value = "函数",name="func")
    private String func;

    @ApiModelProperty(value = "别名",name="alias")
    private String alias;

    @ApiModelProperty(value = "长度",name="len")
    private String len;

    @ApiModelProperty(value = "是否选中",name="selected")
    private String selected;

    @ApiModelProperty(value = "行合并",name="rowMerge")
    private String rowMerge;

    @ApiModelProperty(value = "行小计",name="rowSummary")
    private String rowSummary;

    @ApiModelProperty(value = "是否透视",name="privot")
    private String privot;

    @ApiModelProperty(value = "是否透视选择",name="privotSelected")
    private String privotSelected;

    @ApiModelProperty(value = "顺序",name="sequence")
    private Integer sequence;

    @ApiModelProperty(value = "分组ID",name="groupId")
    private String groupId;

    @ApiModelProperty(value = "模块下关联的报表关联下钻表实体",name="drillParamList")
    private List<CfgColDrillParam> drillParamList;


    public CfgComponentCol() {
    }

    public CfgComponentCol(String id) {
        this.id = id;
    }

    @Id
    @Column(name = "cc_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Column(name = "cc_type")
    public String getType() {
        return this.type;
    }

    public void setType(String value) {
        this.type = value;
    }

    @Column(name = "cc_name")
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Column(name = "cc_col_name")
    public String getColName() {
        return this.colName;
    }

    public void setColName(String value) {
        this.colName = value;
    }

    @Column(name = "cc_col_func")
    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    @Column(name = "cc_col_alias")
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "cc_len")
    public String getLen() {
        return this.len;
    }

    public void setLen(String value) {
        this.len = value;
    }

    @Column(name = "cc_selected")
    public String getSelected() {
        return this.selected;
    }

    public void setSelected(String value) {
        this.selected = value;
    }

    @Column(name = "cc_row_merge")
    public String getRowMerge() {
        return rowMerge;
    }

    public void setRowMerge(String rowMerge) {
        this.rowMerge = rowMerge;
    }

    @Column(name = "cc_row_summary")
    public String getRowSummary() {
        return rowSummary;
    }

    public void setRowSummary(String rowSummary) {
        this.rowSummary = rowSummary;
    }

    @Column(name = "cc_pivot")
    public String getPrivot() {
        return privot;
    }

    public void setPrivot(String privot) {
        this.privot = privot;
    }

    @Column(name = "cc_pivot_selected")
    public String getPrivotSelected() {
        return privotSelected;
    }

    public void setPrivotSelected(String privotSelected) {
        this.privotSelected = privotSelected;
    }

    @Column(name = "cc_sequence")
    public Integer getSequence() {
        return this.sequence;
    }

    public void setSequence(Integer value) {
        this.sequence = value;
    }

    @Column(name = "cc_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cc_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "cc_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cc_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "cc_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "cc_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cc_group")
    public CfgComponentColGroup getGroup() {
        return group;
    }

    public void setGroup(CfgComponentColGroup group) {
        this.group = group;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cc_component")
    public CfgComponent getComponent() {
        return component;
    }

    public void setComponent(CfgComponent component) {
        this.component = component;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cc_page")
    @JsonManagedReference
    @NotFound(action=NotFoundAction.IGNORE)
    public CfgPage getPage() {
        return page;
    }

    public void setPage(CfgPage page) {
        this.page = page;
    }

    @OneToMany(cascade ={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.REFRESH,CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dp_component_col")
    @JsonManagedReference
    public List<CfgColDrillParam> getDrillParamList() {
        return drillParamList;
    }

    public void setDrillParamList(List<CfgColDrillParam> drillParamList) {
        this.drillParamList = drillParamList;
    }

    @Transient
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}

