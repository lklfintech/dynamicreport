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
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.RoleFunction;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.query.RoleFunctionQuery;
import com.lakala.dynamicreport.system.repository.IFunctionRepository;
import com.lakala.dynamicreport.system.repository.IRoleFunctionRepository;
import com.lakala.dynamicreport.system.repository.IRoleRepository;
import com.lakala.dynamicreport.system.repository.ISystemUserRepository;
import com.lakala.dynamicreport.system.service.IRoleFunctionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 角色与功能关联服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "roleFunctionService")
public class RoleFunctionServiceImpl extends BaseServiceImpl<IRoleFunctionRepository,RoleFunction, Long> implements IRoleFunctionService {

	@Autowired
    IRoleFunctionRepository roleFunctionRepository;

	@Autowired
    ISystemUserRepository userRepository;

	@Autowired
    IRoleRepository roleRepository;

	@Autowired
    IFunctionRepository functionRepository;



	@Override
	public List<RoleFunction> findRoleFunction(RoleFunctionQuery roleFunctionQuery) {
		return roleFunctionRepository.findAll(specification(roleFunctionQuery));
	}

	@Transactional
	@Override
	public void save(Long roleId, String functionIds) {
		RoleFunctionQuery roleFunctionQuery = new RoleFunctionQuery();
		roleFunctionQuery.setRole(roleId);
		List<RoleFunction> list = findRoleFunction(roleFunctionQuery);
		if (CollectionUtils.isNotEmpty(list)) {
			roleFunctionRepository.deleteInBatch(list);
		}
		if (StringUtils.isNotBlank(functionIds)) {
			List<RoleFunction> mrList = Lists.newArrayList();
			Role role = roleRepository.getOne(roleId);
			String[] ids = functionIds.split(",");
			for (String id : ids) {
				RoleFunction mr = new RoleFunction();
				mr.setRole(role);
				SystemFunction systemFunction = functionRepository.getOne(id);
				mr.setSystemFunction(systemFunction);
				mrList.add(mr);
			}
			roleFunctionRepository.saveAll(mrList);
		}
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<RoleFunction> specification(final RoleFunctionQuery roleFunctionQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != roleFunctionQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"role.id",roleFunctionQuery.getRole(),Long.class,OperationEnum.AND));
		}
		return SpecificationUtil.where(list);
	}

	@Override
	@Transactional
	public void deleteByFunction(String pageId) {
		roleFunctionRepository.deleteByFunction(pageId);
	}

	@Override
	@Transactional
	public void batchSave(List<RoleFunction> roleFunctionList) {
		roleFunctionRepository.saveAll(roleFunctionList);
	}

	@Override
	public List<BigInteger> findRoleByFunctionId(String functionId) {
		return roleFunctionRepository.findRoleByFunctionId(functionId);
	}
}
