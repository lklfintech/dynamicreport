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
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
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
 * 数据集实体
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "dm_data_list")
@ApiModel(value = "数据集实体",description = "数据集实体(DataList),Table(dm_data_list)",
        parent = BaseEntity.class)
public class DataList extends BaseEntity {

    private static final long serialVersionUID = 1317118270171584660L;

    @ApiModelProperty(value="数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "唯一标识",name="identifier")
    private String identifier;

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    @ApiModelProperty(value = "查询SQL",name="querySql")
    private String querySql;

    @ApiModelProperty(value = "数据源",name="dataSource")
    private DataSource dataSource;

    @ApiModelProperty(value = "查询记录数",name="recordCnt")
    private String recordCnt;

    @ApiModelProperty(value = "描述",name="description")
    private String description;

    @ApiModelProperty(value = "模块下关联的数据参数组",name="dataParameterGroup")
    @IgnoreSwagger2Parameter
    private List<DataParameterGroup> dataParameterGroup;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "dl_id")
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "dl_identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


    @Column(name = "dl_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "dl_query_sql")
    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    @ManyToOne
    @JoinColumn(name = "dl_data_source")
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Column(name = "dl_record_cnt")
    public String getRecordCnt() {
        return recordCnt;
    }

    public void setRecordCnt(String recordCnt) {
        this.recordCnt = recordCnt;
    }

    @Column(name = "dl_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "dl_status")
    public String getStatus() {
        return status;
    }

    @Column(name = "dl_created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dl_created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @Column(name = "dl_modified_user")
    public String getModifiedUser() {
        return modifiedUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "dl_modified_date")
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    @Version
    @Column(name = "dl_version_no")
    public int getVersionNo() {
        return versionNo;
    }

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "dm_data_list_rel_param_group", joinColumns = {@JoinColumn(name = "dlrpg_data_list", referencedColumnName = "dl_id")}, inverseJoinColumns = {@JoinColumn(name = "dlrpg_data_parameter_group", referencedColumnName = "dpg_id")})
    public List<DataParameterGroup> getDataParameterGroup() {
        return dataParameterGroup;
    }

    public void setDataParameterGroup(List<DataParameterGroup> dataParameterGroup) {
        this.dataParameterGroup = dataParameterGroup;
    }

}
