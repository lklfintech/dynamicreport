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

import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.SystemUserQuery;
import com.lakala.dynamicreport.system.query.UserQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 用户服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ISystemUserService extends IBaseService<User, String>{

	/**
	 * 参数带条件分页查询
	 * 
	 * @param systemUserQuery
	 * @return
	 */
	Page<User> findUserCriteria(SystemUserQuery systemUserQuery);

	/**
	 * 参数带条件查询
	 * 
	 * @param systemUserQuery
	 * @return
	 */
	List<User> findUser(SystemUserQuery systemUserQuery);

	/**
	 * 通过当前用户查找有相同角色的用户
	 * 
	 * @param currentUser
	 * @return
	 */
	List<User> findRoleUser(String currentUser);

	/**
	 * 查找当前用户下同角色的所有用户
	 * 
	 * @return
	 */
	List<String> getAllSameRoleUsers();

	/**
	 * 查找当前用户下同报表角色的所有用户
	 *
	 * @return
	 */
	List<String> getAllSameReportRoleUsers();

	/**
	 * 获取登录用户
	 * 
	 * @return
	 */
	String getLoginUser();

	/**
	 * 用户登录
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	String auth(String username, String password);

	/**
	 * 用户修改密码
	 *
	 * @param user
	 * @return
	 */
	ResultJson modifyPwd( UserQuery user);

	/**
	 *用户更新
	 *
	 * @param systemUser
	 * @param pkId
	 */
	void updateByParams(User systemUser, String pkId);

	/**
	 * 获取用户角色
	 *
	 * @param id
	 * @param searchText
	 * @return
	 */
	List<Role> getRules(String id, String searchText);
}
