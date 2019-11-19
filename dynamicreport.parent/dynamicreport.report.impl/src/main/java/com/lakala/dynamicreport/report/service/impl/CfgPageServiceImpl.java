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

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.lakala.dynamicreport.datamodel.model.DataParameter;
import com.lakala.dynamicreport.datamodel.model.DataParameterGroup;
import com.lakala.dynamicreport.datamodel.model.DataSource;
import com.lakala.dynamicreport.datamodel.repository.IDataListRepository;
import com.lakala.dynamicreport.datamodel.repository.IDataParameterGroupRepository;
import com.lakala.dynamicreport.datamodel.repository.IDataParameterRepository;
import com.lakala.dynamicreport.datamodel.repository.IDataSourceRepository;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.bo.RptPageBO;
import com.lakala.dynamicreport.report.model.*;
import com.lakala.dynamicreport.report.query.CfgPageQuery;
import com.lakala.dynamicreport.report.repository.*;
import com.lakala.dynamicreport.report.service.ICfgPageService;
import com.lakala.dynamicreport.system.model.Menu;
import com.lakala.dynamicreport.system.model.RoleFunction;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.repository.IFunctionRepository;
import com.lakala.dynamicreport.system.repository.IMenuRepository;
import com.lakala.dynamicreport.system.repository.IRoleFunctionRepository;
import com.lakala.dynamicreport.system.repository.IRoleRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报表页面接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgPageService")
public class CfgPageServiceImpl extends BaseServiceImpl<ICfgPageRepository,CfgPage,java.lang.String> implements ICfgPageService {

    @Autowired
    ICfgPageRepository cfgPageRepository;

    @Autowired
    ICfgParamRepository cfgParamRepository;

    @Autowired
    ICfgPageParamRepository cfgPageParamRepository;

    @Autowired
    ICfgPageComponentRepository cfgPageComponentRepository;

    @Autowired
    ICfgComponentTypeRepository cfgComponentTypeRepository;

    @Autowired
    ICfgComponentRepository cfgComponentRepository;

    @Autowired
    ICfgComponentColRepository cfgComponentColRepository;

    @Autowired
    IDataSourceRepository dataSourceRepository;

    @Autowired
    IDataParameterGroupRepository dataParameterGroupRepository;

    @Autowired
    IDataParameterRepository dataParameterRepository;

    @Autowired
    IDataListRepository dataListRepository;

    @Autowired
    ICfgChartTemplateRepository cfgChartTemplateRepository;

    @Autowired
    ICfgComponentColGroupRepository cfgComponentColGroupRepository;

    @Autowired
    IMenuRepository menuRepository;

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    IRoleFunctionRepository roleFunctionRepository;

    @Autowired
    IFunctionRepository functionRepository;


    public List<CfgPage> findCfgPage(CfgPageQuery cfgPageQuery) {
        return cfgPageRepository.findAll(specification(cfgPageQuery), PageForList.getSort("id",cfgPageQuery));
    }

    public Page<CfgPage> findCfgPageCriteria(final CfgPageQuery cfgPageQuery) {
        return cfgPageRepository.findAll(specification(cfgPageQuery), PageForList.getPageable("modifiedDate",cfgPageQuery));
    }

    @Override
    @Transactional
    public void batchDelete(String ids) {
        List<CfgPage> cfgPages = new ArrayList<>();
        if (null != ids && !"".equals(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                if (null != id && !"".equals(id)) {
                    CfgPage cfgPage = cfgPageRepository.getOne(id);
                    cfgPages.add(cfgPage);
                    // 删除组件和参数关联
                    cfgPageParamRepository.deleteByPageId(id);
                    cfgPageComponentRepository.deleteByPageId(id);
                }
            }
        }
        cfgPageRepository.deleteInBatch(cfgPages);
    }

