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
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.RoleQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 角色服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRoleService extends IBaseService<Role, Long>{

	/**
	 * 角色带条件分页查询
	 * 
	 * @param roleQuery
	 * @return
	 */
	Page<Role> findRoleCriteria(RoleQuery roleQuery);

	/**
	 * 角色带条件查询
	 * 
	 * @param roleQuery
	 * @return
	 */
	List<Role> findRole(RoleQuery roleQuery);

	/**
	 * 获取角色下的用户
	 *
	 * @param id
	 * @param searchText
	 * @return
	 */
	List<User> getUsers(String id, String searchText);
}
