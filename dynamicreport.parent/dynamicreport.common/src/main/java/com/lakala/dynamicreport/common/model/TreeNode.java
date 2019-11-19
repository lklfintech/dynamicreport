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
package com.lakala.dynamicreport.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单实体对象,用于树形菜单
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "树形菜单",description = "树形菜单(TreeNode)")
public class TreeNode implements Serializable {

	private static final long serialVersionUID = 6429142091973995017L;

	//bootstrap-treeview
	@ApiModelProperty(value = "菜单中的ID",name="id")
	private String id;
	//bootstrap-treeview
	@ApiModelProperty(value = "菜单中的名称",name="text")
	private String text;

	@ApiModelProperty(value = "菜单key",name="key")
	private String key;

	@ApiModelProperty(value = "是否选中",name="checked")
	private Boolean checked;

	@ApiModelProperty(value = "子菜单",name="nodes")
	private List<TreeNode> nodes = new ArrayList<TreeNode>();

	@ApiModelProperty(value = "菜单URL",name="url")
	private String url;

	@ApiModelProperty(value = "菜单的图标",name="icon")
	private String icon;

	@ApiModelProperty(value = "菜单状态",name="state")
	private Map<String, Object> state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<TreeNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeNode> nodes) {
		this.nodes = nodes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
