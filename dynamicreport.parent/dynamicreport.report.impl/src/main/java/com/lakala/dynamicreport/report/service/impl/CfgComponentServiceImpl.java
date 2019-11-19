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
package com.lakala.dynamicreport.report.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.lakala.dynamicreport.datamodel.service.IDataListService;
import com.lakala.dynamicreport.datamodel.service.IDataSourceService;
import com.lakala.dynamicreport.report.model.*;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import com.lakala.dynamicreport.report.query.CfgComponentQuery;
import com.lakala.dynamicreport.report.repository.ICfgComponentColGroupRepository;
import com.lakala.dynamicreport.report.repository.ICfgComponentColRepository;
import com.lakala.dynamicreport.report.repository.ICfgComponentRepository;
import com.lakala.dynamicreport.report.service.ICfgChartTemplateService;
import com.lakala.dynamicreport.report.service.ICfgComponentColService;
import com.lakala.dynamicreport.report.service.ICfgComponentService;
import com.lakala.dynamicreport.report.service.ICfgComponentTypeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lakala.dynamicreport.common.constants.GlobalConstants.DRIVER_HIVE;

/**
 * <p>
 * 报表组件接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "cfgComponentService")
public class CfgComponentServiceImpl extends BaseServiceImpl<ICfgComponentRepository,CfgComponent,java.lang.String> implements ICfgComponentService {

	private Logger log=LoggerFactory.getLogger(CfgComponentServiceImpl.class);
	
	@Autowired
	ICfgComponentRepository cfgComponentRepository;

	@Autowired
	ICfgComponentColRepository cfgComponentColRepository;

	@Autowired
	ICfgComponentColGroupRepository cfgComponentColGroupRepository;

	@Autowired
	private IDataSourceService dataSourceService;

	@Autowired
	private IDataListService dataListService;

	@Autowired
	private ICfgComponentColService cfgComponentColService;

	@Autowired
	private ICfgComponentTypeService cfgComponentTypeService;

	@Autowired
	private ICfgChartTemplateService cfgChartTemplateService;

	private static final String PROVIDER="PROVIDER";


    public List<CfgComponent> findCfgComponent(CfgComponentQuery cfgComponentQuery) {
        return cfgComponentRepository.findAll(specification(cfgComponentQuery), PageForList.getSort("id",cfgComponentQuery));
    }

    public Page<CfgComponent> findCfgComponentCriteria(final CfgComponentQuery cfgComponentQuery) {
        return cfgComponentRepository.findAll(specification(cfgComponentQuery), PageForList.getPageable("modifiedDate",cfgComponentQuery));
    }

	@Transactional
	@Override
	public void saveCfgComponent(CfgComponent cfgComponent,List<CfgComponentCol> cols) {
		cfgComponent = cfgComponentRepository.saveAndFlush(cfgComponent);
		//解除组件和列的关联
		cfgComponentColRepository.deleteByComponentId(cfgComponent.getId());
		if (CollectionUtils.isNotEmpty(cols)) {
			saveCols(cols, cfgComponent);
			for (CfgComponentCol col:cols) {
				cfgComponentColRepository.save(col);
			}
		}
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<CfgComponent> specification(final CfgComponentQuery cfgComponentQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != cfgComponentQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"id",cfgComponentQuery.getId(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentQuery.getName(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"type.id",cfgComponentQuery.getType(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"cfgChartTemplate.id",cfgComponentQuery.getId(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"status",cfgComponentQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.IN,"createdUser",cfgComponentQuery.getUsers(),List.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"versionNo",cfgComponentQuery.getVersionNo(),Integer.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",cfgComponentQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.whereAndOr(list);
	}


    public List<CfgComponent> findByPageId(String pageId) {
        return cfgComponentRepository.findByPageId(pageId);
    }

	@Override
	public List<CfgComponentCol> initColumns(DataList dataList, String newSql) {
		List<CfgComponentCol> columns = new ArrayList<>();
		try {
			String sql = String.format("select subtbl.* from ( %s ) subtbl limit 1", newSql);
			JdbcTemplate jdbcTemplate = dataSourceService.getJdbcTemplate(dataList.getDataSource());
			if (DRIVER_HIVE.equalsIgnoreCase(dataList.getDataSource().getDriverClass())) {// hive库
				List resultList = jdbcTemplate.queryForList(sql);
				if (CollectionUtils.isNotEmpty(resultList)) {
					Map rowMap = (Map) resultList.get(0);
					rowMap.forEach((k, v) -> {
						String colName = k.toString().replace("subtbl.", "");
						CfgComponentCol column = new CfgComponentCol();
						column.setColName(colName);
						column.setName(colName);
						column.setLen("50");
						columns.add(column);
					});
				}
			} else {// 关系型数据库，eg: mysql, oracle
				SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
				SqlRowSetMetaData metaData = rowSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					CfgComponentCol column = new CfgComponentCol();
					column.setColName(metaData.getColumnName(i));
					column.setName(metaData.getColumnName(i));
					column.setLen(String.valueOf(metaData.getPrecision(i)));
					columns.add(column);
				}
			}
		} catch (Exception e) {
			log.error(String.format("数据库查询出错:【%s】", newSql));
			throw new BusinessException(String.format("数据库查询出错:【%s】", newSql));
		}
		return columns;
	}

	@Override
	public ResultJson findColumns(Long dataListId, String id) {
		ResultJson result = new ResultJson();
		result.setSuccess(false);
		// 转换数据集的sql语句
		DataList dataList = dataListService.findOne(dataListId);
		String groupSql = dataListService.transferGroup(dataList.getQuerySql(), PROVIDER);
		Map<String, Object> map = dataListService.transferParams(dataList, groupSql, new HashMap<>(), PROVIDER);
		String newSql = String.valueOf(map.get("sql"));
		// 查询sql语句中的column信息
		List<CfgComponentCol> columns = initColumns(dataList, newSql);

		//以前的数据保留
		CfgComponentColQuery cfgComponentColQuery = new CfgComponentColQuery();
		cfgComponentColQuery.setComponent(id);

		List<CfgComponentCol> oldColumns = cfgComponentColService.findCfgComponentCol(cfgComponentColQuery);

		Map<String,CfgComponentCol> oldMap= Maps.newHashMap();
		Map<String,CfgComponentCol> newMap=Maps.newHashMap();

		for(CfgComponentCol col:oldColumns){
			oldMap.put(col.getColName(),col);
			if (StringUtils.isEmpty(col.getAlias())) {
				col.setAlias(col.getColName());
			}
		}
		for(CfgComponentCol col:columns){
			newMap.put(col.getColName(),col);
			//存在老数据 ,以老数据为准
			if(oldMap.get(col.getColName())!=null){
				BeanUtils.copyProperties(oldMap.get(col.getColName()), col);
			}
			if (StringUtils.isEmpty(col.getAlias())) {
				col.setAlias(col.getColName());
			}
			col.setSelected("Y");
		}

		//老数据 多的  保留
		for(CfgComponentCol col:oldColumns){
			if(newMap.get(col.getColName())==null){
				columns.add(col);
			}
		}

		result.setSuccess(true);
		result.setObj(columns);
		return result;
	}

	@Override
	public CfgComponent getParams(String component,String action) {
		CfgComponent updateObj = new CfgComponent();
		JSONObject jsonObject = JSONObject.parseObject(component);
		// 获取组件参数
		if ("update".equalsIgnoreCase(action)) {//更新
			String id = jsonObject.getString("id");
			updateObj = findOne(id);
		}

		String name = jsonObject.getString("name");
		String typeId = jsonObject.getString("type.id");
		String cfgChartTemplateId = jsonObject.getString("cfgChartTemplate.id");
		Long dataListId = jsonObject.getLong("dataList.id");
		String fixedCol = jsonObject.getString("fixedCol");
		String pagination = jsonObject.getString("pagination");
		String remark = jsonObject.getString("remark");
		String status = jsonObject.getString("status");

		if (StringUtils.isNotBlank(typeId)) {
			CfgComponentType cfgComponentType = cfgComponentTypeService.findOne(typeId);
			updateObj.setType(cfgComponentType);
		} else {
			updateObj.setType(null);
		}
		if (StringUtils.isNotBlank(cfgChartTemplateId)) {
			CfgChartTemplate cfgChartTemplate = cfgChartTemplateService.findOne(cfgChartTemplateId);
			updateObj.setCfgChartTemplate(cfgChartTemplate);
		} else {
			updateObj.setCfgChartTemplate(null);
		}
		if (dataListId != null) {
			DataList dataList = dataListService.findOne(dataListId);
			updateObj.setDataList(dataList);
		} else {
			updateObj.setDataList(null);
		}
		if (org.apache.commons.lang.math.NumberUtils.isNumber(fixedCol)) {
			updateObj.setFixedCol(fixedCol);
		} else {
			updateObj.setFixedCol(null);
		}
		updateObj.setName(name);
		updateObj.setPagination(pagination);
		updateObj.setRemark(remark);
		updateObj.setStatus(status);
		return updateObj;
	}

	private List<CfgComponentCol> saveCols(List<CfgComponentCol> cols, CfgComponent cfgComponent) {
		for (CfgComponentCol cfgComponentCol : cols) {
			if (StringUtils.isBlank(cfgComponentCol.getName())) {
				throw new BusinessException(String.format("更新失败,【%s】中文名必输", cfgComponentCol.getColName()));
				}
				if ("TGT".equals(cfgComponentCol.getType()) && StringUtils.isBlank(cfgComponentCol.getAlias())) {
					throw new BusinessException(String.format("更新失败,【%s】别名必输", cfgComponentCol.getColName()));
			}
			cfgComponentCol.setStatus(StatusConstants.ACTIVE);
			cfgComponentCol.setComponent(cfgComponent);
			if (StringUtils.isNotBlank(cfgComponentCol.getGroupId())) {
				CfgComponentColGroup cfgComponentColGroup = cfgComponentColGroupRepository.getOne(cfgComponentCol.getGroupId());
				cfgComponentCol.setGroup(cfgComponentColGroup);
			} else {
				cfgComponentCol.setGroup(null);
			}
			if(cfgComponentCol != null && cfgComponentCol.getDrillParamList() != null){
				for(CfgColDrillParam cfgColDrillParam:cfgComponentCol.getDrillParamList()){
					cfgColDrillParam.setId(null);
				}
			}
		}
		return cols;
	}

}