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

import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 报表组件列接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgComponentColService extends IBaseService<CfgComponentCol, String>{
    /**
     * 带条件分页查询
     *
     * @param cfgComponentColQuery
     * @return
     */
    Page<CfgComponentCol> findCfgComponentColCriteria(CfgComponentColQuery cfgComponentColQuery);

    /**
     * 带条件查询
     *
     * @param cfgComponentColQuery
     * @return
     */
    List<CfgComponentCol> findCfgComponentCol(CfgComponentColQuery cfgComponentColQuery);

    /**
     * 根据ID列表查询
     *
     * @param ids
     * @return
     */
    List<CfgComponentCol> findByIds(List<String> ids);

    /**
     * 根据组件ID 删除列
     *
     * @param id
     */
	void deleteByComponentId(String id);

	/**
	 * 批量维护列
     *
	 * @param cols
	 */
	void save(List<CfgComponentCol> cols);
}