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

import com.lakala.dynamicreport.common.constants.StatusConstants;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lakala.dynamicreport.report.model.CfgComponentCol;

import java.util.List;

/**
 * <p>
 * 报表组件列查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("cfgComponentColRepository")
public interface ICfgComponentColRepository extends JpaRepository<CfgComponentCol, java.lang.String>, JpaSpecificationExecutor<CfgComponentCol> {

    @EntityGraph(value = "CfgComponentCol.Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<CfgComponentCol> findAll(Specification<CfgComponentCol> var1, Sort var2);

    @EntityGraph(value = "CfgComponentCol.Graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select t from CfgComponentCol t where t.id in ?1 and t.status='" + StatusConstants.ACTIVE + "' order by t.sequence asc")
    List<CfgComponentCol> findByIds(List<String> ids);

    @Modifying
    @Query(value = "delete from rpt_cfg_component_col where cc_component =?1", nativeQuery = true)
    void deleteByComponentId(String id);

    @EntityGraph(value = "CfgComponentCol.Graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select t from CfgComponentCol t where t.component.id = ?1 and t.status='" + StatusConstants.ACTIVE + "'")
    List<CfgComponentCol> findByComponentId(String id);
}