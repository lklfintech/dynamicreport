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
import com.lakala.dynamicreport.report.model.CfgParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 报表参数查询库
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Repository("cfgParamRepository")
public interface ICfgParamRepository extends JpaRepository<CfgParam, String>, JpaSpecificationExecutor<CfgParam> {

    @Query("select p from CfgParam p, CfgPageParam pp where p.id = pp.param.id and pp.page.id =?1 and p.status='" + StatusConstants.ACTIVE + "'")
    List<CfgParam> findByPageId(String pageId);

}