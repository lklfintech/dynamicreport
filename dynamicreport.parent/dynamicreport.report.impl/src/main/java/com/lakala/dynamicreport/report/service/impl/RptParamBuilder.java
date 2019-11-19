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

import com.lakala.dynamicreport.common.utils.DateUtils;
import com.lakala.dynamicreport.report.model.CfgPageParam;
import com.lakala.dynamicreport.report.model.CfgParam;
import com.lakala.dynamicreport.report.service.IRptParamBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.lakala.dynamicreport.report.constants.RptConstant.*;

/**
 * <p>
 * 报表参数生成器接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "rptParamBuilder")
public class RptParamBuilder implements IRptParamBuilder {


    @Override
    public String buildDateTime(CfgPageParam pageParam) {
        CfgParam param = pageParam.getParam();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<div class=\"form-group\">");
        strBuilder.append("  <label class=\"col-sm-3 control-label\">").append(param.getLabel()).append("</label>");
        strBuilder.append("  <div class=\"col-sm-9\">");
        strBuilder.append("    <input type=\"text\" id=\"").append(param.getParamName()).append("\"").append(" name=\"").append(param.getParamName());
        if (StringUtils.isNotBlank(pageParam.getParamValue())) {
            strBuilder.append("\" value=\"");
            Date dateValue = null;
            if (PARAM_VALUE_TODAY.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = new Date();
            } else if (PARAM_VALUE_3_DAYS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addDay(new Date(), -2);
            } else if (PARAM_VALUE_4_DAYS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addDay(new Date(), -3);
            } else if (PARAM_VALUE_7_DAYS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addDay(new Date(), -6);
            } else if (PARAM_VALUE_15_DAYS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addDay(new Date(), -14);
            } else if (PARAM_VALUE_A_MONTH_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addMonth(new Date(), -1);
            } else if (PARAM_VALUE_3_MONTHS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addMonth(new Date(), -3);
            } else if (PARAM_VALUE_6_MONTHS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addMonth(new Date(), -6);
            } else if (PARAM_VALUE_9_MONTHS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addMonth(new Date(), -9);
            } else if (PARAM_VALUE_12_MONTHS_AGO.equalsIgnoreCase(pageParam.getParamValue())) {
                dateValue = DateUtils.addMonth(new Date(), -12);
            }
            if (null != dateValue) {
                if (StringUtils.isNotEmpty(param.getFormat())) {
                    String format = param.getFormat().replaceAll("Y", "y").replaceAll("m", "M").replaceAll("D", "d");
                    strBuilder.append(DateUtils.dateToString(dateValue, format));
                } else {
                    strBuilder.append(DateUtils.dateToString(dateValue, "yyyy/MM/dd"));
                }
            }
            strBuilder.append("\"");
        } else {
            strBuilder.append("\" value=\"\"");
        }
        strBuilder.append(" class=\"form-control layer-date\" placeholder=\"选择日期\" />");
        strBuilder.append("    <script>");
        strBuilder.append("      laydate({");
        strBuilder.append("                 elem:'#").append(param.getParamName()).append("',");
        if (StringUtils.isNotEmpty(param.getFormat())) {
            strBuilder.append("             format:'").append(param.getFormat().toUpperCase()).append("',");
        } else {
            strBuilder.append("             format: 'YYYY/MM/DD',");
        }
        strBuilder.append("                 min: '1900-01-01',");
        strBuilder.append("                 max: '2099-12-31',");
        strBuilder.append("                 istime: false,");
        strBuilder.append("                 istoday: false");
        strBuilder.append("      });");
        strBuilder.append("    </script>");
        strBuilder.append("  </div>");
        strBuilder.append("</div>");
        return strBuilder.toString();
    }

    @Override
    public String buildText(CfgPageParam pageParam) {
        CfgParam param = pageParam.getParam();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<div class=\"form-group\">");
        strBuilder.append("  <label class=\"col-sm-3 control-label\">").append(param.getLabel()).append("</label>");
        strBuilder.append("  <div class=\"col-sm-9\">");
        strBuilder.append("    <input id=\"").append(param.getParamName()).append("\" name=\"").append(param.getParamName()).append("\"");
        if (StringUtils.isNumeric(param.getLen())) {
            strBuilder.append(" maxlength=\"").append(param.getLen()).append("\"");
        }
        strBuilder.append(" type=\"text\"");
        strBuilder.append(" value=\"");
        if (StringUtils.isNotBlank(pageParam.getParamValue())) {
            strBuilder.append(pageParam.getParamValue());
        }
        strBuilder.append("\">");
        strBuilder.append("  </div>");
        strBuilder.append("</div>");
        return strBuilder.toString();
    }

    @Override
    public String buildSingleComboBox(CfgPageParam pageParam, List<Map<String, Object>> dataList) {
        return buildComboBox(pageParam, dataList, false);
    }

    @Override
    public String buildMultipleComboBox(CfgPageParam pageParam, List<Map<String, Object>> dataList) {
        return buildComboBox(pageParam, dataList, true);
    }

    private String buildComboBox(CfgPageParam pageParam, List<Map<String, Object>> dataList, boolean isMultipleSelected) {
        CfgParam param = pageParam.getParam();
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<div class=\"form-group\">");
        strBuilder.append("  <label class=\"col-sm-3 control-label\">").append(param.getLabel()).append("</label>");
        strBuilder.append("  <div class=\"col-sm-9\">");
        strBuilder.append("    <select class=\"form-control\" id=\"").append(param.getParamName()).append("\" name=\"").append(param.getParamName()).append("\"");
        if (isMultipleSelected) {
            strBuilder.append(" multiple=\"multiple\" ");
        }
        if (StringUtils.isNumeric(param.getLen())) {
            strBuilder.append(" style=\"width:").append(param.getLen()).append("px;\"");
        }
        if (FLAG_Y.equalsIgnoreCase(param.getRequired())) {
            strBuilder.append(" required=\"required\" aria-required=\"true\">");
        } else {
            strBuilder.append(">");
        }
        strBuilder.append("<option value=\"\"></option>");
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (Map<String, Object> rowMap : dataList) {
                Iterator iterator = rowMap.entrySet().iterator();
                Map.Entry keyEntry = (Map.Entry) iterator.next();
                Map.Entry valueEntry = (Map.Entry) iterator.next();
                strBuilder.append("<option value=\"");
                strBuilder.append(keyEntry.getValue());
                strBuilder.append("\">");
                strBuilder.append(valueEntry.getValue());
                strBuilder.append("</option>");
            }
        }
        strBuilder.append("    </select>");
        if (isMultipleSelected) {
            strBuilder.append("    <script>");
            strBuilder.append("       $('#").append(param.getParamName()).append("').multiselect();");
            strBuilder.append("    </script>");
        }
        strBuilder.append("  </div>");
        strBuilder.append("</div>");
        return strBuilder.toString();
    }

}
