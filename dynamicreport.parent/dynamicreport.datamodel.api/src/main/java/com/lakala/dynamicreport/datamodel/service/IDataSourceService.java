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

import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.datamodel.model.DataSource;
import com.lakala.dynamicreport.datamodel.query.DataSourceQuery;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * <p>
 * 操作数据源接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IDataSourceService extends IBaseService<DataSource, Long>{

    /**
     * 获取jdbcTemplate
     *
     * @param datasource
     * @return
     */
    JdbcTemplate getJdbcTemplate(DataSource datasource);

    /**
     * 刷新数据源(创建数据源Bean)
     *
     * @param dataSource
     * @return
     */
    void refreshDataSource(DataSource dataSource) throws BusinessException;

    /**
     * 删除数据源的bean
     *
     * @param key
     * @return
     */
    void inactiveDS(String key);


    /**
     * 带条件分页查询
     *
     * @param dataSourceQuery
     * @return
     */
    Page<DataSource> findDatasourceCriteria(DataSourceQuery dataSourceQuery);

    /**
     * 带条件查询
     *
     * @param dataSourceQuery
     * @return
     */
    List<DataSource> findDatasource(DataSourceQuery dataSourceQuery);


    /**
     * springBoot 启动后,寻找启用中的数据源并初始化bean
     *
     */
    void initDataSour();

    /**
     * 更新操作
     *
     * @param dataSource
     * @return
     */
    void saveOrUpdateByUpdate(DataSource dataSource) throws Exception;

    /**
     * 保存操作
     *
     * @param dataSource
     * @return
     */
    ResultJson saveOrUpdateBySave(DataSource dataSource) throws Exception;

    /**
     * 刷新集群各节点的数据源bean
     * @param identifier
     * @throws Exception 
     */
	void refreshDSBean(String identifier) throws Exception;
}
