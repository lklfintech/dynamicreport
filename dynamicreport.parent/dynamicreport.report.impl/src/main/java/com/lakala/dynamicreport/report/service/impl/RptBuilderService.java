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

import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.utils.CreateFileUtils;
import com.lakala.dynamicreport.common.utils.JarTools;
import com.lakala.dynamicreport.datamodel.query.ReportDataQuery;
import com.lakala.dynamicreport.datamodel.service.IDataListService;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.bo.RptParamBO;
import com.lakala.dynamicreport.report.constants.RptConstant;
import com.lakala.dynamicreport.report.model.*;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import com.lakala.dynamicreport.report.service.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import static com.lakala.dynamicreport.report.constants.RptConstant.*;

/**
 * <p>
 * 报表生成接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "rptBuilderService")
public class RptBuilderService implements IRptBuilderService {

    private static final Logger log = LoggerFactory.getLogger(RptBuilderService.class);

    private final ICfgPageService pageService;
    private final ICfgParamService paramService;
    private final ICfgPageParamService pageParamService;
    private final ICfgComponentService componentService;
    private final ICfgComponentColService componentColService;
    private final ICfgComponentColGroupService componentColGroupService;
    private final IRptParamBuilder rptParamBuilder;
    private final RptDataTableBuilder rptDataTableBuilder;
    private final IDataListService dataListService;

    @Autowired
    public RptBuilderService(ICfgPageService pageService,
                             ICfgParamService paramService,
                             ICfgPageParamService pageParamService,
                             ICfgComponentService componentService,
                             ICfgComponentColService componentColService,
                             ICfgComponentColGroupService componentColGroupService,
                             IRptParamBuilder rptParamBuilder,
                             RptDataTableBuilder rptDataTableBuilder,
                             IDataListService dataListService) {
        this.pageService = pageService;
        this.paramService = paramService;
        this.pageParamService = pageParamService;
        this.componentService = componentService;
        this.componentColService = componentColService;
        this.componentColGroupService = componentColGroupService;
        this.rptParamBuilder = rptParamBuilder;
        this.rptDataTableBuilder = rptDataTableBuilder;
        this.dataListService = dataListService;
    }

    @Override
    public RptParamBO buildParamHtml(String pageId) {
        RptParamBO rptParamBO = new RptParamBO();
        if (StringUtils.isBlank(pageId)) {
            rptParamBO.setErrorMsg("报表ID不能为空！");
        }
        // 报表标题
        CfgPage page = pageService.findOne(pageId);
        rptParamBO.setTitle(page.getTitle());
        rptParamBO.setRemark(page.getRemark());
        // 报表条件
        StringBuilder paramHtmlBuilder = new StringBuilder();
        List<CfgPageParam> pageParamList = pageParamService.findByPageId(pageId);
        if (CollectionUtils.isNotEmpty(pageParamList)) {
            for (CfgPageParam pageParam : pageParamList) {
                try {
                    String type = pageParam.getParam().getType().getKey();
                    switch (type) {
                        case PARAM_DATETIME:
                            paramHtmlBuilder.append(rptParamBuilder.buildDateTime(pageParam));
                            break;
                        case PARAM_TEXT:
                            paramHtmlBuilder.append(rptParamBuilder.buildText(pageParam));
                            break;
                        case PARAM_SINGLE_COMBOBOX:
                            ReportDataQuery singleDataQuery = new ReportDataQuery();
                            singleDataQuery.setDataListId(pageParam.getParam().getDataList().getId());
                            List<Map<String, Object>> singleDataList = dataListService.queryForReport(singleDataQuery);
                            paramHtmlBuilder.append(rptParamBuilder.buildSingleComboBox(pageParam, singleDataList));
                            break;
                        case PARAM_MULTI_COMBOBOX:
                            ReportDataQuery multipleDataQuery = new ReportDataQuery();
                            multipleDataQuery.setDataListId(pageParam.getParam().getDataList().getId());
                            List<Map<String, Object>> multipleDataList = dataListService.queryForReport(multipleDataQuery);
                            paramHtmlBuilder.append(rptParamBuilder.buildMultipleComboBox(pageParam, multipleDataList));
                            break;
                        default:
                            log.error("类型{}不支持", type);
                            break;
                    }
                } catch (Exception e) {
                    log.error("页面{}读取配置错误", e);
                    rptParamBO.setErrorMsg(e.getMessage());
                }
            }
        }
        rptParamBO.setParamHtml(paramHtmlBuilder.toString());
        return rptParamBO;
    }

    @Override
    public List<RptComponentBO> buildComponentHeaderHtml(String pageId) {
        List<RptComponentBO> resultList = new ArrayList<>();

        // 查询页面的组件信息
        List<CfgComponent> componentList = componentService.findByPageId(pageId);
        if (CollectionUtils.isNotEmpty(componentList)) {
            // 查询页面每个组件的列定义信息
            for (CfgComponent component : componentList) {
                RptComponentBO componentBO = new RptComponentBO();
                componentBO.setComponent(component);
                // 将组件的列定义归类（维度和指标）
                classifyColumns(componentBO);
                resultList.add(componentBO);
            }
        }
        return resultList;
    }

    /**
     * 归类列定义，拆分成维度和指标二组
     *
     * @param componentBO
     */
    private void classifyColumns(RptComponentBO componentBO) {
        CfgComponentColQuery query = new CfgComponentColQuery();
        query.setComponent(componentBO.getComponent().getId());
        query.setSortName("sequence");
        query.setSortOrder(Sort.Direction.ASC.toString());
        List<CfgComponentCol> componentColList = componentColService.findCfgComponentCol(query);
        if (CollectionUtils.isNotEmpty(componentColList)) {
            List<CfgComponentCol> dimensionList = new ArrayList<>();
            List<CfgComponentCol> targetList = new ArrayList<>();
            List<CfgComponentCol> pivotDimensionList = new ArrayList<>();
            List<CfgComponentCol> pivotTargetList = new ArrayList<>();
            for (CfgComponentCol componentCol : componentColList) {
                switch (componentCol.getType()) {
                    case FLAG_DIM:
                        dimensionList.add(componentCol);
                        if (FLAG_Y.equalsIgnoreCase(componentCol.getPrivot())) {
                            pivotDimensionList.add(componentCol);
                        }
                        break;
                    case FLAG_TARGET:
                        targetList.add(componentCol);
                        if (FLAG_Y.equalsIgnoreCase(componentCol.getPrivot())) {
                            pivotTargetList.add(componentCol);
                        }
                        break;
                    default:
                        log.error("列ID{}，名称{}：没有定义类型！", componentCol.getId(), componentCol.getName());
                        break;
                }
            }
            componentBO.setComponentColList(componentColList);
            componentBO.setDimensionList(dimensionList);
            componentBO.setTargetList(targetList);
            componentBO.setPivotDimensionList(pivotDimensionList);
            componentBO.setPivotTargetList(pivotTargetList);
        } else {
            log.error("组件ID{}，名称{}：没有列定义！", componentBO.getComponent().getId(), componentBO.getComponent().getName());
        }
    }

    @Override
    public Map<String, Object> queryTableHeader(Map<String, String[]> requestMap) {
        Map<String, Object> resultMap = new HashMap<>();

        try{
            // 解析参数
            ReportDataQuery reportDataQuery = new ReportDataQuery();
            Set<String> keySet = requestMap.keySet();
            for (String key : keySet) {
                String[] valArray = requestMap.get(key);
                switch (key) {
                    case "dimensionIds":
                        reportDataQuery.setDimensionIds(valArray[0]);
                        break;
                    case "targetIds":
                        reportDataQuery.setTargetIds(valArray[0]);
                        break;
                    case "componentId":
                        reportDataQuery.setComponentId(valArray[0]);
                        break;
                    default:
                        break;
                }
            }

            // 查询组件
            RptComponentBO componentBO = new RptComponentBO();
            CfgComponent component = componentService.findOne(reportDataQuery.getComponentId());
            componentBO.setComponent(component);
            // 将组件的列定义归类（维度和指标）
            classifyColumns(componentBO);
            // 查询列分组定义
            List<CfgComponentColGroup> componentColGroupList = componentColGroupService.findByComponentId(reportDataQuery.getComponentId());
            String tableContent = rptDataTableBuilder.buildDataTable(componentBO, reportDataQuery, componentColGroupList, null);
            resultMap.put(KEY_TABLE_COLUMNS, tableContent);
            resultMap.put(KEY_TABLE_NAME, component.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> queryTableHeaderForExport(Map<String, String[]> requestMap) {
        Map<String, Object> resultMap = new HashMap<>();

        // 解析参数
        ReportDataQuery reportDataQuery = new ReportDataQuery();
        Set<String> keySet = requestMap.keySet();
        for (String key : keySet) {
            String[] valArray = requestMap.get(key);
            switch (key) {
                case "dimensionIds":
                    reportDataQuery.setDimensionIds(valArray[0]);
                    break;
                case "targetIds":
                    reportDataQuery.setTargetIds(valArray[0]);
                    break;
                case "componentId":
                    reportDataQuery.setComponentId(valArray[0]);
                    break;
                default:
                    break;
            }
        }

        // 查询组件
        RptComponentBO componentBO = new RptComponentBO();
        CfgComponent component = componentService.findOne(reportDataQuery.getComponentId());
        componentBO.setComponent(component);
        // 将组件的列定义归类（维度和指标）
        classifyColumns(componentBO);
        // 查询列分组定义
        List<CfgComponentColGroup> componentColGroupList = componentColGroupService.findByComponentId(reportDataQuery.getComponentId());
        String tableContent = rptDataTableBuilder.buildDataTableForExport(componentBO, reportDataQuery, componentColGroupList, null);
        resultMap.put(KEY_TABLE_COLUMNS, tableContent);
        resultMap.put(KEY_TABLE_NAME, component.getName());
        return resultMap;
    }

    @Override
    public PageBean<Map<String, Object>> queryTableData(Map<String, String[]> requestMap) {
        ReportDataQuery reportDataQuery = generateTableReportDataQuery(requestMap);
        return dataListService.queryForReportPage(reportDataQuery);
    }

    @Override
    public Map<String, Object> queryTableDataNoPaging(Map<String, String[]> requestMap) {
        Map<String, Object> resultMap = new HashMap<>();
        ReportDataQuery reportDataQuery = generateTableReportDataQuery(requestMap);
        List<Map<String, Object>> dataList = dataListService.queryForReport(reportDataQuery);
        resultMap.put("data", dataList);
        resultMap.put("rowsCount", dataList.size());
        return resultMap;
    }

    @Override
    public Map<String, Object> queryPivotTable(Map<String, String[]> requestMap) {
        Map<String, Object> resultMap = new HashMap<>();

        // 解析参数
        ReportDataQuery reportDataQuery = new ReportDataQuery();
        Map<String, Object> paramMap = new HashMap<>();
        Set<String> keySet = requestMap.keySet();
        for (String key : keySet) {
            String[] valArray = requestMap.get(key);
            switch (key) {
                case "dimensionIds":
                    reportDataQuery.setDimensionIds(valArray[0]);
                    break;
                case "targetIds":
                    reportDataQuery.setTargetIds(valArray[0]);
                    break;
                case "componentId":
                    reportDataQuery.setComponentId(valArray[0]);
                    break;
                case "pivotDimensionId":
                    reportDataQuery.setPivotDimensionId(valArray[0]);
                    break;
                case "pivotTargetIds":
                    reportDataQuery.setPivotTargetId(valArray[0]);
                    break;
                default:
                    if (valArray.length > 1) {
                        paramMap.put(key, Arrays.asList(valArray));
                    } else {
                        paramMap.put(key, valArray[0]);
                    }
                    break;
            }
        }
        // 查询组件
        CfgComponent component = componentService.findOne(reportDataQuery.getComponentId());
        reportDataQuery.setDataListId(component.getDataList().getId());
        // 查询组件维度
        List<CfgComponentCol> dimensionList = findComponentColListByIds(reportDataQuery.getDimensionIds());
        // 查询组件指标
        List<CfgComponentCol> targetList = findComponentColListByIds(reportDataQuery.getTargetIds());
        // 生成查询语句中的select和group by部分
        generateQueryStatement(dimensionList, targetList, reportDataQuery);
        reportDataQuery.setParamMap(paramMap);
        // 查询数据列表值
        List<Map<String, Object>> dataList = dataListService.queryForReport(reportDataQuery);
        // 查询透视维度和指标对象
        CfgComponentCol selectedPivotDimension = componentColService.findOne(reportDataQuery.getPivotDimensionId());
        List<String> pivotTargetIdList = new ArrayList<>(Arrays.asList(reportDataQuery.getPivotTargetId().split(",")));
        List<CfgComponentCol> selectedPivotTargetList = componentColService.findByIds(pivotTargetIdList);
        // 循环数据列表值，根据透视字段进行行转列处理
        List<Map<String, Object>> newDataList = new ArrayList<>();
        Map<String, Object> pivotColumnMap = new HashMap<>();
        Map<String, Object> currentRowMap = null;
        Map<String, Object> previousRowMap = null;
        StringBuilder currentRowDimValBuilder = null;
        StringBuilder previousRowDimValBuilder = null;
        for (Map<String, Object> rowMap : dataList) {
            currentRowMap = new HashMap<>();
            currentRowDimValBuilder = new StringBuilder();

            // 循环所有维度，如果维度是透视维度则行转列
            for (CfgComponentCol componentCol : dimensionList) {
                String colVal = (String) rowMap.get(componentCol.getColName());
                if (componentCol.getId().equalsIgnoreCase(selectedPivotDimension.getId())) {
                    // 记录行转列的值，用于渲染数据列表的column
                    pivotColumnMap.put(colVal, componentCol.getLen());
                } else {
                    currentRowMap.put(componentCol.getColName(), colVal);
                    currentRowDimValBuilder.append(colVal);
                }
            }

            if (null != previousRowDimValBuilder && currentRowDimValBuilder.toString().equals(previousRowDimValBuilder.toString())) {
                // 如果当前行与上一行的非透视维度一致，则行转列
                currentRowMap = previousRowMap;
                for (CfgComponentCol componentCol : selectedPivotTargetList) {
                    currentRowMap.put(rowMap.get(selectedPivotDimension.getColName()) + "_" + componentCol.getAlias(), rowMap.get(componentCol.getAlias()));
                }
            } else {
                // 如果当前行与上一行的非透视维度不一致，则是新的一行数据；此时循环指标
                for (CfgComponentCol componentCol : selectedPivotTargetList) {
                    currentRowMap.put(rowMap.get(selectedPivotDimension.getColName()) + "_" + componentCol.getAlias(), rowMap.get(componentCol.getAlias()));
                }
                newDataList.add(currentRowMap);
            }

            // 循环下一行数据前对变量进行赋值
            previousRowMap = currentRowMap;
            previousRowDimValBuilder = currentRowDimValBuilder;
        }
        resultMap.put(KEY_TABLE_DATA, newDataList);

        // 查询列分组定义
        RptComponentBO componentBO = new RptComponentBO();
        componentBO.setComponent(component);
        classifyColumns(componentBO);
        List<CfgComponentColGroup> componentColGroupList = componentColGroupService.findByComponentId(reportDataQuery.getComponentId());
        String tableColumn = rptDataTableBuilder.buildDataTable(componentBO, reportDataQuery, componentColGroupList, pivotColumnMap);
        resultMap.put(KEY_TABLE_COLUMNS, tableColumn);
        resultMap.put(KEY_TABLE_NAME, component.getName());
        return resultMap;
    }

    @Override
    public String queryChartTemplate(String componentId) {
        CfgComponent component = componentService.findOne(componentId);
        StringBuilder templateBuilder = new StringBuilder();
        templateBuilder.append("<script>").append(component.getCfgChartTemplate().getContent()).append("</script>");
        return templateBuilder.toString();
    }

    @Override
    public RptComponentBO queryChartData(Map<String, String[]> requestMap) {
        ReportDataQuery reportDataQuery = new ReportDataQuery();
        Map<String, Object> paramMap = new HashMap<>();
        // 解析请求参数
        Set<String> keySet = requestMap.keySet();
        for (String key : keySet) {
            String[] valArray = requestMap.get(key);
            switch (key) {
                case "dimensionId":
                    reportDataQuery.setDimensionIds(valArray[0]);
                    break;
                case "targetIds":
                    reportDataQuery.setTargetIds(valArray[0]);
                    break;
                case "chartTypeId":
                    reportDataQuery.setChartTypeId(valArray[0]);
                    break;
                default:
                    if (valArray.length > 1) {
                        paramMap.put(key, Arrays.asList(valArray));
                    } else {
                        paramMap.put(key, valArray[0]);
                    }
                    break;
            }
        }
        // 查询组件
        CfgComponent component = componentService.findOne(reportDataQuery.getChartTypeId());
        reportDataQuery.setDataListId(component.getDataList().getId());
        // 查询组件维度
        List<CfgComponentCol> dimensionList = findComponentColListByIds(reportDataQuery.getDimensionIds());
        // 查询组件指标
        List<CfgComponentCol> targetList = findComponentColListByIds(reportDataQuery.getTargetIds());
        // 生成查询语句中的select和group by部分
        generateQueryStatement(dimensionList, targetList, reportDataQuery);
        // 查询图形数据
        reportDataQuery.setParamMap(paramMap);
        List<Map<String, Object>> dataList = dataListService.queryForReport(reportDataQuery);

        // 构造返回对象
        RptComponentBO componentBO = new RptComponentBO();
        componentBO.setComponent(component);
        componentBO.setDimensionList(dimensionList);
        componentBO.setTargetList(targetList);
        componentBO.setDataList(dataList);
        return componentBO;
    }

    @Override
    public RptComponentBO buildChartHeaderHtml(String componentId) {
        RptComponentBO componentBO = new RptComponentBO();
        CfgComponent component = componentService.findOne(componentId);
        componentBO.setComponent(component);
        // 将组件的列定义归类（维度和指标）
        classifyColumns(componentBO);
        return componentBO;
    }

    @Override
    public void exportFile(HttpServletRequest request, HttpServletResponse response) {
        try{
            Map<String,Object> headerMap = queryTableHeaderForExport(request.getParameterMap());
            Map<String,Object> dataMap = queryTableDataNoPaging(request.getParameterMap());
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("data");
            String filePrefix = ".csv";
            String filePath = JarTools.getCurrentPath() + "export/";
            String fileName = headerMap.get(RptConstant.KEY_TABLE_NAME).toString();

            List<List<Object>> dlist = new ArrayList<>();
            List<Object> headerList = new ArrayList<>();
            headerList = setHead(headerMap, dlist);
            getExcelOutput(dataList, dlist, headerList);
            CreateFileUtils.createCsvFile(dlist, filePath, fileName);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + new String((fileName+ filePrefix).getBytes("UTF-8") ,"iso-8859-1"));
            File filePic = new File(filePath + fileName + filePrefix);

            FileInputStream is = new FileInputStream(filePic); OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i;
            while ((i = is.read(buffer)) != -1) {
                // 不能一次性读完，大文件会内存溢出（不能直接fis.read(buffer);）
                out.write(buffer, 0, i);
            }
            response.flushBuffer();
            out.flush();
        } catch (IOException e) {
            log.error("导出日志文件失败：" + e.getMessage(), e);
        }
    }

    @Override
    public void exportPivotFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String,Object> map = queryPivotTable(request.getParameterMap());
            List<Map<String, Object>> dataList = (List<Map<String, Object>>)map.get(KEY_TABLE_DATA);
            String filePrefix = ".csv";
            String filePath = JarTools.getCurrentPath() + "export/";
            String fileName = map.get(RptConstant.KEY_TABLE_NAME).toString();

            List<List<Object>> dlist = new ArrayList<>();
            List<Object> headerList = new ArrayList<>();
            headerList = setHead(map, dlist);
            getExcelOutput(dataList, dlist, headerList);
            CreateFileUtils.createCsvFile(dlist, filePath, fileName);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-disposition", "attachment;filename=" + new String((fileName+ filePrefix).getBytes("UTF-8") ,"iso-8859-1"));
            File filePic = new File(filePath + fileName + filePrefix);
            FileInputStream is = new FileInputStream(filePic); OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i;
            while ((i = is.read(buffer)) != -1) {
                // 不能一次性读完，大文件会内存溢出（不能直接fis.read(buffer);）
                out.write(buffer, 0, i);
            }
            response.flushBuffer();
            out.flush();
        } catch (IOException e) {
            log.error("导出日志文件失败：" + e.getMessage(), e);
        }
    }

    /**
     * 设置头部
     *
     * @param headerMap
     * @param dlist
     * @return
     */
    public List<Object> setHead(Map<String,Object> headerMap, List<List<Object>> dlist) {
        String tableContent = (String)headerMap.get(RptConstant.KEY_TABLE_COLUMNS);
        JSONArray jsonArray = JSONArray.fromObject(tableContent); //首先把字符串转成 JSONArray对象
        List<Object> headers = new ArrayList<>();
        List<Object> headerList = new ArrayList<>();
        if(jsonArray.get(0) instanceof JSONArray){
            JSONArray jsonArray0 = jsonArray.getJSONArray(0);
            JSONArray jsonArray1 = jsonArray.getJSONArray(1);
            headerList = new ArrayList<>();
            headers = new ArrayList<>();
            int k = 0;
            for(int j=0;j<jsonArray0.size();j++){
                JSONObject jsonObject = jsonArray0.getJSONObject(j);
                int colspans = jsonObject.getInt("colspan");
                if(!jsonObject.containsKey("field")){
                    for(int i =0;i < colspans;i++){
                        headers.add(jsonArray1.getJSONObject(k).getString("title"));
                        headerList.add(jsonArray1.getJSONObject(k).getString("field"));
                        ++k;
                    }
                }else{
                    headerList.add(jsonObject.getString("field"));
                    headers.add(jsonObject.getString("title"));
                }
            }
        }else{
            headerList = new ArrayList<>();
            headers = new ArrayList<>();
            for(int j=0;j<jsonArray.size();j++){
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                headerList.add(jsonObject.getString("field"));
                headers.add(jsonObject.getString("title"));
            }
        }
        dlist.add(headers);
        return headerList;
    }

    private List<CfgComponentCol> findComponentColListByIds(String ids) {
        List<CfgComponentCol> resultList = null;
        if (StringUtils.isNotBlank(ids)) {
            String[] idArray = ids.split(SIGN_COMMA);
            List idList = Arrays.asList(idArray);
            resultList = componentColService.findByIds(idList);
        }
        return resultList;
    }

    /**
     * 生成table查询需要的ReportDataQuery对象
     *
     * @param requestMap
     * @return
     */
    private ReportDataQuery generateTableReportDataQuery(Map<String, String[]> requestMap) {
        ReportDataQuery reportDataQuery = new ReportDataQuery();
        Map<String, Object> paramMap = new HashMap<>();

        // 解析参数
        Set<String> keySet = requestMap.keySet();
        for (String key : keySet) {
            String[] valArray = requestMap.get(key);
            switch (key) {
                case "dimensionIds":
                    reportDataQuery.setDimensionIds(valArray[0]);
                    break;
                case "targetIds":
                    reportDataQuery.setTargetIds(valArray[0]);
                    break;
                case "componentId":
                    reportDataQuery.setComponentId(valArray[0]);
                    break;
                case "pageNumber":
                    if (null != valArray && StringUtils.isNumeric(valArray[0])) {
                        reportDataQuery.setPageNumber(Integer.parseInt(valArray[0]));
                    }
                    break;
                case "pageSize":
                    if (null != valArray && StringUtils.isNumeric(valArray[0])) {
                        reportDataQuery.setPageSize(Integer.parseInt(valArray[0]));
                    }
                    break;
                case "sortName":
                    if (null != valArray && StringUtils.isNotEmpty(valArray[0])) {
                        reportDataQuery.setSortName(valArray[0]);
                    }
                    break;
                case "sortOrder":
                    if (null != valArray && StringUtils.isNotEmpty(valArray[0])) {
                        reportDataQuery.setSortOrder(valArray[0]);
                    }
                    break;
                default:
                    if (valArray.length > 1) {
                        paramMap.put(key, Arrays.asList(valArray));
                    } else {
                        paramMap.put(key, valArray[0]);
                    }
                    break;
            }
        }
        // 查询组件
        CfgComponent component = componentService.findOne(reportDataQuery.getComponentId());
        reportDataQuery.setDataListId(component.getDataList().getId());
        // 查询组件维度
        List<CfgComponentCol> dimensionList = findComponentColListByIds(reportDataQuery.getDimensionIds());
        // 查询组件指标
        List<CfgComponentCol> targetList = findComponentColListByIds(reportDataQuery.getTargetIds());
        // 生成查询语句中的select和group by部分
        generateQueryStatement(dimensionList, targetList, reportDataQuery);
        // 查询数据
        reportDataQuery.setParamMap(paramMap);
        return reportDataQuery;
    }


    /**
     * 生成查询语句中的select, group by和order by部分
     *
     * @param dimensionList
     * @param targetList
     * @param reportDataQuery
     */
    private void generateQueryStatement(List<CfgComponentCol> dimensionList, List<CfgComponentCol> targetList, ReportDataQuery reportDataQuery) {
        StringBuilder selectBuilder = new StringBuilder();
        StringBuilder groupByBuilder = new StringBuilder();
        StringBuilder orderByBuilder = new StringBuilder();
        StringBuilder sortByBuilder = new StringBuilder();
        // 循环维度
        if (CollectionUtils.isNotEmpty(dimensionList)) {
            for (CfgComponentCol componentCol : dimensionList) {
                selectBuilder.append(componentCol.getColName());
                if (StringUtils.isNotBlank(componentCol.getAlias())) {
                    selectBuilder.append(" as ").append(componentCol.getAlias());
                }
                selectBuilder.append(SIGN_COMMA);
                groupByBuilder.append(componentCol.getColName());
                groupByBuilder.append(SIGN_COMMA);
                if (!componentCol.getId().equalsIgnoreCase(reportDataQuery.getPivotDimensionId())) {
                    orderByBuilder.append(componentCol.getColName());
                    orderByBuilder.append(SIGN_COMMA);
                }
            }
        } else {
            log.error("数据列表没有定义维度！");
        }
        // 删除 group by, order by 语句最后的逗号
        groupByBuilder.deleteCharAt(groupByBuilder.length() - 1);
        orderByBuilder.deleteCharAt(orderByBuilder.length() - 1);

        if (CollectionUtils.isNotEmpty(targetList)) {
            for (CfgComponentCol componentCol : targetList) {
                selectBuilder.append(componentCol.getFunc());
                selectBuilder.append("(").append(componentCol.getColName()).append(")");
                if (StringUtils.isNotBlank(componentCol.getAlias())) {
                    selectBuilder.append(" as ").append(componentCol.getAlias());
                }
                selectBuilder.append(SIGN_COMMA);
            }
        } else {
            // 指标为空时，不需要group by语句
            groupByBuilder = new StringBuilder();
        }
        // 删除 select 语句最后的逗号
        selectBuilder.deleteCharAt(selectBuilder.length() - 1);

        // 查询数据
        reportDataQuery.setSelectStatement(selectBuilder.toString());
        reportDataQuery.setGroupByStatement(groupByBuilder.toString());
        if (StringUtils.isNotBlank(reportDataQuery.getSortName())) {
            sortByBuilder.append(reportDataQuery.getSortName());
            if (StringUtils.isNotBlank(reportDataQuery.getSortOrder())) {
                sortByBuilder.append(" ").append(reportDataQuery.getSortOrder());
            }
        } else {
            sortByBuilder = orderByBuilder;
        }
        reportDataQuery.setOrderByStatement(sortByBuilder.toString());
    }

    /**
     * 输出excel值
     *
     * @param list
     * @param dlist
     */
    private void getExcelOutput(List<Map<String, Object>> list, List<List<Object>> dlist,List<Object> headerList) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (Map<String,Object> map : list) {
                List<Object> olist = new ArrayList<>();
                for(int i=0; i<headerList.size();i++){
                    olist.add(map.get(headerList.get(i)));
                }
                dlist.add(olist);
            }
        }
    }

}
