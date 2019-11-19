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
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lakala.dynamicreport.report.model.CfgComponent;

import java.util.List;

/**
 * <p>
 * 报表组件查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("cfgComponentRepository")
public interface ICfgComponentRepository extends JpaRepository<CfgComponent,java.lang.String>, JpaSpecificationExecutor<CfgComponent> {

    @EntityGraph(value = "CfgComponent.Graph", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select c from CfgComponent c, CfgPageComponent pc where c.id = pc.component.id and pc.page.id = ?1 and c.status='" + StatusConstants.ACTIVE + "' order by pc.sequence asc")
    List<CfgComponent> findByPageId(String pageId);

}