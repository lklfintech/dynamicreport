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
package com.lakala.dynamicreport.report.constants;

/**
 * <p>
 * 报表常量类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class RptConstant {
	private RptConstant(){
		
	}

    /**
     * 参数组件类型定义
     */
    public static final String TYPE_PARAM = "TYPE_PARAM";
    public static final String PARAM_DATETIME = "PARAM_DATETIME"; // 日期框
    public static final String PARAM_TEXT = "PARAM_TEXT"; // 文本框
    public static final String PARAM_SINGLE_COMBOBOX = "PARAM_SINGLE_COMBOBOX"; // 单选下拉框
    public static final String PARAM_MULTI_COMBOBOX = "PARAM_MULTI_COMBOBOX"; // 多选下拉框

    /**
     * 日期参数默认值定义日期参数默认值定义
     */
    public static final String PARAM_VALUE_TODAY = "PARAM_VALUE_TODAY"; // 当天
    public static final String PARAM_VALUE_3_DAYS_AGO = "PARAM_VALUE_3_DAYS_AGO"; // 3天前
    public static final String PARAM_VALUE_4_DAYS_AGO = "PARAM_VALUE_4_DAYS_AGO"; // 4天前
    public static final String PARAM_VALUE_7_DAYS_AGO = "PARAM_VALUE_7_DAYS_AGO"; // 7天前
    public static final String PARAM_VALUE_15_DAYS_AGO = "PARAM_VALUE_15_DAYS_AGO"; // 15天前
    public static final String PARAM_VALUE_A_MONTH_AGO = "PARAM_VALUE_A_MONTH_AGO"; // 1个月前
    public static final String PARAM_VALUE_3_MONTHS_AGO = "PARAM_VALUE_3_MONTHS_AGO"; // 3个月前
    public static final String PARAM_VALUE_6_MONTHS_AGO = "PARAM_VALUE_6_MONTHS_AGO"; // 6个月前
    public static final String PARAM_VALUE_9_MONTHS_AGO = "PARAM_VALUE_9_MONTHS_AGO"; // 9个月前
    public static final String PARAM_VALUE_12_MONTHS_AGO = "PARAM_VALUE_12_MONTHS_AGO"; // 12个月前

    /**
     * 表格组件类型定义
     */
    public static final String TYPE_TABLE = "TYPE_TABLE";
    public static final String TABLE_COMMON_TABLE = "TABLE_COMMON_TABLE"; // 常规表格
    public static final String TABLE_AGGR_TABLE = "TABLE_AGGR_TABLE"; // 聚合表格
    public static final String TABLE_PIVOT_TABLE = "TABLE_PIVOT_TABLE"; // 透视表格

    /**
     * 图表组件类型定义
     */
    public static final String TYPE_CHART = "TYPE_CHART";
    public static final String TYPE_SINGLE_CHART = "TYPE_SINGLE_CHART"; // 单图
    public static final String TYPE_MULTIPLE_CHART = "TYPE_MULTIPLE_CHART"; // 多图
    public static final String CHART_MULTIPLE_DIM = "CHART_MULTIPLE_DIM";// 柱状图|折线图(多维度单指标)

    /**
     * 符号、标志定义
     */
    public static final String SIGN_COMMA = ",";
    public static final String FLAG_Y = "Y";
    public static final String FLAG_DIM = "DIM"; // 维度
    public static final String FLAG_TARGET = "TGT"; // 指标

    /**
     * map key定义
     */
    public static final String KEY_TABLE_COLUMNS = "KEY_TABLE_COLUMNS"; // 数据列
    public static final String KEY_TABLE_DATA = "KEY_TABLE_DATA"; // 数据值
    public static final String KEY_TABLE_NAME = "KEY_TABLE_NAME";//表名



}
