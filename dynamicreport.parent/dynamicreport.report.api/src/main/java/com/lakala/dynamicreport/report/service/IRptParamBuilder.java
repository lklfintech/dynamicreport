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

import com.lakala.dynamicreport.report.model.CfgPageParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表参数生成器接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRptParamBuilder {

    /**
     * 生成日期框
     *
     * @param pageParam
     * @return
     */
    String buildDateTime(CfgPageParam pageParam);

    /**
     * 生成文本框
     *
     * @param pageParam
     * @return
     */
    String buildText(CfgPageParam pageParam);

    /**
     * 生成单选下拉框
     *
     * @param pageParam
     * @param dataList
     * @return
     */
    String buildSingleComboBox(CfgPageParam pageParam, List<Map<String, Object>> dataList);

    /**
     * 生成多选下拉框
     *
     * @param pageParam
     * @param dataList
     * @return
     */
    String buildMultipleComboBox(CfgPageParam pageParam, List<Map<String, Object>> dataList);

}
