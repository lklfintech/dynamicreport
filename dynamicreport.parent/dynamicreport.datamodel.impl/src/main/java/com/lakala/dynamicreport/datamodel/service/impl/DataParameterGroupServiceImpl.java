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
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.datamodel.model.DataParameterGroup;
import com.lakala.dynamicreport.datamodel.query.DataParameterGroupQuery;
import com.lakala.dynamicreport.datamodel.repository.IDataListRelParamterGroupRepository;
import com.lakala.dynamicreport.datamodel.repository.IDataParameterGroupRepository;
import com.lakala.dynamicreport.datamodel.repository.IParameterGroupRelParameterRepository;
import com.lakala.dynamicreport.datamodel.service.IDataParameterGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 操作数据参数组接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class DataParameterGroupServiceImpl extends BaseServiceImpl<IDataParameterGroupRepository,DataParameterGroup, Long> implements IDataParameterGroupService {

	@Autowired
	IDataParameterGroupRepository dataParameterGroupRepository;
	@Autowired
	IParameterGroupRelParameterRepository groupParameterRepository;
	@Autowired
	IDataListRelParamterGroupRepository dataListRelParamterGroupRepository;
	
	@Override
	public Page<DataParameterGroup> findDataParameterGroupCriteria(DataParameterGroupQuery dataParameterGroupQuery) {
		return dataParameterGroupRepository.findAll(specification(dataParameterGroupQuery), PageForList.getPageable("modifiedDate",dataParameterGroupQuery));
	}

	@Override
	public List<DataParameterGroup> findDataParameterGroup(DataParameterGroupQuery dataParameterGroupQuery) {
		return dataParameterGroupRepository.findAll(specification(dataParameterGroupQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<DataParameterGroup> specification(final DataParameterGroupQuery dataParameterGroupQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != dataParameterGroupQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"id",dataParameterGroupQuery.getId(),Long.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"status",dataParameterGroupQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"identifier",dataParameterGroupQuery.getIdentifier(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"identifier",dataParameterGroupQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"name",dataParameterGroupQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"content",dataParameterGroupQuery.getSearchText(),String.class,OperationEnum.OR));

		}
		return SpecificationUtil.whereAndOr(list);
	}
 
	@Override
	@Cacheable(value = "dataListCache", key = "'dataList_'+#identifier")
	public DataParameterGroup findByIdentifierCache(String identifier) {
		return  dataParameterGroupRepository.findByIdentify(identifier);
	}

	@Override
	public DataParameterGroup findByIdentifier(String identifier) {
		return  dataParameterGroupRepository.findByIdentify(identifier);
	}
	
	@Override
	@CacheEvict(value = "dataListCache", key = "'dataList_'+#identifier")
	public void initByIdentifierCache(String identifier) {
		//删除缓存
	}
}
