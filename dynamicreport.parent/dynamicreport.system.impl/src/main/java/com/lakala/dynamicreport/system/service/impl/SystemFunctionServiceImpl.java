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
import com.google.common.collect.Maps;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.query.SytemFunctionQuery;
import com.lakala.dynamicreport.system.repository.IFunctionRepository;
import com.lakala.dynamicreport.system.repository.IRoleFunctionRepository;
import com.lakala.dynamicreport.system.service.ISytemFunctionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统功能服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "sysFunctionService")
public class SystemFunctionServiceImpl extends BaseServiceImpl<IFunctionRepository,SystemFunction, String> implements ISytemFunctionService {

	@Autowired
	IFunctionRepository functionRepository;

	@Autowired
	IRoleFunctionRepository roleFunctionRepository;

	@Override
	public List<SystemFunction> findParameter(SytemFunctionQuery functionQuery) {
		return functionRepository.findAll(specification(functionQuery));
	}

	@Override
	public Page<SystemFunction> findParameterCriteria(final SytemFunctionQuery functionQuery) {
		return functionRepository.findAll(specification(functionQuery), PageForList.getPageable("id",functionQuery));
	}

	@Override
	public List<TreeNode> getTree(List<SystemFunction> functionList, List<String> functionIds) {
		List<TreeNode> trees = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(functionList)) {
			for (SystemFunction function : functionList) {
				TreeNode node = new TreeNode();
				node.setText(function.getName());
				node.setId(function.getId());
				if(CollectionUtils.isNotEmpty(functionIds)&&functionIds.contains(function.getId())){
					Map<String, Object> map = Maps.newHashMap();
					map.put("checked", Boolean.TRUE);
					node.setState(map);
				}
				if (CollectionUtils.isNotEmpty(function.getChildrens())) {
					node.setNodes(getTree(function.getChildrens(),functionIds));
				}
				trees.add(node);
			}
		}
		return trees;
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<SystemFunction> specification(final SytemFunctionQuery functionQuery) {
		List<Predication> list  = Lists.newArrayList();
		if (null != functionQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"name",functionQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.or(list);
	}

	@Override
	public List<SystemFunction> findParent(SytemFunctionQuery functionQuery) {
		return functionRepository.findTopFunctions();
	}

}
