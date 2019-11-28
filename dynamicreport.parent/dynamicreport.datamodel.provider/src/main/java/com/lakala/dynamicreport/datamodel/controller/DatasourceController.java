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

import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.datamodel.model.DataSource;
import com.lakala.dynamicreport.datamodel.query.DataSourceQuery;
import com.lakala.dynamicreport.datamodel.service.IDataSourceService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 数据源controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/data-source")
@Api(tags = "数据源管理",description = "数据源控制器(DatasourceController)")
public class DatasourceController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DatasourceController.class);

	@Autowired
	private IDataSourceService dataSourceService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("dataSource:query")
	@GetMapping(value = "/list")
	public PageBean<DataSource> list(@ApiParam(name = "query",value = "数据源查询对象",required = true) DataSourceQuery query) {
		PageBean<DataSource> pb = new PageBean<>();
		Page<DataSource> page = dataSourceService.findDatasourceCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "保存数据源")
	@RequiresPermissions("dataSource:save")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "datasource",value = "数据源对象",required = true) DataSource datasource) {
		// 保存
		try {
			ResultJson resultJson = dataSourceService.saveOrUpdateBySave(datasource);
			return resultJson;
		} catch (Exception e) {
			log.error("[DatasourceController : save]" + datasource, e);
			return failure("保存失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除数据源")
	@RequiresPermissions("dataSource:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			DataSource ds=dataSourceService.findOne(id);
			dataSourceService.inactiveDS(ds.getIdentifier());//先删除bean 
			dataSourceService.delete(ds.getId());//物理删除数据
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[DatasourceController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "更新数据源")
	@RequiresPermissions("dataSource:save")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "datasource",value = "数据源对象",required = true) DataSource datasource) {
		try {
			dataSourceService.saveOrUpdateByUpdate(datasource);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[DatasourceController : update]" + datasource, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("dataSource:save")
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "datasource",value = "数据源对象",required = true) DataSource datasource) {
		try {
			if (datasource.getId() != null) {
				return update(datasource);
			} else {
				return save(datasource);
			}
		} catch (Exception e) {
			log.error("[DatasourceController : save_update]" + datasource, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	/**
	 * 查询单条数据
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("dataSource:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public DataSource getOne(@PathVariable Long id) {
		DataSource ds= dataSourceService.findOne(id);
		ds.setPassword("******");
		return ds;
	}
	
	/**
	 * 集群
	 * 根据配置刷新各个节点的数据库实例
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据配置刷新各个节点的数据库实例")
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResultJson refresh(String identifier) {
		try {
			dataSourceService.refreshDSBean(identifier);
		} catch (Exception e) {
			log.error("刷新失败{}",e);
			return failure("刷新失败");
		}
		return success("刷新成功");
		
	}

}
