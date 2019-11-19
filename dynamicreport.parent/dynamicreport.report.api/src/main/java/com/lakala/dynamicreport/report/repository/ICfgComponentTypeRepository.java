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
package com.lakala.dynamicreport.report.repository;

import com.lakala.dynamicreport.report.model.CfgComponentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 报表组件类型查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("cfgComponentTypeRepository")
public interface ICfgComponentTypeRepository extends JpaRepository<CfgComponentType, String>, JpaSpecificationExecutor<CfgComponentType> {

    @EntityGraph(value = "CfgComponentType.Graph", type = EntityGraph.EntityGraphType.FETCH)
    Page<CfgComponentType> findAll(Specification<CfgComponentType> specification, Pageable pageable);

    @EntityGraph(value = "CfgComponentType.Graph", type = EntityGraph.EntityGraphType.FETCH)
    CfgComponentType getOne(String id);

    @Query("select t from CfgComponentType t where t.parent.id is null")
    @EntityGraph(value = "CfgComponentType.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<CfgComponentType> findTopComponentTypes();

    @Query("select t from CfgComponentType t where t.key=?1")
    @EntityGraph(value = "CfgComponentType.Graph", type = EntityGraph.EntityGraphType.FETCH)
    CfgComponentType findByKey(String key);

}