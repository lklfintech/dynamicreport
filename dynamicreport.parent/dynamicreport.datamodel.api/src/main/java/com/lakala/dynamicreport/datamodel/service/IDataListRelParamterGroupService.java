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
package com.lakala.dynamicreport.datamodel.service;

import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.datamodel.model.DataListRelParamterGroup;
import com.lakala.dynamicreport.datamodel.query.DataListRelParamterGroupQuery;

import java.util.List;

/**
 * <p>
 * 数据参数组接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IDataListRelParamterGroupService extends IBaseService<DataListRelParamterGroup, Long>{

 
	/**
	 * 带条件查询
	 * 
	 * @param dataListRelParamterGroupQuery
	 * @return
	 */
	List<DataListRelParamterGroup> findGroups(DataListRelParamterGroupQuery dataListRelParamterGroupQuery);

	/**
	 * 删除
	 *
	 * @param listId
	 */
	void deleteByListId(Long listId);

 
}
