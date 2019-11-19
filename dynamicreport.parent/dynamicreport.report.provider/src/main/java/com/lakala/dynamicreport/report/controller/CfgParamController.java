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
import com.lakala.dynamicreport.report.model.CfgComponentType;
import com.lakala.dynamicreport.report.model.CfgPageParam;
import com.lakala.dynamicreport.report.model.CfgParam;
import com.lakala.dynamicreport.report.query.CfgPageParamQuery;
import com.lakala.dynamicreport.report.query.CfgParamQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentTypeService;
import com.lakala.dynamicreport.report.service.ICfgPageParamService;
import com.lakala.dynamicreport.report.service.ICfgParamService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 报表参数controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/param")
@Api(tags = "报表参数管理",description = "报表参数控制器(CfgParamController)")
public class CfgParamController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CfgParamController.class);

	@Autowired
	private ICfgParamService cfgParamService;
	
	@Autowired
	private ICfgComponentTypeService cfgComponentTypeService;
	
	@Autowired
	private ICfgPageParamService cfgPageParamService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("cfgParam:query")
	@GetMapping(value = "/list")
	public PageBean<CfgParam> list(@ApiParam(name = "query",value = "报表参数查询对象",required = true) CfgParamQuery query) {
		PageBean<CfgParam> pb = new PageBean<>();
		Page<CfgParam> page = cfgParamService.findCfgParamCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}
	
	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("cfgParam:query")
	@GetMapping(value = "/list-all")
	public List<CfgParam> listAll(@ApiParam(name = "query",value = "报表参数查询对象",required = true) CfgParamQuery query) {
		return cfgParamService.findCfgParam(query);
	}
	
	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("cfgParam:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public CfgParam getOne(@PathVariable String id ) {
		return cfgParamService.findOne(id);
	}
	
	@ApiOperation(value = "保存数据")
	@RequiresPermissions("cfgParam:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cfgParam",value = "报表参数对象",required = true) CfgParam cfgParam,
						   HttpServletRequest request) {
		// 保存
		try {
			if(cfgParam.getType()==null||cfgParam.getType().getId()==null){
				cfgParam.setType(null);
			}else{
				CfgComponentType cfgComponentType = cfgComponentTypeService.findOne(cfgParam.getType().getId());
				cfgParam.setType(cfgComponentType);
			}
			cfgParamService.save(cfgParam);
			return success("保存成功");
		} catch (Exception e) {
			log.error("保存失败", e);
			return failure("保存失败" + e.getMessage(),e);
		}
	}
	
	@ApiOperation(value = "删除数据")
	@RequiresPermissions("cfgParam:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "报表参数ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable  String id) {
		try {
			cfgParamService.batchDelete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("刪除失败", e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}
	
	@ApiOperation(value = "更新数据")
	@RequiresPermissions("cfgParam:saveOrUpdate")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cfgParam",value = "报表参数对象",required = true) CfgParam cfgParam,
							 HttpServletRequest request) {
		try {
			CfgParam updateObj=cfgParamService.findOne(cfgParam.getId());
			if(cfgParam.getType()==null||cfgParam.getType().getId()==null){
				updateObj.setType(null);
			}else{
				CfgComponentType cfgComponentType = cfgComponentTypeService.findOne(cfgParam.getType().getId());
				updateObj.setType(cfgComponentType);
			}
			updateObj.setName(cfgParam.getName());
			updateObj.setParamName(cfgParam.getParamName());
			updateObj.setLabel(cfgParam.getLabel());
			updateObj.setLen(cfgParam.getLen());
			updateObj.setFormat(cfgParam.getFormat());
			updateObj.setRequired(cfgParam.getRequired());
			if(cfgParam.getDataList()!=null&&cfgParam.getDataList().getId()!=null){
				updateObj.setDataList(cfgParam.getDataList());
			}else{
				updateObj.setDataList(null);
			}
			
			updateObj.setStatus(cfgParam.getStatus());
			cfgParamService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("更新失败", e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "报表关联多个参数")
	@RequiresPermissions("cfgPage:relateParam")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "报表参数ID",required = true),
	        @ApiImplicitParam(name = "ids",value = "页面参数ids",required = true),
	        @ApiImplicitParam(name = "vos",value = "页面对象字符串",required = true),
	        @ApiImplicitParam(name = "allIds",value = "新的关联页面",required = true)
	})
	@PostMapping(value = "/save-params")
	public ResultJson saveParams(String id, String ids,String vos, String allIds) {
		try {
			cfgPageParamService.save(id, ids,vos, allIds);
			return success("报表关联参数成功");
		} catch (Exception e) {
			log.error("[CfgParamController : saveParams]" + "id:" + id + "ids:" + ids, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询报表关联多个参数")
	@GetMapping(value = "/realte-param")
	public List<CfgParam> queryRealteParam(@ApiParam(name = "cfgParamQuery",value = "报表参数查询对象",required = true) CfgParamQuery cfgParamQuery) {
		cfgParamQuery.setStatus(StatusConstants.ACTIVE);
		List<CfgParam> cfgParams = cfgParamService.findCfgParam(cfgParamQuery);
		// 设置参数是否已选择及参数默认值
		CfgPageParamQuery cfgPageParamQuery = new CfgPageParamQuery();
		cfgPageParamQuery.setPage(cfgParamQuery.getPage());
		List<CfgPageParam> cfgPageParams = cfgPageParamService.findCfgPageParam(cfgPageParamQuery);
		for (CfgParam cfgParam : cfgParams) {
			for (CfgPageParam cfgPageParam : cfgPageParams) {
				if (cfgParam.getId().equals(cfgPageParam.getParam().getId())) {
					cfgParam.setSelected(true);
					cfgParam.setParamValue(cfgPageParam.getParamValue());
					cfgParam.setSequence(cfgPageParam.getSequence());
					break;
				}
			}
		}
		Collections.sort(cfgParams);
		return cfgParams;
	}

}