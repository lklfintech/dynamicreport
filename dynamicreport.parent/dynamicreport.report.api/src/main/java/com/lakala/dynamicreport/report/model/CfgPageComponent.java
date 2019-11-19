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

import com.lakala.dynamicreport.common.model.BaseEntity;
import com.xzixi.swagger2.plus.annotation.IgnoreSwagger2Parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <p>
 * 报表页面组件实体类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Entity
@Table(name = "rpt_cfg_page_component")
@ApiModel(value = "报表页面组件实体类",description = "报表页面组件实体类(CfgPageComponent),Table(rpt_cfg_page_component)",
		parent =BaseEntity.class)
public class CfgPageComponent extends BaseEntity{

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "数据主键",name="id")
	private String id;

	@ApiModelProperty(value = "序列",name="sequence")
	private Integer sequence;

	@ApiModelProperty(value = "报表页面实体类",name="page")
	@IgnoreSwagger2Parameter
	private CfgPage page;

	public CfgPageComponent(){
	}

	public CfgPageComponent(
		String id
	){
		this.id = id;
	}

	

	public void setId(String value) {
		this.id = value;
	}
	
	@Id 	
	@Column(name = "pc_id")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
	public String getId() {
		return this.id;
	}
	
	@Column(name = "pc_sequence")
	public Integer getSequence() {
		return this.sequence;
	}
	public void setSequence(Integer value) {
		this.sequence = value;
	}
	

	public void setPage(CfgPage page){
		this.page = page;
	}
	
	@ManyToOne
	@JoinColumn(name = "pc_page")
	public CfgPage getPage() {
		return page;
	}
	private CfgComponent component;
	
	public void setComponent(CfgComponent component){
		this.component = component;
	}
	
	@ManyToOne
	@JoinColumn(name = "pc_component")
	public CfgComponent getComponent() {
		return component;
	}

}

