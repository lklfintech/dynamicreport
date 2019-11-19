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
import com.lakala.dynamicreport.report.model.CfgPageParam;
import com.lakala.dynamicreport.report.query.CfgPageParamQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 报表页面参数接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgPageParamService extends IBaseService<CfgPageParam, String>{
	/**
	 * 带条件分页查询
	 *
	 * @param cfgPageParamQuery
	 * @return
	 */
	Page<CfgPageParam> findCfgPageParamCriteria(CfgPageParamQuery cfgPageParamQuery);
	
	/**
	 * 带条件查询
	 *
	 * @param cfgPageParamQuery
	 * @return
	 */
	List<CfgPageParam> findCfgPageParam(CfgPageParamQuery cfgPageParamQuery);
	
	/**
	 * 报表关联多个参数
	 *
	 * @param id
	 * @param ids
	 * @param vos
	 * @param allIds
	 */
	void save(String id, String ids, String vos, String allIds);

	/**
	 * 根据页面ID查询
	 *
	 * @param pageId
	 * @return
	 */
	List<CfgPageParam> findByPageId(String pageId);
}