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
import com.lakala.dynamicreport.report.model.CfgComponentColGroup;
import com.lakala.dynamicreport.report.query.CfgComponentColGroupQuery;
import com.lakala.dynamicreport.report.repository.ICfgComponentColGroupRepository;
import com.lakala.dynamicreport.report.service.ICfgComponentColGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报表组件列组接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgComponentColGroupService")
public class CfgComponentColGroupServiceImpl extends BaseServiceImpl<ICfgComponentColGroupRepository,CfgComponentColGroup, String> implements ICfgComponentColGroupService {

    @Autowired
    ICfgComponentColGroupRepository cfgComponentColGroupRepository;

    public List<CfgComponentColGroup> findCfgComponentColGroup(CfgComponentColGroupQuery cfgComponentColGroupQuery) {
        return cfgComponentColGroupRepository.findAll(specification(cfgComponentColGroupQuery),PageForList.getSort("id",cfgComponentColGroupQuery));
    }

    public Page<CfgComponentColGroup> findCfgComponentColGroupCriteria(final CfgComponentColGroupQuery cfgComponentColGroupQuery) {
        return cfgComponentColGroupRepository.findAll(specification(cfgComponentColGroupQuery), PageForList.getPageable("modifiedDate",cfgComponentColGroupQuery));
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CfgComponentColGroup> specification(final CfgComponentColGroupQuery cfgComponentColGroupQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cfgComponentColGroupQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentColGroupQuery.getId(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentColGroupQuery.getName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",cfgComponentColGroupQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",cfgComponentColGroupQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgComponentColGroupQuery.getVersionNo(),Integer.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentColGroupQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentColGroupQuery.getSearchText(),String.class,OperationEnum.OR));
        }
        return SpecificationUtil.whereAndOr(list);
    }

    @Override
    public List<CfgComponentColGroup> findByComponentId(String componentId) {
        return cfgComponentColGroupRepository.findByComponentId(componentId);
    }
}