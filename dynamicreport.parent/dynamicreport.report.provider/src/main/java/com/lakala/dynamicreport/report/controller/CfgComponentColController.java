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
package com.lakala.dynamicreport.report.controller;

import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.report.model.CfgComponent;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.model.CfgComponentColGroup;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentColGroupService;
import com.lakala.dynamicreport.report.service.ICfgComponentColService;
import com.lakala.dynamicreport.report.service.ICfgComponentService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 报表组件列controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/component-col")
@Api(tags = "报表组件列管理",description = "报表组件列控制器(CfgComponentColController)")
public class CfgComponentColController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CfgComponentColController.class);

	@Autowired
	private ICfgComponentColService cfgComponentColService;

	@Autowired
	private ICfgComponentColGroupService cfgComponentColGroupService;

	@Autowired
	private ICfgComponentService cfgComponentService;

	@Autowired
	private ISystemUserService systemUserService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("cfgComponentCol:query")
	@GetMapping(value = "/list")
	public PageBean<CfgComponentCol> list(@ApiParam(name = "query",value = "组件列查询对象",required = true) CfgComponentColQuery query) {
		PageBean<CfgComponentCol> pb = new PageBean<>();
		query.setUsers(systemUserService.getAllSameReportRoleUsers());
		Page<CfgComponentCol> page = cfgComponentColService.findCfgComponentColCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("cfgComponentCol:query")
	@GetMapping(value = "/list-all")
	public List<CfgComponentCol> listAll(@ApiParam(name = "query",value = "组件列查询对象",required = true) CfgComponentColQuery query) {
		return cfgComponentColService.findCfgComponentCol(query);
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("cfgComponentCol:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public CfgComponentCol getOne(@PathVariable String id) {
		return cfgComponentColService.findOne(id);
	}

	@ApiOperation(value = "保存数据")
	@RequiresPermissions("cfgComponentCol:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cfgComponentCol",value = "组件列对象",required = true) CfgComponentCol cfgComponentCol,
						   HttpServletRequest request) {
		// 保存
		try {
			if(cfgComponentCol.getGroup()==null||cfgComponentCol.getGroup().getId()==null){
				cfgComponentCol.setGroup(null);
			}else{
				CfgComponentColGroup cfgComponentColGroup = cfgComponentColGroupService.findOne(cfgComponentCol.getGroup().getId());
				cfgComponentCol.setGroup(cfgComponentColGroup);
			}
			if(cfgComponentCol.getComponent()==null||cfgComponentCol.getComponent().getId()==null){
				cfgComponentCol.setComponent(null);
			}else{
				CfgComponent cfgComponent = cfgComponentService.findOne(cfgComponentCol.getComponent().getId());
				cfgComponentCol.setComponent(cfgComponent);
			}
			cfgComponentCol.setStatus(StatusConstants.ACTIVE);
			cfgComponentColService.save(cfgComponentCol);
			return success("保存成功");
		} catch (Exception e) {
			log.error("保存失败", e);
			return failure("保存失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "删除数据")
	@RequiresPermissions("cfgComponentCol:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "组件列ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable  String id) {
		try {
			cfgComponentColService.batchDelete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("刪除失败", e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "更新数据")
	@RequiresPermissions("cfgComponentCol:saveOrUpdate")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cfgComponentCol",value = "组件列对象",required = true) CfgComponentCol cfgComponentCol,
							 HttpServletRequest request) {
		try {
			CfgComponentCol updateObj=cfgComponentColService.findOne(cfgComponentCol.getId());
			if(cfgComponentCol.getGroup()==null||cfgComponentCol.getGroup().getId()==null){
				updateObj.setGroup(null);
			}else{
				CfgComponentColGroup cfgComponentColGroup = cfgComponentColGroupService.findOne(cfgComponentCol.getGroup().getId());
				updateObj.setGroup(cfgComponentColGroup);
			}
			if(cfgComponentCol.getComponent()==null||cfgComponentCol.getComponent().getId()==null){
				updateObj.setComponent(null);
			}else{
				CfgComponent cfgComponent = cfgComponentService.findOne(cfgComponentCol.getComponent().getId());
				updateObj.setComponent(cfgComponent);
			}
			updateObj.setType(cfgComponentCol.getType());
			updateObj.setName(cfgComponentCol.getName());
			updateObj.setColName(cfgComponentCol.getColName());
			updateObj.setLen(cfgComponentCol.getLen());
			updateObj.setSelected(cfgComponentCol.getSelected());
			updateObj.setRowMerge(cfgComponentCol.getRowMerge());
			updateObj.setSequence(cfgComponentCol.getSequence());
			updateObj.setStatus(cfgComponentCol.getStatus());
			cfgComponentColService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("更新失败", e);
			return failure("更新失败" + e.getMessage());
		}
	}
}