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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * 报表组件列组实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_component_col_group")
@ApiModel(value = "报表组件列组实体类",
        description = "报表组件列组实体类(CfgComponentColGroup),Table(rpt_cfg_component_col_group)",
        parent = BaseEntity.class)
public class CfgComponentColGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    @ApiModelProperty(value = "是否显示",name="display")
    private String display;


    public CfgComponentColGroup() {
    }

    public CfgComponentColGroup(
            String id
    ) {
        this.id = id;
    }


    public void setId(String value) {
        this.id = value;
    }

    @Id
    @Column(name = "ccg_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    @Column(name = "ccg_name")
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Column(name = "ccg_display")
    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String value) {
        this.display = value;
    }

    @Column(name = "ccg_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "ccg_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "ccg_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "ccg_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "ccg_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "ccg_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }


    /**
     private List<CfgComponentCol> cfgComponentCols = new ArrayList<CfgComponentCol>();
     public void setCfgComponentCols(List<CfgComponentCol> cfgComponentCol){
     this.cfgComponentCols = cfgComponentCol;
     }

     @JsonIgnore
     @OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "cfgComponentColGroup")
     public List<CfgComponentCol> getCfgComponentCols() {
     return cfgComponentCols;
     }
     **/

}

