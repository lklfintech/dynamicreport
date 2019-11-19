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

import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.lakala.dynamicreport.datamodel.model.DataParameter;
import com.lakala.dynamicreport.datamodel.model.DataParameterGroup;
import com.lakala.dynamicreport.datamodel.query.DataListQuery;
import com.lakala.dynamicreport.datamodel.query.ReportDataQuery;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 操作数据集接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IDataListService extends IBaseService<DataList, Long>{


    /**
     * 带条件分页查询
     *
     * @param dataListQuery
     * @return
     */
    Page<DataList> findDataListCriteria(DataListQuery dataListQuery);

    /**
     * 带条件查询
     *
     * @param dataListQuery
     * @return
     */
    List<DataList> findDataList(DataListQuery dataListQuery);

    /**
     * 标识获取缓存数据
     *
     * @param identifier
     * @return
     */
    DataList findByIdentifierCache(String identifier);

    /**
     * 删除缓存
     *
     * @param identifier
     */
    void initByIdentifierCache(String identifier);

    /**
     * 根据标识查询
     *
     * @param identifier
     * @return
     */
    DataList findByIdentifier(String identifier);

    /**
     * 用于特征工程查询
     *
     * @param dl
     * @param parMap
     * @return
     * @throws UnsupportedEncodingException
     */
    List<Map<String, Object>> queryForFeature(DataList dl, Map<String, Object> parMap)
            throws UnsupportedEncodingException;

    /**
     * 针对报表多数据源查询
     *
     * @param reportDataQuery
     * @return
     */
    List<Map<String, Object>> queryForReport(ReportDataQuery reportDataQuery);

    /**
     * 报表查询分页数据
     *
     * @param reportDataQuery
     * @return
     */
    PageBean<Map<String, Object>> queryForReportPage(ReportDataQuery reportDataQuery);


    /**
     * SQL 替换参数组
     *
     * @param sql
     * @param source
     * @return
     */
    String transferGroup(String sql, String source);

    /**
     * 替换参数
     *
     * @param dataList
     * @param sql
     * @param source
     * @param map
     * @return
     */
    Map<String, Object> transferParams(DataList dataList, String sql, Map<String, Object> map, String source);

    /**
     * 正则获取参数Identifier
     *
     * @param content
     * @return
     */
    List<String> getSqlParamList(String content);
    
    /**
	 * 获取参数组,从库或者本地缓存,或者reids缓存
     *
	 * @param source
	 * @param groupIdentifier
	 * @return
	 */
	DataParameterGroup findDataParameterGroup(String source,String groupIdentifier);

	/**
	 * 初始化redis参数组
     *
	 * @param groupIdentifier
	 * @return
	 */
	void initGroupByIdentifierCache(String groupIdentifier);
	
	
	/**
	 * 获取参数,从库或者本地缓存,或者reids缓存
     *
	 * @param source
	 * @param paramIdentifier
	 * @return
	 */
	DataParameter findDataParameter(String source, String paramIdentifier);

	/**
	 * 初始化redis缓存 的参数
     *
	 * @param paramIdentifier
	 */
	void initDataParameterCache(String paramIdentifier);

    /**
     * 更新或保存
     *
     * @param dataList
     * @return
     */
    ResultJson saveOrUpdate(DataList dataList);

    /**
     * 转换sql组
     *
     * @param dataList
     * @return
     */
    ResultJson querySqlTest(DataList dataList);
}
