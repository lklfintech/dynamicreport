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

import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.report.model.CfgComponentColGroup;
import com.lakala.dynamicreport.report.query.CfgComponentColGroupQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentColGroupService;
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
 * 报表组件列组controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/component-col-group")
@Api(tags = "报表组件列组管理",description = "报表组件列组控制器(CfgComponentColGroupController)")
public class CfgComponentColGroupController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CfgComponentColGroupController.class);

	@Autowired
	private ICfgComponentColGroupService cfgComponentColGroupService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("cfgComponentColGroup:query")
	@GetMapping(value = "/list")
	public PageBean<CfgComponentColGroup> list(@ApiParam(name = "query",value = "组件列组查询对象",required = true) CfgComponentColGroupQuery query) {
		PageBean<CfgComponentColGroup> pb = new PageBean<>();
		Page<CfgComponentColGroup> page = cfgComponentColGroupService.findCfgComponentColGroupCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}
	
	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("cfgComponentColGroup:query")
	@GetMapping(value = "/list-all")
	public List<CfgComponentColGroup> listAll(@ApiParam(name = "query",value = "组件列组查询对象",required = true) CfgComponentColGroupQuery query) {
		return cfgComponentColGroupService.findCfgComponentColGroup(query);
	}
	
	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("cfgComponentColGroup:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public CfgComponentColGroup getOne(@PathVariable String id) {
		return cfgComponentColGroupService.findOne(id);
	}
	
	@ApiOperation(value = "保存数据")
	@RequiresPermissions("cfgComponentColGroup:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cfgComponentColGroup",value = "组件列组对象",required = true) CfgComponentColGroup cfgComponentColGroup,
						   HttpServletRequest request) {
		// 保存
		try {
			cfgComponentColGroupService.save(cfgComponentColGroup);
			return success("保存成功");
		} catch (Exception e) {
			log.error("保存失败", e);
			return failure("保存失败" + e.getMessage(),e);
		}
	}
	
	@ApiOperation(value = "删除数据")
	@RequiresPermissions("cfgComponentColGroup:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "组件列组ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable String id) {
		try {
			cfgComponentColGroupService.batchDelete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("刪除失败", e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}
	
	@ApiOperation(value = "更新数据")
	@RequiresPermissions("cfgComponentColGroup:saveOrUpdate")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cfgComponentColGroup",value = "组件列组对象",required = true) CfgComponentColGroup cfgComponentColGroup,
							 HttpServletRequest request) {
		try {
			CfgComponentColGroup updateObj=cfgComponentColGroupService.findOne(cfgComponentColGroup.getId());
			updateObj.setName(cfgComponentColGroup.getName());
			updateObj.setDisplay(cfgComponentColGroup.getDisplay());
			updateObj.setStatus(cfgComponentColGroup.getStatus());
			cfgComponentColGroupService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("更新失败", e);
			return failure("更新失败" + e.getMessage());
		}
	}
}