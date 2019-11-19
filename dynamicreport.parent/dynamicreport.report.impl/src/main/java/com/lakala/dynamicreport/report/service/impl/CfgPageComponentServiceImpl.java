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
package com.lakala.dynamicreport.report.service.impl;

import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.report.model.CfgComponent;
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.model.CfgPageComponent;
import com.lakala.dynamicreport.report.query.CfgPageComponentQuery;
import com.lakala.dynamicreport.report.repository.ICfgComponentRepository;
import com.lakala.dynamicreport.report.repository.ICfgPageComponentRepository;
import com.lakala.dynamicreport.report.repository.ICfgPageRepository;
import com.lakala.dynamicreport.report.service.ICfgPageComponentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 报表页面组件接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgPageComponentService")
public class CfgPageComponentServiceImpl extends BaseServiceImpl<ICfgPageComponentRepository,CfgPageComponent,String> implements ICfgPageComponentService {

	@Autowired
	private ICfgPageComponentRepository cfgPageComponentRepository;
	
	@Autowired
	private ICfgPageRepository cfgPageRepository;
	
	@Autowired
	private ICfgComponentRepository cfgComponentRepository;



	public List<CfgPageComponent> findCfgPageComponent(CfgPageComponentQuery cfgPageComponentQuery) {
		return cfgPageComponentRepository.findAll(specification(cfgPageComponentQuery),PageForList.getSort("id",cfgPageComponentQuery));
	}

	public Page<CfgPageComponent> findCfgPageComponentCriteria(final CfgPageComponentQuery cfgPageComponentQuery) {
		return cfgPageComponentRepository.findAll(specification(cfgPageComponentQuery), PageForList.getPageable("modifiedDate",cfgPageComponentQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<CfgPageComponent> specification(final CfgPageComponentQuery cfgPageComponentQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != cfgPageComponentQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgPageComponentQuery.getId(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"page.id",cfgPageComponentQuery.getPage(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"component.id",cfgPageComponentQuery.getComponent(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgPageComponentQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"component.name",cfgPageComponentQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.whereAndOr(list);
	}

	@Override
	@Transactional
	public void save(String pageId, String components, String allIds) {
		List<String> varIds = new ArrayList<>();
		if (StringUtils.isNotBlank(allIds)) {
			String[] idArray = allIds.split(",");
			varIds = Arrays.asList(idArray);
		}
		CfgPageComponentQuery cfgPageComponentQuery = new CfgPageComponentQuery();
		cfgPageComponentQuery.setPage(pageId);
		
		List<CfgPageComponent> list = findCfgPageComponent(cfgPageComponentQuery);
		
		for (CfgPageComponent sv : list) {
			if (varIds.contains(sv.getComponent().getId())) {
				cfgPageComponentRepository.delete(sv);
			}
		}
		
		if (StringUtils.isNotBlank(components)) {
			List<CfgPageComponent> mrList = new ArrayList<>();
			CfgPage modelObj = cfgPageRepository.getOne(pageId);
			String[] ids = components.split(",");
			for (String id : ids) {
				CfgPageComponent mr = new CfgPageComponent();
				mr.setPage(modelObj);
				CfgComponent ruleObj = cfgComponentRepository.getOne(id);
				mr.setComponent(ruleObj);
				mrList.add(mr);				
			}
			cfgPageComponentRepository.saveAll(mrList);
		}
	}

}