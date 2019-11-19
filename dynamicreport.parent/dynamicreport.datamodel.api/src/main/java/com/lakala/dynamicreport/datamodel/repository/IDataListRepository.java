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
package com.lakala.dynamicreport.datamodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lakala.dynamicreport.datamodel.model.DataList;

/**
 * <p>
 * 数据集查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("dataListRepository")
public interface IDataListRepository extends JpaRepository<DataList, Long>,
		JpaSpecificationExecutor<DataList> {

 	@Query("select p from DataList p where p.identifier = ?1")
 	DataList findByIdentify(String identifier);
	
}
