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
package com.lakala.dynamicreport.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.utils.StrUtils;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.query.SytemFunctionQuery;
import com.lakala.dynamicreport.system.service.IRoleService;
import com.lakala.dynamicreport.system.service.ISytemFunctionService;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统功能controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/system-funtion")
@Api(tags = "系统功能管理",description = "系统功能控制器(SystemFunctionController)")
public class SystemFunctionController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(SystemFunctionController.class);

	@Autowired
	private ISytemFunctionService functionService;
	
	@Autowired
	private IRoleService roleService;
	
	@ApiOperation(value = "分页")
	@RequiresPermissions("system_funtion:query")
	@GetMapping(value = "/list")
	public PageBean<SystemFunction> list(@ApiParam(name = "query",value = "系统功能查询对象",required = true) SytemFunctionQuery query) {
		PageBean<SystemFunction> pb = new PageBean<>();
		Page<SystemFunction> page = functionService.findParameterCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("system_funtion:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "action",value = "操作行为",required = true),
	        @ApiImplicitParam(name = "pkId",value = "功能ID",required = true),
	        @ApiImplicitParam(name = "parentId",value = "父类ID",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "function",value = "系统功能对象",required = true) SystemFunction function,
								   String action, String pkId, String parentId) {
		try {
			if ("update".equals(action)) {
				return update(function, pkId, parentId);
			} else {
				return save(function, parentId);
			}
		} catch (Exception e) {
			log.error("[SystemFunctionController : save_update]" + function, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "保存参数")
	@RequiresPermissions("system_funtion:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "parentId",value = "父类ID",required = true)
	})
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "function",value = "系统功能对象",required = true) SystemFunction function,
						   String parentId) {
 		try {
			SystemFunction functionsObj = functionService.findOne(function.getId());
			if (functionsObj != null) {
 				log.error(GlobalConstants.BRACKETS_2,"系统功能ID重复",function.getId());
				return failure("保存失败" + "系统功能ID重复!!");
			}
			
			if (!StrUtils.isEmpty(parentId)) {
				SystemFunction parent=new SystemFunction();
				parent.setId(parentId);
				function.setParent(parent);
			}
			function.setStatus(StatusConstants.ACTIVE);
			functionService.save(function);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[SystemFunctionController : save]" + function, e);
			return failure("保存失败" + e.getMessage());
		}
	}
 
	@ApiOperation(value = "更新参数")
	@RequiresPermissions("system_funtion:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkId",value = "功能ID",required = true),
	        @ApiImplicitParam(name = "parentId",value = "父类ID",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "function",value = "系统功能对象",required = true) SystemFunction function,
							 String pkId, String parentId) {
		try {
			SystemFunction functionsObj = functionService.findOne(pkId);
			functionsObj.setName(function.getName());
			functionsObj.setParent(function.getParent());
			functionsObj.setDescription(function.getDescription());
			if (!StrUtils.isEmpty(parentId)) {
				SystemFunction parent=new SystemFunction();
				parent.setId(parentId);
				functionsObj.setParent(parent);
			}else{
				functionsObj.setParent(null);
			}
			functionsObj.setSequence(function.getSequence());
			functionsObj.setType(function.getType());
  			functionService.update(functionsObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[SystemFunctionController : update]" + function, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除")
	@RequiresPermissions("system_funtion:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkId",value = "功能ID",paramType = "path",required = true)
	})
	@DeleteMapping(value = "/{pkId}")
	public ResultJson delete(@ApiIgnore SystemFunction function,@PathVariable String pkId) {
		try {
			function.setId(pkId);
			functionService.delete(function.getId());
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[SystemFunctionController : delete]" + function, e);
			return failure("刪除失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "查询单条数据")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "功能ID",paramType = "path",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public SystemFunction getOne(@PathVariable String id) {
		return functionService.findOne(id);
	}

	@ApiOperation(value = "查找所有功能树形菜单")
	@RequiresPermissions("system_funtion:query")
	@GetMapping(value = "/list-all")
	public  List<TreeNode> findAll(@ApiParam(name = "functionQuery",value = "系统功能查询对象",required = true) SytemFunctionQuery functionQuery) {
 		List<TreeNode> treeNode=new ArrayList<>();
		try {
  			List<SystemFunction> functionInfo = functionService.findParent(functionQuery);
  			treeNode=functionService.getTree(functionInfo,null);
 		} catch (Exception e) {
			log.error("[SystemFunctionController : list_all]" + functionQuery, e);
		}
		return treeNode;
	}
    
	@ApiOperation(value = "获取功能的树形菜单")
	@RequiresPermissions("system_funtion:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "功能ID",dataType="Long",required = true)
	})
	@GetMapping(value = "/functions")
	public List<TreeNode> getRules(@ApiParam(name = "functionQuery",value = "系统功能查询对象",required = true) SytemFunctionQuery functionQuery,
								   Long id) {
		Role model = roleService.findOne(id);
		List<SystemFunction> systemFunctionList = model.getSystemFunctions();
		List<String> functionIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(systemFunctionList)) {
			for (SystemFunction systemFunction : systemFunctionList) {
				functionIds.add(systemFunction.getId());
			}
		}
 		List<SystemFunction> functionInfo = functionService.findParent(functionQuery);
 		return functionService.getTree(functionInfo,functionIds);
 	}

}
