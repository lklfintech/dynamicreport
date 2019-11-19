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
package com.lakala.dynamicreport.datamodel.service.impl;

import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.datamodel.model.ParameterGroupRelParameter;
import com.lakala.dynamicreport.datamodel.query.ParameterGroupRelParameterQuery;
import com.lakala.dynamicreport.datamodel.repository.IParameterGroupRelParameterRepository;
import com.lakala.dynamicreport.datamodel.service.IParameterGroupRelParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * <p>
 * 组关联参数接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class ParameterGroupRelParameterServiceImpl extends BaseServiceImpl<IParameterGroupRelParameterRepository,ParameterGroupRelParameter, Long> implements IParameterGroupRelParameterService{

	@Autowired
	IParameterGroupRelParameterRepository groupParameterRepository;

	@Override
	public List<ParameterGroupRelParameter> findGroupParametes(ParameterGroupRelParameterQuery parameterGroupRelParameterQuery) {
		return groupParameterRepository.findAll(specification(parameterGroupRelParameterQuery));
	}
	/**
	 * 拼凑查询条件
	 */
	private Specification<ParameterGroupRelParameter> specification(final ParameterGroupRelParameterQuery parameterGroupRelParameterQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != parameterGroupRelParameterQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"id", parameterGroupRelParameterQuery.getId(),Long.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"dataParameter", parameterGroupRelParameterQuery.getDataParameter(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"dataParameterGroup", parameterGroupRelParameterQuery.getDataParameterGroup(),String.class,OperationEnum.AND));
		}
		return SpecificationUtil.where(list);
	}
	@Override
	@Transactional
	public void deleteByGroupId(Long groupId) {
		groupParameterRepository.deleteByGroupId(groupId);
	}
	
}
