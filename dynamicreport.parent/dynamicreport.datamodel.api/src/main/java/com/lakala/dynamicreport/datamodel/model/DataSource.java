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
 * 数据源实体
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "dm_data_source")
@ApiModel(value = "数据源实体",description = "数据源实体(DataSource),Table(dm_data_source)",
        parent = BaseEntity.class)
public class DataSource extends BaseEntity {

    private static final long serialVersionUID = 1317118270171584666L;


    @ApiModelProperty(value = "数据主键",name="id")
    private Long id;

    @ApiModelProperty(value = "唯一标识",name="identifier")
    private String identifier;

    @ApiModelProperty(value = "数据源名称",name="name")
    private String name;

    @ApiModelProperty(value = "数据源URL",name="url")
    private String url;

    @ApiModelProperty(value = "数据源驱动",name="driverClass")
    private String driverClass;

    @ApiModelProperty(value = "链接用户名",name="username")
    private String username;

    @ApiModelProperty(value = "链接密码",name="password")
    private String password;

    @ApiModelProperty(value = "描述",name="description")
    private String description;

    @ApiModelProperty(value = "是否有kerberos认证",name="hiveKerberos")
    private String hiveKerberos;

    @ApiModelProperty(value = "krb5.conf配置文件地址",name="hiveKrb5")
    private String hiveKrb5;

    @ApiModelProperty(value = "Keytab配置文件地址",name="hiveKeytab")
    private String hiveKeytab;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.lakala.dynamicreport.common.utils.SnowflakeId")
    @Column(name = "ds_id")
    public Long getId() {
        return id;
    }

    @JsonSerialize(using = ToStringSerializer.class)
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ds_identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Column(name = "ds_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ds_url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "ds_driver_class")
    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    @Column(name = "ds_username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "ds_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "ds_description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ds_hive_kerberos")
    public String getHiveKerberos() {
        return hiveKerberos;
    }

    public void setHiveKerberos(String hiveKerberos) {
        this.hiveKerberos = hiveKerberos;
    }

    @Column(name = "ds_hive_krb5")
    public String getHiveKrb5() {
        return hiveKrb5;
    }

    public void setHiveKrb5(String hiveKrb5) {
        this.hiveKrb5 = hiveKrb5;
    }

    @Column(name = "ds_hive_keytab")
    public String getHiveKeytab() {
        return hiveKeytab;
    }

    public void setHiveKeytab(String hiveKeytab) {
        this.hiveKeytab = hiveKeytab;
    }

    @Column(name = "ds_status")
    public String getStatus() {
        return status;
    }

    @Column(name = "ds_created_user")
    public String getCreatedUser() {
        return createdUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ds_created_date")
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    @Column(name = "ds_modified_user")
    public String getModifiedUser() {
        return modifiedUser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ds_modified_date")
    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    @Version
    @Column(name = "ds_version_no")
    public int getVersionNo() {
        return versionNo;
    }

}
