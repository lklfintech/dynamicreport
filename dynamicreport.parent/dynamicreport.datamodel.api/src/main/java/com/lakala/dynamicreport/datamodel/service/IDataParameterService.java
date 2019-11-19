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
import com.lakala.dynamicreport.datamodel.model.DataParameter;
import com.lakala.dynamicreport.datamodel.query.DataParameterQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 数据参数接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IDataParameterService extends IBaseService<DataParameter, Long>{


	/**
	 * 带条件分页查询
	 * 
	 * @param dataParameterQuery
	 * @return
	 */
	Page<DataParameter> findDataParameterCriteria(DataParameterQuery dataParameterQuery);

	/**
	 * 带条件查询
	 * 
	 * @param dataParameterQuery
	 * @return
	 */
	List<DataParameter> findDataParameter(DataParameterQuery dataParameterQuery);

	/**
	 * 根据唯一标识集合查找数据
	 *
	 * @param identifiers
	 * @return
	 */
	List<DataParameter> findByIdentifiers(List<String> identifiers);

	/**
	 *
	 * @param string
	 * @return
	 */
	DataParameter findByIdentifierCache(String string);

	/**
	 *
	 *
	 * @param string
	 */
	void initByIdentifierCache(String string);

	/**
	 *
	 *
	 * @param string
	 * @return
	 */
	DataParameter findByIdentifier(String string);


}
