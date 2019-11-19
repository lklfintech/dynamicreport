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

import com.lakala.dynamicreport.datamodel.query.ReportDataQuery;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.model.CfgColDrillParam;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.model.CfgComponentColGroup;
import com.lakala.dynamicreport.report.service.IRptDataTableBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lakala.dynamicreport.report.constants.RptConstant.FLAG_Y;
import static com.lakala.dynamicreport.report.constants.RptConstant.SIGN_COMMA;

/**
 * <p>
 * 报表数据列表生成器接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "rptDataTableBuilder")
public class RptDataTableBuilder implements IRptDataTableBuilder {


    @Override
    public String buildDataTable(RptComponentBO componentBO, ReportDataQuery reportDataQuery, List<CfgComponentColGroup> groupList, Map<String, Object> pivotColumnMap) {
        StringBuilder tableScriptBuilder = new StringBuilder();
        String columnStartStr = null;
        String columnEndStr = null;

        // 判断维度是否显示分组
        boolean isDisplayGroup = false;
        for (CfgComponentColGroup targetGroup : groupList) {
            if (FLAG_Y.equalsIgnoreCase(targetGroup.getDisplay())) {
                for (CfgComponentCol target : componentBO.getTargetList()) {
                    if (null != target.getGroup() && target.getGroup().getId().equals(targetGroup.getId())) {
                        isDisplayGroup = true;
                        break;
                    }
                }
                for (CfgComponentCol dim : componentBO.getDimensionList()) {
                    if (null != dim.getGroup() && dim.getGroup().getId().equals(targetGroup.getId())) {
                        isDisplayGroup = true;
                        break;
                    }
                }
                if (isDisplayGroup) {
                    break;
                }
            }
        }

        // 根据分组设置column开始和结尾
        if (isDisplayGroup) {
            columnStartStr = "[[";
            columnEndStr = "]]";
        } else {
            columnStartStr = "[";
            columnEndStr = "]";
        }

        // 组装分组和未分组的列
        StringBuilder columnBuilder = new StringBuilder(); // 不需要分组的列及列组
        StringBuilder groupColBuilder = new StringBuilder(); // 需要分组的列
        CfgComponentColGroup currentGroup = null;
        int groupCount = 0;
        if (CollectionUtils.isNotEmpty(componentBO.getComponentColList())) {
            for (int i = 0; i < componentBO.getComponentColList().size(); i++) {
                CfgComponentCol componentCol = componentBO.getComponentColList().get(i);
                // 当前列不在选择的维度和指标列中，则跳过该列
                if (!reportDataQuery.getTargetIds().contains(componentCol.getId() + SIGN_COMMA)
                        && !reportDataQuery.getDimensionIds().contains(componentCol.getId() + SIGN_COMMA)) {
                    continue;
                }
                // 当前列是透视维度，则跳过
                if (StringUtils.isNotBlank(reportDataQuery.getPivotDimensionId())
                        && componentCol.getId().equalsIgnoreCase(reportDataQuery.getPivotDimensionId())) {
                    continue;
                }
                // 当前列是透视指标，则跳过
                if (StringUtils.isNotBlank(reportDataQuery.getPivotTargetId())
                        && componentCol.getId().equalsIgnoreCase(reportDataQuery.getPivotTargetId())) {
                    continue;
                }
                if (null != componentCol.getGroup() && FLAG_Y.equalsIgnoreCase(componentCol.getGroup().getDisplay())) { // 列显示分组
                    if (null == currentGroup) {
                        currentGroup = componentCol.getGroup();
                    } else {
                        if (!currentGroup.getId().equalsIgnoreCase(componentCol.getGroup().getId())) {
                            // 分组变化时
                            columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                            // 重置参数
                            groupCount = 0;
                            currentGroup = componentCol.getGroup();
                        }
                    }
                    groupCount++;
                    groupColBuilder.append(buildTableColumn(componentCol, null, null));
                } else { // 列没有或不显示分组
                    if (null != currentGroup) {
                        columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                        // 重置参数
                        groupCount = 0;
                        currentGroup = null;
                    }
                    if (isDisplayGroup) {
                        columnBuilder.append(buildTableColumn(componentCol, "1", "2"));
                    } else {
                        if (null != pivotColumnMap && pivotColumnMap.size() > 0) {
                            if(!containColumn(componentBO.getTargetList(),componentCol)){
                                columnBuilder.append(buildTableColumn(componentCol, null, null));
                            }
                        }else{
                            columnBuilder.append(buildTableColumn(componentCol, null, null));
                        }
                    }
                }

                if (i == componentBO.getComponentColList().size() - 1 && null != currentGroup) {
                    columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                }
            }
        }


        // 透视维度
        if (null != pivotColumnMap && pivotColumnMap.size() > 0) {
            List<String> pivotTargetIdList = null;
            if(reportDataQuery.getPivotTargetId() != null){
                pivotTargetIdList = new ArrayList<>(Arrays.asList(reportDataQuery.getPivotTargetId().split(",")));
            }
            List<String> keyList = new ArrayList<>();
            keyList.addAll(pivotColumnMap.keySet());
            Collections.sort(keyList);
            for (String columnName : keyList) {
                for(CfgComponentCol cfgComponentCol:componentBO.getPivotTargetList()){
                    if(pivotTargetIdList.contains(cfgComponentCol.getId())){
                        CfgComponentCol componentCol = new CfgComponentCol();
                        componentCol.setName(columnName + "(" + cfgComponentCol.getName()+")");
                        componentCol.setColName(columnName + "_" + cfgComponentCol.getAlias());
                        componentCol.setLen((String)pivotColumnMap.get(columnName));
                        if (isDisplayGroup) {
                            columnBuilder.append(buildTableColumn(componentCol, "1", "2"));
                        } else {
                            columnBuilder.append(buildTableColumn(componentCol, null, null));
                        }
                    }
                }
            }
        }

        // 组装表格
        tableScriptBuilder.append(columnStartStr);
        columnBuilder.deleteCharAt(columnBuilder.length() - 1);
        tableScriptBuilder.append(columnBuilder);
        if (isDisplayGroup) {
            tableScriptBuilder.append("],[");
            tableScriptBuilder.append(groupColBuilder);
            tableScriptBuilder.deleteCharAt(tableScriptBuilder.length() - 1);
        }
        tableScriptBuilder.append(columnEndStr);

        return tableScriptBuilder.toString();
    }

    boolean containColumn(List<CfgComponentCol> cfgComponentCols,CfgComponentCol cfgComponentCol){
        boolean ifContain = false;
        for(CfgComponentCol col:cfgComponentCols){
            if(col.getColName().equals(cfgComponentCol.getColName())){
                ifContain = true;
            }
        }
        return ifContain;
    }

    @Override
    public String buildDataTableForExport(RptComponentBO componentBO, ReportDataQuery reportDataQuery, List<CfgComponentColGroup> groupList, Map<String, Object> pivotColumnMap) {
        StringBuilder tableScriptBuilder = new StringBuilder();
        String columnStartStr = null;
        String columnEndStr = null;

        // 判断维度是否显示分组
        boolean isDisplayGroup = false;
        for (CfgComponentColGroup targetGroup : groupList) {
            if (FLAG_Y.equalsIgnoreCase(targetGroup.getDisplay())) {
                for (CfgComponentCol target : componentBO.getTargetList()) {
                    if (null != target.getGroup() && target.getGroup().getId().equals(targetGroup.getId())) {
                        isDisplayGroup = true;
                        break;
                    }
                }
                for (CfgComponentCol dim : componentBO.getDimensionList()) {
                    if (null != dim.getGroup() && dim.getGroup().getId().equals(targetGroup.getId())) {
                        isDisplayGroup = true;
                        break;
                    }
                }
                if (isDisplayGroup) {
                    break;
                }
            }
        }

        // 根据分组设置column开始和结尾
        if (isDisplayGroup) {
            columnStartStr = "[[";
            columnEndStr = "]]";
        } else {
            columnStartStr = "[";
            columnEndStr = "]";
        }

        // 组装分组和未分组的列
        StringBuilder columnBuilder = new StringBuilder(); // 不需要分组的列及列组
        StringBuilder groupColBuilder = new StringBuilder(); // 需要分组的列
        CfgComponentColGroup currentGroup = null;
        int groupCount = 0;
        if (CollectionUtils.isNotEmpty(componentBO.getComponentColList())) {
            for (int i = 0; i < componentBO.getComponentColList().size(); i++) {
                CfgComponentCol componentCol = componentBO.getComponentColList().get(i);
                // 当前列不在选择的维度和指标列中，则跳过该列
                if (!reportDataQuery.getTargetIds().contains(componentCol.getId() + SIGN_COMMA)
                        && !reportDataQuery.getDimensionIds().contains(componentCol.getId() + SIGN_COMMA)) {
                    continue;
                }
                // 当前列是透视维度，则跳过
                if (StringUtils.isNotBlank(reportDataQuery.getPivotDimensionId())
                        && componentCol.getId().equalsIgnoreCase(reportDataQuery.getPivotDimensionId())) {
                    continue;
                }
                // 当前列是透视指标，则跳过
                if (StringUtils.isNotBlank(reportDataQuery.getPivotTargetId())
                        && componentCol.getId().equalsIgnoreCase(reportDataQuery.getPivotTargetId())) {
                    continue;
                }
                if (null != componentCol.getGroup() && FLAG_Y.equalsIgnoreCase(componentCol.getGroup().getDisplay())) { // 列显示分组
                    if (null == currentGroup) {
                        currentGroup = componentCol.getGroup();
                    } else {
                        if (!currentGroup.getId().equalsIgnoreCase(componentCol.getGroup().getId())) {
                            // 分组变化时
                            columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                            // 重置参数
                            groupCount = 0;
                            currentGroup = componentCol.getGroup();
                        }
                    }
                    groupCount++;
                    groupColBuilder.append(buildTableColumnForExport(componentCol, null, null));
                } else { // 列没有或不显示分组
                    if (null != currentGroup) {
                        columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                        // 重置参数
                        groupCount = 0;
                        currentGroup = null;
                    }
                    if (isDisplayGroup) {
                        columnBuilder.append(buildTableColumnForExport(componentCol, "1", "2"));
                    } else {
                        columnBuilder.append(buildTableColumnForExport(componentCol, null, null));
                    }
                }

                if (i == componentBO.getComponentColList().size() - 1 && null != currentGroup) {
                    columnBuilder.append(buildTableColumnGroup(currentGroup.getName(), String.valueOf(groupCount), "1"));
                }
            }
        }

        // 透视维度
        if (null != pivotColumnMap && pivotColumnMap.size() > 0) {
            List<String> keyList = new ArrayList<>();
            keyList.addAll(pivotColumnMap.keySet());
            Collections.sort(keyList);
            for (String columnName : keyList) {
                CfgComponentCol componentCol = new CfgComponentCol();
                componentCol.setName(columnName);
                componentCol.setColName(columnName);
                componentCol.setLen((String)pivotColumnMap.get(columnName));
                if (isDisplayGroup) {
                    columnBuilder.append(buildTableColumn(componentCol, "1", "2"));
                } else {
                    columnBuilder.append(buildTableColumn(componentCol, null, null));
                }
            }
        }

        // 组装表格
        tableScriptBuilder.append(columnStartStr);
        columnBuilder.deleteCharAt(columnBuilder.length() - 1);
        tableScriptBuilder.append(columnBuilder);
        if (isDisplayGroup) {
            tableScriptBuilder.append("],[");
            tableScriptBuilder.append(groupColBuilder);
            tableScriptBuilder.deleteCharAt(tableScriptBuilder.length() - 1);
        }
        tableScriptBuilder.append(columnEndStr);

        return tableScriptBuilder.toString();
    }

    /**
     * 生成table列
     *
     * @param componentCol
     * @param colspan
     * @param rowspan
     * @return
     */
    private String buildTableColumn(CfgComponentCol componentCol, String colspan, String rowspan) {
        StringBuilder tableStrBuilder = new StringBuilder();
        tableStrBuilder.append("{");
        tableStrBuilder.append("title: \"").append(componentCol.getName()).append("\",");
        if (StringUtils.isNotBlank(componentCol.getAlias())) {
            tableStrBuilder.append("field: \"").append(componentCol.getAlias()).append("\",");
        } else {
            tableStrBuilder.append("field: \"").append(componentCol.getColName()).append("\",");
        }
        tableStrBuilder.append("sortable: true,");
        if (StringUtils.isNotEmpty(colspan)) {
            tableStrBuilder.append("colspan:").append(colspan).append(",");
        }
        if (StringUtils.isNotEmpty(rowspan)) {
            tableStrBuilder.append("rowspan:").append(rowspan).append(",");
        }
        if (StringUtils.isNotEmpty(componentCol.getLen())) {
            tableStrBuilder.append("width:\"").append(componentCol.getLen()).append("\",");
        }

        // 下钻链接
        if (null != componentCol.getPage()) {
            // 下钻参数
            StringBuilder drillParam = new StringBuilder("'pageId=");
            drillParam.append(componentCol.getPage().getId()).append("'");
            if (CollectionUtils.isNotEmpty(componentCol.getDrillParamList())) {
                for (CfgColDrillParam colDrillParam : componentCol.getDrillParamList()) {
                    drillParam.append("+'&").append(colDrillParam.getTgtParamName()).append("='+row.").append(colDrillParam.getParamName());
                }
            }
            // 下钻formatter
            tableStrBuilder.append("formatter: function (value, row, index) {");
            tableStrBuilder.append("return '<a class=\"J_menuItem\" href=\"javascript:void(0)\" onclick=\"drillReport('+\"'\"+").append(drillParam.toString()).append("+\"','\"");
            tableStrBuilder.append("+'").append(componentCol.getPage().getTitle()).append("'");
            tableStrBuilder.append("+\"'\"+')\">'+value+'</a>'");
            tableStrBuilder.append("},");
        }

        tableStrBuilder.deleteCharAt(tableStrBuilder.length() - 1);
        tableStrBuilder.append("},");
        return tableStrBuilder.toString();
    }

    /**
     * 生成table列
     *
     * @param componentCol
     * @param colspan
     * @param rowspan
     * @return
     */
    private String buildTableColumnForExport(CfgComponentCol componentCol, String colspan, String rowspan) {
        StringBuilder tableStrBuilder = new StringBuilder();
        tableStrBuilder.append("{");
        tableStrBuilder.append("title: \"").append(componentCol.getName()).append("\",");
        if (StringUtils.isNotBlank(componentCol.getAlias())) {
            tableStrBuilder.append("field: \"").append(componentCol.getAlias()).append("\",");
        } else {
            tableStrBuilder.append("field: \"").append(componentCol.getColName()).append("\",");
        }
        tableStrBuilder.append("sortable: true,");
        if (StringUtils.isNotEmpty(colspan)) {
            tableStrBuilder.append("colspan:").append(colspan).append(",");
        }
        if (StringUtils.isNotEmpty(rowspan)) {
            tableStrBuilder.append("rowspan:").append(rowspan).append(",");
        }
        if (StringUtils.isNotEmpty(componentCol.getLen())) {
            tableStrBuilder.append("width:\"").append(componentCol.getLen()).append("\",");
        }

        tableStrBuilder.deleteCharAt(tableStrBuilder.length() - 1);
        tableStrBuilder.append("},");
        return tableStrBuilder.toString();
    }

    /**
     * 生成table列分组
     *
     * @param title
     * @param colspan
     * @param rowspan
     * @return
     */
    private String buildTableColumnGroup(String title, String colspan, String rowspan) {
        StringBuilder tableStrBuilder = new StringBuilder();
        tableStrBuilder.append("        {");
        tableStrBuilder.append("        title: \"").append(title).append("\",");
        tableStrBuilder.append("        colspan: ").append(colspan).append(",");
        tableStrBuilder.append("        rowspan: ").append(rowspan).append(",");
        tableStrBuilder.append("        valign:\"middle\",");
        tableStrBuilder.append("        align:\"center\"");
        tableStrBuilder.append("},");
        return tableStrBuilder.toString();
    }
}
