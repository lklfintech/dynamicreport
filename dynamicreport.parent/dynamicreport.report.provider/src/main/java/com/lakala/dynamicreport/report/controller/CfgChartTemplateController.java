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
import com.lakala.dynamicreport.report.model.CfgChartTemplate;
import com.lakala.dynamicreport.report.query.CfgChartTemplateQuery;
import com.lakala.dynamicreport.report.service.ICfgChartTemplateService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
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
 * 报表图形模板controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/chart-template")
@Api(tags = "报表图形模板管理",description = "报表图形模板控制器(CfgChartTemplateController)")
public class CfgChartTemplateController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CfgChartTemplateController.class);

	@Autowired
	private ICfgChartTemplateService cfgChartTemplateService;

	@Autowired
	ISystemUserService systemUserService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("cfgChartTemplate:query")
	@GetMapping(value = "/list")
	public PageBean<CfgChartTemplate> list(@ApiParam(name = "query",value = "图形模板查询对象",required = true) CfgChartTemplateQuery query) {
		PageBean<CfgChartTemplate> pb = new PageBean<>();
		Page<CfgChartTemplate> page = cfgChartTemplateService.findCfgChartTemplateCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}
	
	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("cfgChartTemplate:query")
	@GetMapping(value = "/list-all")
	public List<CfgChartTemplate> listAll(@ApiParam(name = "query",value = "图形模板查询对象",required = true) CfgChartTemplateQuery query) {
		return cfgChartTemplateService.findCfgChartTemplate(query);
	}
	
	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("cfgChartTemplate:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public CfgChartTemplate getOne(@PathVariable String id) {
		return cfgChartTemplateService.findOne(id);
	}
	
	@ApiOperation(value = "保存数据")
	@RequiresPermissions("cfgChartTemplate:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cfgChartTemplate",value = "图形模板对象",required = true) CfgChartTemplate cfgChartTemplate,
						   HttpServletRequest request) {
		// 保存
		try {
			if(StringUtils.isEmpty(cfgChartTemplate.getContent())){
				return failure("模板内容不能为空！");
			}
			cfgChartTemplateService.save(cfgChartTemplate);
			return success("保存成功");
		} catch (Exception e) {
			log.error("保存失败", e);
			return failure("保存失败" + e.getMessage(),e);
		}
	}
	
	@ApiOperation(value = "删除数据")
	@RequiresPermissions("cfgChartTemplate:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "图形模板ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable String id) {
		try {
			cfgChartTemplateService.batchDelete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("刪除失败", e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}
	
	@ApiOperation(value = "更新数据")
	@RequiresPermissions("cfgChartTemplate:saveOrUpdate")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cfgChartTemplate",value = "图形模板对象",required = true) CfgChartTemplate cfgChartTemplate,
							 HttpServletRequest request) {
		try {
			CfgChartTemplate updateObj=cfgChartTemplateService.findOne(cfgChartTemplate.getId());
			updateObj.setName(cfgChartTemplate.getName());
			updateObj.setContent(cfgChartTemplate.getContent());
			updateObj.setStatus(cfgChartTemplate.getStatus());
			cfgChartTemplateService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("更新失败", e);
			return failure("更新失败" + e.getMessage());
		}
	}
}