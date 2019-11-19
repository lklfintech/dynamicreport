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
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.report.constants.RptConstant;
import com.lakala.dynamicreport.report.model.CfgComponentType;
import com.lakala.dynamicreport.report.query.CfgComponentTypeQuery;
import com.lakala.dynamicreport.report.repository.ICfgComponentTypeRepository;
import com.lakala.dynamicreport.report.service.ICfgComponentTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表组件类型接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgComponentTypeService")
public class CfgComponentTypeServiceImpl extends BaseServiceImpl<ICfgComponentTypeRepository,CfgComponentType, String> implements ICfgComponentTypeService {

    @Autowired
    ICfgComponentTypeRepository cfgComponentTypeRepository;

    public List<CfgComponentType> findCfgComponentType(CfgComponentTypeQuery cfgComponentTypeQuery) {
        return cfgComponentTypeRepository.findAll(specification(cfgComponentTypeQuery));
    }

    public Page<CfgComponentType> findCfgComponentTypeCriteria(final CfgComponentTypeQuery cfgComponentTypeQuery) {
        return cfgComponentTypeRepository.findAll(specification(cfgComponentTypeQuery), PageForList.getPageable("sequence",cfgComponentTypeQuery));
    }

    public List<CfgComponentType> findTopComponentTypes() {
        return cfgComponentTypeRepository.findTopComponentTypes();
    }

    @Override
    public List<TreeNode> getTree(List<CfgComponentType> componentTypeList, List<String> ids) {
        List<TreeNode> trees = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(componentTypeList)) {
            for (CfgComponentType type : componentTypeList) {
                TreeNode node = new TreeNode();
                node.setText(type.getName());
                node.setId(type.getId());
                if (CollectionUtils.isNotEmpty(ids) && ids.contains(String.valueOf(type.getId()))) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("checked", Boolean.TRUE);
                    node.setState(map);
                }
                if (CollectionUtils.isNotEmpty(type.getChildrens())) {
                    node.setNodes(getTree(type.getChildrens(), ids));
                }
                trees.add(node);
            }
        }
        return trees;
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CfgComponentType> specification(final CfgComponentTypeQuery cfgComponentTypeQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cfgComponentTypeQuery) {
            list.add(Predication.get(OperationEnum.EQUAL,"id",cfgComponentTypeQuery.getId(),Long.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"key",cfgComponentTypeQuery.getKey(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentTypeQuery.getName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",cfgComponentTypeQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",cfgComponentTypeQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgComponentTypeQuery.getVersionNo(),Integer.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentTypeQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentTypeQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"key",cfgComponentTypeQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"parent.key",cfgComponentTypeQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"parent.name",cfgComponentTypeQuery.getSearchText(),String.class,OperationEnum.OR));
            if (StringUtils.isNotBlank(cfgComponentTypeQuery.getParentKey())) {
                if (RptConstant.TYPE_PARAM.equalsIgnoreCase(cfgComponentTypeQuery.getParentKey())) {
                    list.add(Predication.get(OperationEnum.EQUAL,"parent.key",cfgComponentTypeQuery.getParentKey(),String.class,OperationEnum.AND));
                } else {
                    list.add(Predication.get(OperationEnum.NE,"parent.key",RptConstant.TYPE_PARAM,String.class,OperationEnum.AND));
                }
            }
        }
        return SpecificationUtil.whereAndOr(list);
    }

}