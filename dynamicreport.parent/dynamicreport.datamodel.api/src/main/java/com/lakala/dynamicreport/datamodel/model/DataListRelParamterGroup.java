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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.lakala.dynamicreport.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * 数据集关联组
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity	       
@Table(name = "dm_data_list_rel_param_group")
@ApiModel(value = "数据集关联组",
        description = "数据集关联组(DataListRelParamterGroup),Table(dm_data_list_rel_param_group)",
        parent = BaseEntity.class)
public class DataListRelParamterGroup extends BaseEntity {

    public DataListRelParamterGroup() {
    }

    public DataListRelParamterGroup(Long dataList, Long dataParameterGroup) {
        this.dataList = dataList;
        this.dataParameterGroup = dataParameterGroup;
    }

    private static final long serialVersionUID = 1317118270171584666L;

    @ApiModelProperty(value = "数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "集合ID",name="dataList")
    private Long dataList;

    @ApiModelProperty(value = "组ID",name="dataParameterGroup")
    private Long dataParameterGroup;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "dlrpg_id")
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "dlrpg_data_list")
    public Long getDataList() {
        return dataList;
    }

    public void setDataList(Long dataList) {
        this.dataList = dataList;
    }

    @Column(name = "dlrpg_data_parameter_group")
    public Long getDataParameterGroup() {
        return dataParameterGroup;
    }

    public void setDataParameterGroup(Long dataParameterGroup) {
        this.dataParameterGroup = dataParameterGroup;
    }

}
