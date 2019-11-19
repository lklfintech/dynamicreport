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
import com.lakala.dynamicreport.report.model.CfgChartTemplate;
import com.lakala.dynamicreport.report.query.CfgChartTemplateQuery;
import com.lakala.dynamicreport.report.repository.ICfgChartTemplateRepository;
import com.lakala.dynamicreport.report.service.ICfgChartTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报表图形模板接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgChartTemplateService")
public class CfgChartTemplateServiceImpl extends BaseServiceImpl<ICfgChartTemplateRepository,CfgChartTemplate, String> implements ICfgChartTemplateService {

	@Autowired
	ICfgChartTemplateRepository cfgChartTemplateRepository;

	public List<CfgChartTemplate> findCfgChartTemplate(CfgChartTemplateQuery cfgChartTemplateQuery) {
		return cfgChartTemplateRepository.findAll(specification(cfgChartTemplateQuery),PageForList.getSort("id",cfgChartTemplateQuery));
	}

	public Page<CfgChartTemplate> findCfgChartTemplateCriteria(final CfgChartTemplateQuery cfgChartTemplateQuery) {
		return cfgChartTemplateRepository.findAll(specification(cfgChartTemplateQuery), PageForList.getPageable("modifiedDate",cfgChartTemplateQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<CfgChartTemplate> specification(final CfgChartTemplateQuery cfgChartTemplateQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != cfgChartTemplateQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgChartTemplateQuery.getId(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",cfgChartTemplateQuery.getName(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"content",cfgChartTemplateQuery.getContent(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"status",cfgChartTemplateQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.IN,"createdUser",cfgChartTemplateQuery.getUsers(),List.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgChartTemplateQuery.getVersionNo(),Integer.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgChartTemplateQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"name",cfgChartTemplateQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.whereAndOr(list);
	}

}