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
package com.lakala.dynamicreport.datamodel.controller;

import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.datamodel.model.DataList;
import com.lakala.dynamicreport.datamodel.model.DataSource;
import com.lakala.dynamicreport.datamodel.query.DataListQuery;
import com.lakala.dynamicreport.datamodel.query.DataSourceQuery;
import com.lakala.dynamicreport.datamodel.service.IDataListService;
import com.lakala.dynamicreport.datamodel.service.IDataSourceService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据集controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/data-list")
@Api(tags = "数据集管理",description = "数据集控制器(DataListController)")
public class DataListController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DataListController.class);

	@Autowired 
	private IDataListService dataListService;

	@Autowired
	private IDataSourceService dataSourceService;

	@Autowired
	private ISystemUserService systemUserService;

	private static final String PROVIDER="PROVIDER";

	@ApiOperation(value = "分页")
	@RequiresPermissions("dataList:query")
	@GetMapping(value = "/list")
	public PageBean<DataList> list(@ApiParam(name = "query",value = "数据集查询对象") DataListQuery query) {
		PageBean<DataList> pb = new PageBean<>();
		query.setUsers(systemUserService.getAllSameReportRoleUsers());
		Page<DataList> page = dataListService.findDataListCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}
	
	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("dataList:query")
	@GetMapping(value = "/list-all")
	public List<DataList> listAll(@ApiParam(name = "query",value = "数据集查询对象",required = true) DataListQuery query) {
        query.setUsers(systemUserService.getAllSameReportRoleUsers());
		return dataListService.findDataList(query);
	}

	@ApiOperation(value = "刪除数据集合")
	@RequiresPermissions("dataList:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			dataListService.delete(id);//物理删除数据
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[DataListController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("dataList:save")
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "dataList",value = "数据集对象",required = true) DataList dataList) {
		try {
			ResultJson resultJson = dataListService.saveOrUpdate(dataList);
			return resultJson;
		} catch (Exception e) {
			log.error("[DataListController : save_update]" + dataList, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("dataList:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public DataList getOne(@PathVariable Long id) {
		return dataListService.findOne(id);
	}

	@ApiOperation(value = "获取可用的datasource")
	@GetMapping(value = "/data-sources")
	public List<DataSource> getDatasource() {
		DataSourceQuery dsQ=new DataSourceQuery();
		dsQ.setStatus(StatusConstants.ACTIVE);
		return dataSourceService.findDatasource(dsQ);
	}

	@ApiOperation(value = "sql测试")
	@GetMapping(value = "/sql-test")
	public ResultJson querySqlTest(@ApiParam(name = "dataList",value = "数据集对象",required = true) DataList dataList) {
		try {
			ResultJson resultJson = dataListService.querySqlTest(dataList);
			return resultJson;
		} catch (Exception e) {
			log.error("[DataListController : sql_test] {},{}" ,dataList.getQuerySql(), e.getMessage());
			return failure(e.getMessage());
		}
	}

	@ApiOperation(value = "转换sql组")
	@PutMapping(value = "/transfer-sql")
	public ResultJson changSql(@ApiParam(name = "dataList",value = "数据集对象",required = true) DataList dataList) {
		String sql=dataList.getQuerySql();
		if(StringUtils.isEmpty(sql)){
			return failure("请输入查询SQL");
		}
		//处理sql Description 借用前段传标识
		if("CHANG_GROUP".equalsIgnoreCase(dataList.getDescription())){
			sql=dataListService.transferGroup(sql,PROVIDER);
		}else{
			sql=dataListService.transferGroup(sql,PROVIDER);
			Map<String,Object> map=dataListService.transferParams(dataList, sql,new HashMap<String, Object>(),PROVIDER);
			sql=String.valueOf(map.get("sql"));
		}
		return success(sql);
	}
}
