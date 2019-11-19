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
import com.lakala.dynamicreport.report.model.CfgPageComponent;
import com.lakala.dynamicreport.report.query.CfgPageComponentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 报表页面组件接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ICfgPageComponentService extends IBaseService<CfgPageComponent,String>{
	/**
	 * 带条件分页查询
	 *
	 * @param cfgPageComponentQuery
	 * @return
	 */
	Page<CfgPageComponent> findCfgPageComponentCriteria(CfgPageComponentQuery cfgPageComponentQuery);
	
	/**
	 * 带条件查询
	 *
	 * @param cfgPageComponentQuery
	 * @return
	 */
	List<CfgPageComponent> findCfgPageComponent(CfgPageComponentQuery cfgPageComponentQuery);
	
	/**
	 * 报表关联组件
	 *
	 * @param id
	 * @param ids
	 * @param allIds
	 */
	void save(String id, String ids, String allIds);
}