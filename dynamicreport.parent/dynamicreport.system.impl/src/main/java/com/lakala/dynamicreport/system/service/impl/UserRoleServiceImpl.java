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

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.model.UserRole;
import com.lakala.dynamicreport.system.query.UserRoleQuery;
import com.lakala.dynamicreport.system.repository.IRoleRepository;
import com.lakala.dynamicreport.system.repository.ISystemUserRepository;
import com.lakala.dynamicreport.system.repository.IUserRoleRepository;
import com.lakala.dynamicreport.system.service.IUserRoleService;

/**
 * <p>
 * 用户角色服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<IUserRoleRepository,UserRole, Long> implements IUserRoleService {

	@Autowired
	IUserRoleRepository userRoleRepository;

	@Autowired
	ISystemUserRepository userRepository;

	@Autowired
	IRoleRepository roleRepository;

	@Override
	public List<UserRole> findUserRole(UserRoleQuery userRoleQuery) {
		return userRoleRepository.findAll(specification(userRoleQuery));
	}

	@Override
	@Transactional
	public void save(String id, String ids, String allIds) {
		List<String> varIds = Lists.newArrayList();
		UserRoleQuery userRoleQuery = new UserRoleQuery();
		userRoleQuery.setUser(id);
		List<UserRole> list = findUserRole(userRoleQuery);
		if (StringUtils.isNotBlank(allIds)) {
			String[] idArray = allIds.split(",");
			varIds = Arrays.asList(idArray);
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (UserRole userRoleObj : list) {
				if (varIds.contains(userRoleObj.getRole().getId().toString())) {
					userRoleRepository.delete(userRoleObj);
				}
			}
		}
		if (StringUtils.isNotBlank(ids)) {
			List<UserRole> mrList = Lists.newArrayList();
			User user = userRepository.getOne(id);
			String[] idStr = ids.split(",");
			for (String idIdx : idStr) {
				UserRole mr = new UserRole();
				mr.setUser(user);
				Role roleObj = roleRepository.getOne(Long.valueOf(idIdx));
				mr.setRole(roleObj);
				mrList.add(mr);
			}
			userRoleRepository.saveAll(mrList);
		}
	}

	@Override
	@Transactional
	public void saveUser(String roleId, String userIds, String userAllIds) {
		List<String> varIds =  Lists.newArrayList();
		UserRoleQuery userRoleQuery = new UserRoleQuery();
		userRoleQuery.setRole(Long.valueOf(roleId));
		List<UserRole> list = findUserRole(userRoleQuery);
		if (StringUtils.isNotBlank(userAllIds)) {
			String[] idArray = userAllIds.split(",");
			varIds = Arrays.asList(idArray);
		}
		if (CollectionUtils.isNotEmpty(list)) {
			for (UserRole userRoleObj : list) {
				if (varIds.contains(userRoleObj.getUser().getId())) {
					userRoleRepository.delete(userRoleObj);
				}
			}
		}
		if (StringUtils.isNotBlank(userIds)) {
			List<UserRole> mrList = Lists.newArrayList();
			Role role = roleRepository.getOne(Long.valueOf(roleId));

			String[] idStr = userIds.split(",");
			for (String idIdx : idStr) {
				UserRole mr = new UserRole();
				mr.setRole(role);
				User userObj = userRepository.getOne(idIdx);
				mr.setUser(userObj);
				mrList.add(mr);
			}
			userRoleRepository.saveAll(mrList);
		}
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<UserRole> specification(final UserRoleQuery userRoleQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != userRoleQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"user.id",userRoleQuery.getUser(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"role.id",userRoleQuery.getRole(),String.class,OperationEnum.AND));
		}
		return SpecificationUtil.where(list);
	}

}
