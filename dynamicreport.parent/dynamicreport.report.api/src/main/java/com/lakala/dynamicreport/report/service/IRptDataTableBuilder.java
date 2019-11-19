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

import com.lakala.dynamicreport.datamodel.query.ReportDataQuery;
import com.lakala.dynamicreport.report.bo.RptComponentBO;
import com.lakala.dynamicreport.report.model.CfgComponentColGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表数据列表生成器接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRptDataTableBuilder {

    /**
     * 生成表格
     *
     * @param componentBO
     * @param reportDataQuery
     * @param targetGroupList
     * @param pivotColumnMap
     * @return
     */
    String buildDataTable(RptComponentBO componentBO, ReportDataQuery reportDataQuery, List<CfgComponentColGroup> targetGroupList, Map<String, Object> pivotColumnMap);

    /**
     * 生成表格(用于导出)
     *
     * @param componentBO
     * @param reportDataQuery
     * @param targetGroupList
     * @param pivotColumnMap
     * @return
     */
    String buildDataTableForExport(RptComponentBO componentBO, ReportDataQuery reportDataQuery, List<CfgComponentColGroup> targetGroupList, Map<String, Object> pivotColumnMap);

}
