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
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.model.CfgPageComponent;
import com.lakala.dynamicreport.report.query.CfgPageComponentQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentService;
import com.lakala.dynamicreport.report.service.ICfgPageComponentService;
import com.lakala.dynamicreport.report.service.ICfgPageService;
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
 *	报表页面组件controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */

@RestController
@RequestMapping("/cfg/page-component")
@Api(tags = "报表页面组件管理",description = "报表页面组件控制器(CfgPageComponentController)")
public class CfgPageComponentController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CfgPageComponentController.class);

	@Autowired
	private ICfgPageComponentService cfgPageComponentService;

	@Autowired
	private ICfgPageService cfgPageService;
	@Autowired
	private ICfgComponentService cfgComponentService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("cfgPageComponent:query")
	@GetMapping(value = "/list")
	public PageBean<CfgPageComponent> list(@ApiParam(name = "query",value = "页面组件查询对象",required = true) CfgPageComponentQuery query) {
		PageBean<CfgPageComponent> pb = new PageBean<>();
		Page<CfgPageComponent> page = cfgPageComponentService.findCfgPageComponentCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "查询所有数据")
	@GetMapping(value = "/list-all")
	public List<CfgPageComponent> listAll(@ApiParam(name = "query",value = "页面组件查询对象",required = true) CfgPageComponentQuery query) {
		return cfgPageComponentService.findCfgPageComponent(query);
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("cfgPageComponent:query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
    })
	@GetMapping(value = "/list/{id}")
	public CfgPageComponent getOne(@PathVariable String id) {
		return cfgPageComponentService.findOne(id);
	}

	@ApiOperation(value = "保存数据")
	@RequiresPermissions("cfgPageComponent:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cfgPageComponent",value = "页面组件对象",required = true) CfgPageComponent cfgPageComponent,
						   HttpServletRequest request) {
		// 保存
		try {
			if(cfgPageComponent.getPage()==null||cfgPageComponent.getPage().getId()==null){
				cfgPageComponent.setPage(null);
			}else{
				CfgPage cfgPage = cfgPageService.findOne(cfgPageComponent.getPage().getId());
				cfgPageComponent.setPage(cfgPage);
			}
			if(cfgPageComponent.getComponent()==null||cfgPageComponent.getComponent().getId()==null){
				cfgPageComponent.setComponent(null);
			}else{
				CfgComponent cfgComponent = cfgComponentService.findOne(cfgPageComponent.getComponent().getId());
				cfgPageComponent.setComponent(cfgComponent);
			}
			cfgPageComponent.setStatus(StatusConstants.ACTIVE);
			cfgPageComponentService.save(cfgPageComponent);
			return success("保存成功");
		} catch (Exception e) {
			log.error("保存失败", e);
			return failure("保存失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "删除数据")
	@RequiresPermissions("cfgPageComponent:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "页面组件ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable String id) {
		try {
			cfgPageComponentService.batchDelete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("刪除失败", e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "更新数据")
	@RequiresPermissions("cfgPageComponent:saveOrUpdate")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cfgPageComponent",value = "页面组件对象",required = true) CfgPageComponent cfgPageComponent,
							 HttpServletRequest request) {
		try {
			CfgPageComponent updateObj=cfgPageComponentService.findOne(cfgPageComponent.getId());
			if(cfgPageComponent.getPage()==null||cfgPageComponent.getPage().getId()==null){
				updateObj.setPage(null);
			}else{
				CfgPage cfgPage = cfgPageService.findOne(cfgPageComponent.getPage().getId());
				updateObj.setPage(cfgPage);
			}
			if(cfgPageComponent.getComponent()==null||cfgPageComponent.getComponent().getId()==null){
				updateObj.setComponent(null);
			}else{
				CfgComponent cfgComponent = cfgComponentService.findOne(cfgPageComponent.getComponent().getId());
				updateObj.setComponent(cfgComponent);
			}
			updateObj.setSequence(cfgPageComponent.getSequence());
			cfgPageComponentService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("更新失败", e);
			return failure("更新失败" + e.getMessage());
		}
	}
}