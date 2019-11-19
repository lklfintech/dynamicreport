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
package com.lakala.dynamicreport.report.service;

import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.report.model.CfgComponentType;
import com.lakala.dynamicreport.report.query.CfgComponentTypeQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 报表组件类型接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgComponentTypeService extends IBaseService<CfgComponentType, String>{
	/**
	 * 带条件分页查询
	 *
	 * @param cfgComponentTypeQuery
	 * @return
	 */
	Page<CfgComponentType> findCfgComponentTypeCriteria(CfgComponentTypeQuery cfgComponentTypeQuery);
	
	/**
	 * 带条件查询
	 *
	 * @param cfgComponentTypeQuery
	 * @return
	 */
	List<CfgComponentType> findCfgComponentType(CfgComponentTypeQuery cfgComponentTypeQuery);
	
	/**
	 * 查询顶层组件类型
	 *
	 * @return
	 */
	List<CfgComponentType> findTopComponentTypes();

	/**
	 * 查询类型节点
	 *
	 * @param componentTypeList
	 * @param ids
	 * @return
	 */
	List<TreeNode> getTree(List<CfgComponentType> componentTypeList, List<String> ids);
}