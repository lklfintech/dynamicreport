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

import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.bo.RptParamBO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表生成接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRptBuilderService {

    /**
     * 生成报表参数html
     *
     * @param pageId
     * @return
     */
    RptParamBO buildParamHtml(String pageId);

    /**
     * 生成组件的头部html（维度和指标选择栏）
     *
     * @param pageId
     * @return
     */
    List<RptComponentBO> buildComponentHeaderHtml(String pageId);

    /**
     * 查询列表头信息
     *
     * @param requestMap
     * @return
     */
    Map<String, Object> queryTableHeader(Map<String, String[]> requestMap);

    /**
     * 查询列表头信息(用于导出)
     *
     * @param requestMap
     * @return
     */
    Map<String, Object> queryTableHeaderForExport(Map<String, String[]> requestMap);

    /**
     * 查询列表数据（分页）
     *
     * @param requestMap
     * @return
     */
    PageBean<Map<String, Object>> queryTableData(Map<String, String[]> requestMap);

    /**
     * 查询列表数据（不分页）
     *
     * @param requestMap
     * @return
     */
    Map<String, Object> queryTableDataNoPaging(Map<String, String[]> requestMap);

    /**
     * 查询透视数据列表
     *
     * @param requestMap
     * @return
     */
    Map<String, Object> queryPivotTable(Map<String, String[]> requestMap);

    /**
     * 查询图形模板
     *
     * @param componentId
     * @return
     */
    String queryChartTemplate(String componentId);

    /**
     * 查询图形数据
     *
     * @param requestMap
     * @return
     */
    RptComponentBO queryChartData(Map<String, String[]> requestMap);

    /**
     * 生成图形组件的头部html（维度和指标选择栏）
     *
     * @param componentId
     * @return
     */
    RptComponentBO buildChartHeaderHtml(String componentId);

    /**
     * 导出
     *
     * @param request
     * @param response
     */
    void exportFile(HttpServletRequest request, HttpServletResponse response);

    /**
     * 导出(透视)
     *
     * @param request
     * @param response
     */
    void exportPivotFile(HttpServletRequest request,HttpServletResponse response);
}
