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

import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.system.model.RoleFunction;
import com.lakala.dynamicreport.system.query.RoleFunctionQuery;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 角色与功能服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRoleFunctionService extends IBaseService<RoleFunction, Long>{

	/**
	 * 查询角色功能
	 * 
	 * @param roleFunctionQuery
	 * @return
	 */
	List<RoleFunction> findRoleFunction(RoleFunctionQuery roleFunctionQuery);

	/**
	 *  保存
	 * 
	 * @param roleId
	 * @param functionIds
	 */
	void save(Long roleId, String functionIds);

	/**
	 * 根据功能解除关系
	 *
	 * @param pageId
	 */
	void deleteByFunction(String pageId);

	/**
	 * 批量保存
	 *
	 * @param roleFunctionList
	 */
	void batchSave(List<RoleFunction> roleFunctionList);

	/**
	 * 穿功能关联角色
	 *
	 * @param id
	 * @return
	 */
	List<BigInteger> findRoleByFunctionId(String id);
}
