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
import com.lakala.dynamicreport.system.model.UserRole;
import com.lakala.dynamicreport.system.query.UserRoleQuery;

import java.util.List;

/**
 * <p>
 * 用户角色服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IUserRoleService extends IBaseService<UserRole, Long>{

	/**
	 *  查询用户角色
	 * 
	 * @param userRoleQuery
	 * @return
	 */
	List<UserRole> findUserRole(UserRoleQuery userRoleQuery);

	/**
	 * 保存
	 *
	 * @param id
	 * @param ids
	 * @param allIds
	 */
	void save(String id, String ids,String allIds);

	/**
	 * 保存角色用户
	 *
	 * @param roleId
	 * @param userIds
	 * @param userAllIds
	 */
	void saveUser(String roleId, String userIds,String userAllIds);
}
