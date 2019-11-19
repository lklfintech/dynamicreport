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
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import com.lakala.dynamicreport.report.repository.ICfgComponentColRepository;
import com.lakala.dynamicreport.report.service.ICfgComponentColService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 报表组件列接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgComponentColService")
public class CfgComponentColServiceImpl extends BaseServiceImpl<ICfgComponentColRepository,CfgComponentCol, String> implements ICfgComponentColService {

    @Autowired
    ICfgComponentColRepository cfgComponentColRepository;


    @Override
    public List<CfgComponentCol> findCfgComponentCol(CfgComponentColQuery cfgComponentColQuery) {
        return cfgComponentColRepository.findAll(specification(cfgComponentColQuery), PageForList.getSort("id",cfgComponentColQuery));
    }

    public Page<CfgComponentCol> findCfgComponentColCriteria(final CfgComponentColQuery cfgComponentColQuery) {
        return cfgComponentColRepository.findAll(specification(cfgComponentColQuery), PageForList.getPageable("modifiedDate",cfgComponentColQuery));
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CfgComponentCol> specification(final CfgComponentColQuery cfgComponentColQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cfgComponentColQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentColQuery.getId(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"group.id",cfgComponentColQuery.getGroup(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"component.id",cfgComponentColQuery.getComponent(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"type",cfgComponentColQuery.getType(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentColQuery.getName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"colName",cfgComponentColQuery.getColName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",cfgComponentColQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",cfgComponentColQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgComponentColQuery.getVersionNo(),Integer.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentColQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentColQuery.getSearchText(),String.class,OperationEnum.OR));
        }
        return SpecificationUtil.whereAndOr(list);
    }

    @Override
    public List<CfgComponentCol> findByIds(List<String> ids) {
        return cfgComponentColRepository.findByIds(ids);
    }

	@Override
	@Transactional
	public void deleteByComponentId(String id) {
        cfgComponentColRepository.deleteByComponentId(id);
	}

	@Override
    @Transactional
	public void save(List<CfgComponentCol> cols) {
		cfgComponentColRepository.saveAll(cols);
	}
}