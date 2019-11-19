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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lakala.dynamicreport.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * 报表关联下钻表实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_col_drill_param")
@ApiModel(value = "报表关联下钻表实体类",description = "报表关联下钻表实体类(CfgColDrillParam),Table(rpt_cfg_col_drill_param)",
        parent = BaseEntity.class)
public class CfgColDrillParam extends BaseEntity {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "参数名称",name="paramName")
    private String paramName;

    @ApiModelProperty(value = "指向参数名称",name="tgtParamName")
    private String tgtParamName;

    @ApiModelProperty(value = "报表组件列",name="cfgComponentCol")
    private CfgComponentCol cfgComponentCol;


    public CfgColDrillParam() {
    }

    public CfgColDrillParam(String id) {
        this.id = id;
    }

    @Id
    @Column(name = "dp_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Column(name = "dp_param_name")
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Column(name = "dp_tgt_param_name")
    public String getTgtParamName() {
        return tgtParamName;
    }

    public void setTgtParamName(String tgtParamName) {
        this.tgtParamName = tgtParamName;
    }

    @ManyToOne
    @JoinColumn(name = "dp_component_col")
    @JsonBackReference
    public CfgComponentCol getCfgComponentCol() {
        return cfgComponentCol;
    }

    public void setCfgComponentCol(CfgComponentCol cfgComponentCol) {
        this.cfgComponentCol = cfgComponentCol;
    }
}

