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
import com.lakala.dynamicreport.datamodel.model.DataParameter;
import com.lakala.dynamicreport.datamodel.query.DataParameterQuery;
import com.lakala.dynamicreport.datamodel.repository.IDataParameterRepository;
import com.lakala.dynamicreport.datamodel.service.IDataParameterService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 操作数据参数接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class DataParameterServiceImpl extends BaseServiceImpl<IDataParameterRepository,DataParameter, Long>  implements IDataParameterService {

	@Autowired
	IDataParameterRepository dataParameterRepository;
	 
	@Override
	public Page<DataParameter> findDataParameterCriteria(DataParameterQuery dataParameterQuery) {
		return dataParameterRepository.findAll(specification(dataParameterQuery), PageForList.getPageable("modifiedDate",dataParameterQuery));
	}

	@Override
	public List<DataParameter> findDataParameter(DataParameterQuery dataParameterQuery) {
		return dataParameterRepository.findAll(specification(dataParameterQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<DataParameter> specification(final DataParameterQuery dataParameterQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != dataParameterQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"id",dataParameterQuery.getId(),Long.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"identifier",dataParameterQuery.getIdentifier(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"status",dataParameterQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.IN,"id",dataParameterQuery.getIds(),List.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"identifier",dataParameterQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"paramName",dataParameterQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"name",dataParameterQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"paramContent",dataParameterQuery.getSearchText(),String.class,OperationEnum.OR));

		}
		return SpecificationUtil.whereAndOr(list);
	}

	@Override
	public List<DataParameter> findByIdentifiers(List<String> identifiers) {
		
		if(CollectionUtils.isEmpty(identifiers))
			return new ArrayList<>();
		
		return dataParameterRepository.findByIdentifiers(identifiers);
	}

 
	@Override
	@Cacheable(value = "dataListCache", key = "'dataParameter_'+#identifier")
	public DataParameter findByIdentifierCache(String identifier) {
		return  dataParameterRepository.findByIdentify(identifier);
	}
	
	@Override
	public DataParameter findByIdentifier(String identifier) {
		return  dataParameterRepository.findByIdentify(identifier);
	}
	
	
	@Override
	@CacheEvict(value = "dataListCache", key = "'dataParameter_'+#identifier")
	public void initByIdentifierCache(String identifier) {
		//删除缓存
	}
}
