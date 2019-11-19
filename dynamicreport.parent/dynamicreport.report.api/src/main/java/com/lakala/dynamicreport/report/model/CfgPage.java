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
 * 报表页面实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_page")
@ApiModel(value = "报表页面实体类",description = "报表页面实体类(CfgPage),Table(rpt_cfg_page)",
        parent = BaseEntity.class)
public class CfgPage extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "标题",name="title")
    private String title;

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    @ApiModelProperty(value = "是否发布",name="published")
    private String published;

    @ApiModelProperty(value = "发布路径",name="publishedPath")
    private String publishedPath;


    public CfgPage() {
    }

    public CfgPage(String id) {
        this.id = id;
    }


    public void setId(String value) {
        this.id = value;
    }

    @Id
    @Column(name = "pg_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    @Column(name = "pg_title")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    @Column(name = "pg_remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "pg_published")
    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    @Column(name = "pg_published_path")
    public String getPublishedPath() {
        return publishedPath;
    }

    public void setPublishedPath(String publishedPath) {
        this.publishedPath = publishedPath;
    }

    @Column(name = "pg_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "pg_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "pg_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "pg_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "pg_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "pg_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }

}

