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
import com.lakala.dynamicreport.report.model.CfgParam;
import com.lakala.dynamicreport.report.query.CfgParamQuery;
import com.lakala.dynamicreport.report.repository.ICfgParamRepository;
import com.lakala.dynamicreport.report.service.ICfgParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 报表参数接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgParamService")
public class CfgParamServiceImpl extends BaseServiceImpl<ICfgParamRepository,CfgParam, String> implements ICfgParamService {

    @Autowired
    ICfgParamRepository cfgParamRepository;


    public List<CfgParam> findCfgParam(CfgParamQuery cfgParamQuery) {
        return cfgParamRepository.findAll(specification(cfgParamQuery), PageForList.getSort("id",cfgParamQuery));
    }

    public Page<CfgParam> findCfgParamCriteria(final CfgParamQuery cfgParamQuery) {
        return cfgParamRepository.findAll(specification(cfgParamQuery), PageForList.getPageable("modifiedDate",cfgParamQuery));
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CfgParam> specification(final CfgParamQuery cfgParamQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cfgParamQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgParamQuery.getId(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"type.id",cfgParamQuery.getType(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgParamQuery.getName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",cfgParamQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",cfgParamQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgParamQuery.getVersionNo(),Integer.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"paramName",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"label",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"type.key",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"type.name",cfgParamQuery.getSearchText(),String.class,OperationEnum.OR));
        }
        return SpecificationUtil.whereAndOr(list);
    }
}