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
package com.lakala.dynamicreport.system.service;

import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.query.SytemFunctionQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 系统功能服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ISytemFunctionService extends IBaseService<SystemFunction, String>{

	/**
	 * 参数带条件分页查询
	 * 
	 * @param functionQuery
	 * @return
	 */
	Page<SystemFunction> findParameterCriteria(SytemFunctionQuery functionQuery);

	/**
	 * 参数带条件查询 
	 * 
	 * @param functionQuery
	 * @return
	 */
	List<SystemFunction> findParameter(SytemFunctionQuery  functionQuery);
	
	/**
	 * 参数带条件查询
	 * 
	 * @param functionQuery
	 * @return
	 */
	List<SystemFunction> findParent(SytemFunctionQuery  functionQuery);

	/**
	 *获取属性功能节点
	 *
	 * @param functionList
	 * @param functionIds
	 * @return
	 */
	List<TreeNode> getTree(List<SystemFunction> functionList, List<String> functionIds);
}
