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
package com.lakala.dynamicreport.system.service.impl;

import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.RoleQuery;
import com.lakala.dynamicreport.system.query.SystemUserQuery;
import com.lakala.dynamicreport.system.repository.IRoleRepository;
import com.lakala.dynamicreport.system.service.IRoleService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "roleService")
public class RoleServiceImpl extends BaseServiceImpl<IRoleRepository,Role, Long> implements IRoleService {

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	ISystemUserService systemUserService;

	@Override
	public List<Role> findRole(RoleQuery roleQuery) {
		return roleRepository.findAll(specification(roleQuery));
	}

	@Override
	public Page<Role> findRoleCriteria(final RoleQuery roleQuery) {
		return roleRepository.findAll(specification(roleQuery), PageForList.getPageable("id",roleQuery));
	}

	@Override
	public List<User> getUsers(String id, String searchText) {
		Role role = findOne(Long.valueOf(id));
		List<User> users = role.getUsers();
		List<String> userIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(users)){
			for (User user:users){
				userIds.add(user.getId());
			}
		}
		SystemUserQuery query = new SystemUserQuery();
		query.setSearchText(searchText);
		query.setStatus(StatusConstants.ACTIVE);
		List<User> list = systemUserService.findUser(query);
		if (CollectionUtils.isNotEmpty(list)) {
			for (User user : list) {
				if (userIds.contains(user.getId())) {
					user.setSelected(Boolean.TRUE);
				} else {
					user.setSelected(Boolean.FALSE);
				}
			}
		}
		return list;
	}

	/**
	 * 拼凑查询条件
	 *
	 * @param roleQuery
	 * @return
	 */
	private Specification<Role> specification(final RoleQuery roleQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != roleQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"status",roleQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",roleQuery.getName(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",roleQuery.getSearchText(),String.class,OperationEnum.AND));
		}
		return SpecificationUtil.where(list);
	}

}
