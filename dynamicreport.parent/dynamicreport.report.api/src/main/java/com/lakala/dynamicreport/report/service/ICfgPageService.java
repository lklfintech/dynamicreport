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
import com.lakala.dynamicreport.report.bo.RptPageBO;
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.query.CfgPageQuery;
import org.springframework.data.domain.Page;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * <p>
 * 报表页面接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgPageService extends IBaseService<CfgPage,String>{

    /**
     * 带条件分页查询
     *
     * @param cfgPageQuery
     * @return
     */
    Page<CfgPage> findCfgPageCriteria(CfgPageQuery cfgPageQuery);

    /**
     * 带条件查询
     *
     * @param cfgPageQuery
     * @return
     */
    List<CfgPage> findCfgPage(CfgPageQuery cfgPageQuery);

    /**
     * 批量导出报表数据
     *
     * @param ids
     * @return
     */
    List<RptPageBO> exportJson(String ids);

    /**
     * 批量导入报表数据
     *
     * @param jsonStr
     * @return
     */
    int importJson(String jsonStr);

    /**
     * 发布
     *
     * @param pageId
     * @param parent
     * @param roleIds
     * @return
     */
    ResultJson deploy(String pageId, Long parent, String roleIds,String servletPath);

    /**
     * 取消发布
     *
     * @param request
     * @param pageId
     * @return
     */
    ResultJson undeployService(ServletRequest request , String pageId);
}