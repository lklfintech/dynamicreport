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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lakala.dynamicreport.report.model.CfgComponentColGroup;

import java.util.List;

/**
 * <p>
 * 报表组件列组查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("cfgComponentColGroupRepository")
public interface ICfgComponentColGroupRepository extends JpaRepository<CfgComponentColGroup, String>, JpaSpecificationExecutor<CfgComponentColGroup> {

    @Query("select tg from CfgComponentColGroup tg " +
            "where tg.id in (select t.group.id from CfgComponentCol t where t.component.id = ?1) " +
            "and tg.status='" + StatusConstants.ACTIVE + "'")
    List<CfgComponentColGroup> findByComponentId(String componentId);

    @Query("select tg from CfgComponentColGroup tg where tg.name = ?1 and tg.status='" + StatusConstants.ACTIVE + "'")
    CfgComponentColGroup findByName(String name);
}