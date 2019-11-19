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
 * 报表参数实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_param")
@NamedEntityGraph(name = "CfgParam.Graph", attributeNodes = {@NamedAttributeNode("type")})
@ApiModel(value = "报表参数实体类",description = "报表参数实体类(CfgParam),Table(rpt_cfg_param)",
        parent = BaseEntity.class)
public class CfgParam extends BaseEntity implements Comparable<CfgParam> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "数据主键",name="id")
    private String id;

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    @ApiModelProperty(value = "参数标识",name="paramName")
    private String paramName;

    @ApiModelProperty(value = "标签",name="label")
    private String label;

    @ApiModelProperty(value = "长度",name="len")
    private String len;

    @ApiModelProperty(value = "格式",name="format")
    private String format;

    @ApiModelProperty(value = "是否必输",name="required")
    private String required;

    @ApiModelProperty(value = "数据集实体",name="dataList")
    @IgnoreSwagger2Parameter
    private DataList dataList;

    @ApiModelProperty(value = "参数值",name="paramValue")
    private String paramValue;

    @ApiModelProperty(value = "报表组件类型实体类",name="type")
    @IgnoreSwagger2Parameter
    private CfgComponentType type;

    @ApiModelProperty(value = "是否选中",name="selected")
    private boolean selected;

    @ApiModelProperty(value = "序列",name="sequence")
    private Integer sequence;
    
    public CfgParam() {
    }

    public CfgParam(String id) {
        this.id = id;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Id
    @Column(name = "cp_id")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return this.id;
    }

    @Column(name = "cp_name")
    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Column(name = "cp_param_name")
    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Column(name = "cp_label")
    public String getLabel() {
        return this.label;
    }

    public void setLabel(String value) {
        this.label = value;
    }

    @Column(name = "cp_len")
    public String getLen() {
        return this.len;
    }

    public void setLen(String value) {
        this.len = value;
    }

    @Column(name = "cp_format")
    public String getFormat() {
        return this.format;
    }

    public void setFormat(String value) {
        this.format = value;
    }

    @Column(name = "cp_required")
    public String getRequired() {
        return this.required;
    }

    public void setRequired(String value) {
        this.required = value;
    }

    @ManyToOne
    @JoinColumn(name = "cp_data_list_id")
    public DataList getDataList() {
        return this.dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    @Column(name = "cp_status")
    public String getStatus() {
        return this.status;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cp_created_date")
    public java.sql.Timestamp getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "cp_created_user")
    public String getCreatedUser() {
        return this.createdUser;
    }

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "cp_modified_date")
    public java.sql.Timestamp getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "cp_modified_user")
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    @Version
    @Column(name = "cp_version_no")
    public Integer getVersionNo() {
        return this.versionNo;
    }


    /**
     * private List<CfgPageParam> cfgPageParams = new ArrayList<CfgPageParam>();
     * public void setCfgPageParams(List<CfgPageParam> cfgPageParam){
     * this.cfgPageParams = cfgPageParam;
     * }
     *
     * @JsonIgnore
     * @OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "cfgParam")
     * public List<CfgPageParam> getCfgPageParams() {
     * return cfgPageParams;
     * }
     **/

    public void setType(CfgComponentType type) {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "cp_type")
    public CfgComponentType getType() {
        return type;
    }

    @Transient
    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    @Transient
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

    @Transient
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public int compareTo(CfgParam cfgParam) {
		Integer seq2=cfgParam.getSequence();
		Integer seq1=sequence;
		if(seq1==null){
			seq1=9999999;
		}
		if(seq2==null){
			seq2=10000000;
		}
		return seq1-seq2;
	}
}

