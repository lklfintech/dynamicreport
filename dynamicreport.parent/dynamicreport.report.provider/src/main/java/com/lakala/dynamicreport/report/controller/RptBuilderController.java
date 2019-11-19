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
package com.lakala.dynamicreport.report.controller;

import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.bo.RptParamBO;
import com.lakala.dynamicreport.report.service.IRptBuilderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表生成controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/report")
@Api(tags = "报表生成管理",description = "报表生成控制器(RptBuilderController)")
public class RptBuilderController {
    private static final Logger log = LoggerFactory.getLogger(RptBuilderController.class);

    @Autowired
    IRptBuilderService rptBuilderService;


    @ApiOperation(value = "生成参数html")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageId",value = "页面ID",required = true)
    })
    @PostMapping(value = "/param/build")
    public RptParamBO buildParamHtml(String pageId) throws Exception{
        return rptBuilderService.buildParamHtml(pageId);
    }

    @ApiOperation(value = "生成组件的头部html（维度和指标选择栏）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageId",value = "页面ID",required = true)
    })
    @PostMapping(value = "/component/header/build")
    public List<RptComponentBO> buildComponentHeaderHtml(String pageId) throws Exception {
        return rptBuilderService.buildComponentHeaderHtml(pageId);
    }

    @ApiOperation(value = "查询数据列表的表头")
    @GetMapping(value = "/component/table/header")
    public Map<String, Object> queryTableHeader(HttpServletRequest request) {
        return rptBuilderService.queryTableHeader(request.getParameterMap());
    }

    @ApiOperation(value = "查询数据列表的数据（分页）")
    @GetMapping(value = "/component/table/data")
    public PageBean<Map<String, Object>> queryTableData(HttpServletRequest request) {
        return rptBuilderService.queryTableData(request.getParameterMap());
    }

    @ApiOperation(value = "查询数据列表的数据（不分页）")
    @GetMapping(value = "/component/table/data-no-paging")
    public Map<String, Object> queryTableDataNoPaging(HttpServletRequest request) {
        return rptBuilderService.queryTableDataNoPaging(request.getParameterMap());
    }

    @ApiOperation(value = "导出")
    @GetMapping(value = "/component/table/export")
    public void exportFile(HttpServletRequest request,HttpServletResponse response){
        try {
           rptBuilderService.exportFile(request,response);
        } catch (Exception e) {
            log.error("批量导出失败:" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "导出(透视)")
    @GetMapping(value = "/component/table/pivot/export")
    public void exportPivotFile(HttpServletRequest request,HttpServletResponse response){
        try {
            rptBuilderService.exportPivotFile(request,response);
        } catch (Exception e) {
            log.error("批量导出失败:" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询透视数据列表")
    @GetMapping(value = "/component/table/pivot")
    public Map<String, Object> queryPivotTable(HttpServletRequest request) {
        return rptBuilderService.queryPivotTable(request.getParameterMap());
    }

    @ApiOperation(value = "查询图表模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "componentId",value = "组件ID",required = true)
    })
    @GetMapping(value = "/component/chart/template/query")
    public String queryChartTemplate(String componentId) {
        return rptBuilderService.queryChartTemplate(componentId);
    }

    @ApiOperation(value = "查询图表数据")
    @GetMapping(value = "/component/chart/data/query")
    public RptComponentBO queryChartData(HttpServletRequest request) {
        return rptBuilderService.queryChartData(request.getParameterMap());
    }

    @ApiOperation(value = "根据组件ID查询图表头部维度和指标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "componentId",value = "组件ID",required = true)
    })
    @PostMapping(value = "/component/chart/header/build")
    public RptComponentBO buildChartHeaderHtml(String componentId) {
        return rptBuilderService.buildChartHeaderHtml(componentId);
    }

}
