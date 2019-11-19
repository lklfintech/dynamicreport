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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lakala.dynamicreport.common.model.BaseEntity;
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * 报表组件类型实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_component_type")
@NamedEntityGraph(name = "CfgComponentType.Graph", attributeNodes = {@NamedAttributeNode("parent")})
@ApiModel(value = "报表组件类型实体类",description = "报表组件类型实体类(CfgComponentType),Table(rpt_cfg_component_type)",
        parent = BaseEntity.class)
public class CfgComponentType extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "标识key",name="key")
    private String key;

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    @ApiModelProperty(value = "报表组件类型实体类",name="parent")
    private CfgComponentType parent;

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    @ApiModelProperty(value = "排序",name="sequence")
    private Integer sequence;

    @ApiModelProperty(value = "模块下关联的报表组件类型",name="childrens")
    @IgnoreSwagger2Parameter
    private List<CfgComponentType> childrens;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "ct_id")
    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Column(name = "ct_key")
    public String getKey() {
        return this.key;
    }

    public void setKey(String value) {
        this.key = value;
    }

    @Column(name = "ct_name")
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ct_parent")
    public CfgComponentType getParent() {
        return this.parent;
    }

    public void setParent(CfgComponentType value) {
        this.parent = value;
    }

    @Column(name = "ct_remark")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String value) {
        this.remark = value;
    }

    @Column(name = "ct_sequence")
    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    @Column(name = "ct_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "ct_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "ct_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "ct_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "ct_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "ct_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    public List<CfgComponentType> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<CfgComponentType> childrens) {
        this.childrens = childrens;
    }

}

