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

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.model.CfgPageParam;
import com.lakala.dynamicreport.report.model.CfgParam;
import com.lakala.dynamicreport.report.query.CfgPageParamQuery;
import com.lakala.dynamicreport.report.repository.ICfgPageParamRepository;
import com.lakala.dynamicreport.report.repository.ICfgPageRepository;
import com.lakala.dynamicreport.report.repository.ICfgParamRepository;
import com.lakala.dynamicreport.report.service.ICfgPageParamService;
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
 * 报表页面参数接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgPageParamService")
public class CfgPageParamServiceImpl extends BaseServiceImpl<ICfgPageParamRepository,CfgPageParam, String> implements ICfgPageParamService {

	@Autowired
	ICfgPageParamRepository cfgPageParamRepository;

	@Autowired
	ICfgPageRepository cfgPageRepository;

	@Autowired
	ICfgParamRepository cfgParamRepository;


	public List<CfgPageParam> findCfgPageParam(CfgPageParamQuery cfgPageParamQuery) {
		return cfgPageParamRepository.findAll(specification(cfgPageParamQuery), PageForList.getSort("id",cfgPageParamQuery));
	}

	public Page<CfgPageParam> findCfgPageParamCriteria(final CfgPageParamQuery cfgPageParamQuery) {
		return cfgPageParamRepository.findAll(specification(cfgPageParamQuery), PageForList.getPageable("modifiedDate",cfgPageParamQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<CfgPageParam> specification(final CfgPageParamQuery cfgPageParamQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != cfgPageParamQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgPageParamQuery.getId(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"page.id",cfgPageParamQuery.getPage(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"param.id",cfgPageParamQuery.getParam(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgPageParamQuery.getSearchText(),String.class,OperationEnum.OR));

		}
		return SpecificationUtil.whereAndOr(list);
	}

	@Override
	@Transactional
	public void save(String pageId, String params,String vos, String allIds) {

		CfgPage modelObj = cfgPageRepository.getOne(pageId);

		List<CfgPageParam> voList=JSONArray.parseArray(vos,CfgPageParam.class);

		List<String> varIds = new ArrayList<>();
		if (StringUtils.isNotBlank(allIds)) {
			String[] idArray = allIds.split(",");
			varIds = Arrays.asList(idArray);
		}

		//以前关联的数据
		List<CfgPageParam> cfgPageParams=findByPageId(pageId);

		for(CfgPageParam cfgPageParam:cfgPageParams){
			if(!varIds.contains(cfgPageParam.getParam().getId())){//以前的关联数据不在本次操作范围内
				CfgPageParam vo=new CfgPageParam();
				vo.setId(cfgPageParam.getParam().getId());
				vo.setParamValue(cfgPageParam.getParamValue());
				voList.add(vo);
			}
		}
		for (CfgPageParam sv : cfgPageParams) {
				cfgPageParamRepository.delete(sv);
		}

		List<CfgPageParam> mrList = new ArrayList<>();
		for(CfgPageParam cfg:voList){

			CfgPageParam mr = new CfgPageParam();
			mr.setPage(modelObj);
			CfgParam ruleObj = cfgParamRepository.getOne(cfg.getId());
			mr.setParam(ruleObj);
			mr.setParamValue(cfg.getParamValue());
			mr.setSequence(cfg.getSequence());
			mrList.add(mr);
			
		}
		cfgPageParamRepository.saveAll(mrList);
	}

	public List<CfgPageParam> findByPageId(String pageId) {
		return cfgPageParamRepository.findByPageId(pageId);
	}
}