    public List<RptPageBO> exportJson(String ids) {
        List<RptPageBO> resultList = new ArrayList<>();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            RptPageBO rptPageBO = new RptPageBO();
            // 查找报表page配置
            CfgPage page = cfgPageRepository.getOne(id);
            rptPageBO.setPage(page);
            // 查找报表参数
            List<CfgParam> paramList = cfgParamRepository.findByPageId(id);
            rptPageBO.setParamList(paramList);
            // 查找报表组件
            List<CfgComponent> componentList = cfgComponentRepository.findByPageId(id);
            if (CollectionUtils.isNotEmpty(componentList)) {
                List<RptComponentBO> componentBOList = new ArrayList<>();
                for (CfgComponent component : componentList) {
                    RptComponentBO componentBO = new RptComponentBO();
                    componentBO.setComponent(component);
                    // 查找绩组件列定义
                    List<CfgComponentCol> componentColList = cfgComponentColRepository.findByComponentId(component.getId());
                    componentBO.setComponentColList(componentColList);
                    componentBOList.add(componentBO);
                }
                rptPageBO.setComponentList(componentBOList);
            }
            resultList.add(rptPageBO);
        }
        return resultList;
    }

    @Transactional
    public int importJson(String rptJsonStr) {
        List rptList = JSONObject.parseObject(rptJsonStr, List.class);
        if (CollectionUtils.isNotEmpty(rptList)) {
            for (int i = 0; i < rptList.size(); i++) {
                RptPageBO pageBO = JSONObject.toJavaObject((JSONObject) rptList.get(i), RptPageBO.class);
                // 1 保存页面表
                CfgPage page = pageBO.getPage();
                page.setId(null);
                page.setPublished("N");
                page.setPublishedPath(null);
                CfgPage pagePersist = cfgPageRepository.save(page);

                // 2 保存参数表及其关联的组件类型表、数据源表、参数组表、参数表、数据集表、页面参数关联表
                List<CfgParam> paramList = pageBO.getParamList();
                if (CollectionUtils.isNotEmpty(paramList)) {
                    for (CfgParam param : paramList) {
                        // 2.1 保存组件类型
                        if (null != param.getType()) {
                            param.setType(importComponentType(param.getType()));
                        }
                        // 2.2 保存数据源表、参数组表、参数表、数据集表
                        if (null != param.getDataList()) {
                            param.setDataList(importDataList(param.getDataList()));
                        }
                        // 2.3 保存参数
                        param.setId(null);
                        CfgParam paramPersist = cfgParamRepository.save(param);
                        // 2.4 保存报表参数关联
                        CfgPageParam pageParam = new CfgPageParam();
                        pageParam.setPage(pagePersist);
                        pageParam.setParam(paramPersist);
                        cfgPageParamRepository.save(pageParam);
                    }
                }

                // 3 保存组件表
                List<RptComponentBO> rptComponentBOList = pageBO.getComponentList();
                if (CollectionUtils.isNotEmpty(rptComponentBOList)) {
                    for (RptComponentBO componentBO : rptComponentBOList) {
                        CfgComponent component = componentBO.getComponent();
                        // 3.1 保存组件类型
                        if (null != component.getType()) {
                            component.setType(importComponentType(component.getType()));
                        }
                        // 3.2 保存数据源表、参数组表、参数表、数据集表
                        if (null != component.getDataList()) {
                            component.setDataList(importDataList(component.getDataList()));
                        }
                        // 3.3 保存模板表
                        CfgChartTemplate chartTemplate = component.getCfgChartTemplate();
                        if (null != chartTemplate) {
                            CfgChartTemplate chartTemplatePersist = cfgChartTemplateRepository.findByName(chartTemplate.getName());
                            if (null == chartTemplatePersist) {
                                chartTemplate.setId(null);
                                chartTemplatePersist = cfgChartTemplateRepository.save(chartTemplate);
                            }
                            component.setCfgChartTemplate(chartTemplatePersist);
                        }
                        // 3.4 保存组件
                        component.setId(null);
                        CfgComponent componentPersist = cfgComponentRepository.save(component);
                        // 3.5 保存组件列及列分组
                        List<CfgComponentCol> componentColList = componentBO.getComponentColList();
                        if (CollectionUtils.isNotEmpty(componentColList)) {
                            for (CfgComponentCol componentCol : componentColList) {
                                // 3.5.1 保存列分组
                                CfgComponentColGroup componentColGroup = componentCol.getGroup();
                                if (null != componentColGroup) {
                                    CfgComponentColGroup componentColGroupPersist = cfgComponentColGroupRepository.findByName(componentColGroup.getName());
                                    if (null == componentColGroupPersist) {
                                        componentColGroup.setId(null);
                                        componentColGroupPersist = cfgComponentColGroupRepository.save(componentColGroup);
                                    }
                                    componentCol.setGroup(componentColGroupPersist);
                                }
                                // 3.5.2 保存列
                                componentCol.setComponent(componentPersist);
                                componentCol.setId(null);
                                cfgComponentColRepository.save(componentCol);
                            }
                        }
                        // 3.6 保存报表组件关联
                        CfgPageComponent pageComponent = new CfgPageComponent();
                        pageComponent.setPage(pagePersist);
                        pageComponent.setComponent(componentPersist);
                        cfgPageComponentRepository.save(pageComponent);
                    }
                }
            }
        }
        return rptList.size();
    }

    @Override
    @Transactional
    public ResultJson deploy(String pageId, Long parent, String roleIds,String servletPath) {
        ResultJson resultJson = new ResultJson();
        resultJson.setSuccess(false);
        CfgPage updateObj = findOne(pageId);
        if (StatusConstants.INACTIVE.equals(updateObj.getStatus())) {
            resultJson.setMsg("该报表未启用");
            return resultJson;
        }

        SystemFunction systemFunction = functionRepository.findById(pageId).orElse(null);
        if (systemFunction != null) {
            resultJson.setMsg("发布失败:" + updateObj.getTitle() + ":已发布");
            return resultJson;
        }

        Menu parentMenu = menuRepository.getOne(parent);
        if (parentMenu == null) {
            resultJson.setMsg("发布失败,菜单不存在:" + parent);
            return resultJson;
        }

        updateObj.setPublished(StatusConstants.YES_FLAG);
        updateObj.setPublishedPath(getDeployPath(parentMenu));
        cfgPageRepository.saveAndFlush(updateObj);

        SystemFunction sf = new SystemFunction();
        sf.setId(pageId);
        sf.setName(updateObj.getTitle());
        sf.setParent(parentMenu.getSystemFunction());
        sf.setType("report");
        sf.setStatus(StatusConstants.ACTIVE);
        systemFunction = functionRepository.save(sf);

        Menu menu = new Menu();
        menu.setName(updateObj.getTitle());
        menu.setSystemFunction(systemFunction);
        menu.setParent(parentMenu);
        menu.setPath(servletPath + "?pageId=" + pageId);
        menuRepository.save(menu);

        List<RoleFunction> roleFunctionList = new ArrayList<>();
        if(roleIds != null){
            for(String roleId:roleIds.split(",")){
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setRole(roleRepository.getOne(Long.parseLong(roleId)));
                roleFunction.setSystemFunction(systemFunction);
                roleFunctionList.add(roleFunction);
            }
        }

        if(roleFunctionList.size() > 0){
            roleFunctionRepository.saveAll(roleFunctionList);
        }
        resultJson.setSuccess(true);
        resultJson.setMsg("发布成功");
        return resultJson;
    }

    @Override
    @Transactional
    public ResultJson undeployService(ServletRequest request , String pageId) {
        ResultJson resultJson = new ResultJson();
        resultJson.setSuccess(false);
        SystemFunction systemFunction = functionRepository.findById(pageId).orElse(null);
        if (systemFunction == null) {
            resultJson.setMsg("该报表还未发布");
            return resultJson;
        }

        HttpServletRequest req=(HttpServletRequest)request;
        HttpSession session=req.getSession();
        ActiveUser userDetail = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
        if(!systemFunction.getCreatedUser().equals(userDetail.getUsername()) && !"超级管理员".equals(userDetail.getRoleName())){
            resultJson.setMsg("无该报表取消发布权限");
            return resultJson;
        }

        // 更新发布状态
        CfgPage updateObj = findOne(pageId);
        updateObj.setPublished(StatusConstants.NO_FLAG);
        updateObj.setPublishedPath(null);
        cfgPageRepository.saveAndFlush(updateObj);

        //解除角色 功能关联
        roleFunctionRepository.deleteByFunction(pageId);

        //删除menu 记录
        menuRepository.deleteByFunction(pageId);

        //删除function
        functionRepository.deleteById(systemFunction.getId());
        resultJson.setSuccess(true);
        resultJson.setMsg("取消发布成功");
        return resultJson;
    }

    /**
     * 获取发布目录
     * @param menu
     * @return
     */
    private String getDeployPath(Menu menu) {
        if (menu.getParent() == null) {
            return menu.getName();
        } else {
            return getDeployPath(menu.getParent()) + "/" + menu.getName();
        }
    }

    /**
     * 保存组件类型
     *
     * @param componentType
     * @return
     */
    private CfgComponentType importComponentType(CfgComponentType componentType) {
        CfgComponentType componentTypePersist = cfgComponentTypeRepository.findByKey(componentType.getKey());
        if (null == componentTypePersist) {
            componentType.setId(null);
            componentTypePersist = cfgComponentTypeRepository.save(componentType);
        }
        return componentTypePersist;
    }

    /**
     * 保存数据源、参数组、参数、数据集
     *
     * @param dataList
     * @return
     */
    private DataList importDataList(DataList dataList) {
        // 1 保存数据源
        DataSource dataSource = dataList.getDataSource();
        DataSource dataSourcePersist = dataSourceRepository.findByIdentifier(dataSource.getIdentifier());
        if (null == dataSourcePersist) {
            dataSource.setId(null);
            dataSourcePersist = dataSourceRepository.save(dataSource);
        }
        dataList.setDataSource(dataSourcePersist);
        // 2 保存参数、参数组
        List<DataParameterGroup> dataParameterGroups = dataList.getDataParameterGroup();
        List<DataParameterGroup> dataParameterGroupPersistList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dataParameterGroups)) {
            for (DataParameterGroup dataParameterGroup : dataParameterGroups) {
                // 2.1 保存参数
                List<DataParameter> dataParameters = dataParameterGroup.getDataParameter();
                List<DataParameter> dataParametersPersistList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(dataParameters)) {
                    for (DataParameter dataParameter : dataParameters) {
                        DataParameter dataParameterPersist = dataParameterRepository.findByIdentify(dataParameter.getIdentifier());
                        if (null == dataParameterPersist) {
                            dataParameter.setId(null);
                            dataParameterPersist = dataParameterRepository.save(dataParameter);
                        }
                        dataParametersPersistList.add(dataParameterPersist);
                    }
                    dataParameterGroup.setDataParameter(dataParametersPersistList);
                }
                // 2.2 保存参数组
                DataParameterGroup dataParameterGroupPersist = dataParameterGroupRepository.findByIdentify(dataParameterGroup.getIdentifier());
                if (null == dataParameterGroupPersist) {
                    dataParameterGroup.setId(null);
                    dataParameterGroupPersist = dataParameterGroupRepository.save(dataParameterGroup);
                }
                dataParameterGroupPersistList.add(dataParameterGroupPersist);
            }
            dataList.setDataParameterGroup(dataParameterGroupPersistList);
        }
        // 3 保存数据集
        DataList dataListPersist = dataListRepository.findByIdentify(dataList.getIdentifier());
        if (null == dataListPersist) {
            dataList.setId(null);
            dataListPersist = dataListRepository.save(dataList);
        }
        return dataListPersist;
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CfgPage> specification(final CfgPageQuery cfgPageQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cfgPageQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"id",cfgPageQuery.getId(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"title",cfgPageQuery.getTitle(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",cfgPageQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",cfgPageQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgPageQuery.getVersionNo(),Integer.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"title",cfgPageQuery.getSearchText(),String.class,OperationEnum.OR));
        }
        return SpecificationUtil.whereAndOr(list);
    }

}