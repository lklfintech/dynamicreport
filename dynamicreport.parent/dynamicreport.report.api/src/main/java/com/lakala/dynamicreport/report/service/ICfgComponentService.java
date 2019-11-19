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
package com.lakala.dynamicreport.report.service;

import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.lakala.dynamicreport.report.model.CfgComponent;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.query.CfgComponentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 报表组件接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgComponentService extends IBaseService<CfgComponent,String>{
    /**
     * 带条件分页查询
     *
     * @param cfgComponentQuery
     * @return
     */
    Page<CfgComponent> findCfgComponentCriteria(CfgComponentQuery cfgComponentQuery);

    /**
     * 带条件查询
     *
     * @param cfgComponentQuery
     * @return
     */
    List<CfgComponent> findCfgComponent(CfgComponentQuery cfgComponentQuery);

    /**
     * 保存组件（事务操作）
     *
     * @param cfgComponent
     * @return
     */
    void saveCfgComponent(CfgComponent cfgComponent, List<CfgComponentCol> cols);

    /**
     * 根据page id查找
     *
     * @param pageId
     * @return
     */
    List<CfgComponent> findByPageId(String pageId);

    /**
     * 根据数据集获取列信息
     *
     * @param dataList
     * @param newSql
     * @return
     */
    List<CfgComponentCol> initColumns(DataList dataList, String newSql);

    /**
     * 根据DataListId获取表描述
     *
     * @param dataListId
     * @param id
     * @return
     */
    ResultJson findColumns(Long dataListId, String id);


    /**
     * 组装保存和更新时的参数
     *
     * @param component
     * @param action
     * @return
     */
    CfgComponent getParams(String component,String action);

}