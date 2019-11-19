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

import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.system.model.Menu;
import com.lakala.dynamicreport.system.query.MenuQuery;
import com.lakala.dynamicreport.system.service.IMenuService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理",description = "菜单控制器(MenuController)")
public class MenuController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private IMenuService menuService;

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("menu:saveOrUpdate")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "action",value = "操作行为",defaultValue = "save",required = true),
			@ApiImplicitParam(name = "parentId",value = "父类ID",dataType = "Long",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "menu",value = "菜单",required = true) Menu menu, String action, Long parentId) {
		try {
			if ("update".equals(action)) {
				return update(menu, parentId);
			} else {
				return save(menu, parentId);
			}
		} catch (Exception e) {
			log.error("[FunctionController : saveOrUpdate]" + menu, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "保存")
	@RequiresPermissions("menu:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "parentId",value = "父类ID",dataType = "Long",required = true)
	})
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "menu",value = "菜单对象",required = true) Menu menu, Long parentId) {
 		try {
 			//NULL时菜单页面不能正常显示，默认图标
 			if(StringUtils.isEmpty(menu.getIcon())){
 				menu.setIcon("");
 			}
			menu.setStatus(StatusConstants.ACTIVE);
			menuService.save(menu);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[FunctionController : save]" + menu, e);
			return failure("保存失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "更新")
	@RequiresPermissions("menu:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "parentId",value = "父类ID",dataType="Long",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "menu",value = "菜单对象",required = true) Menu menu, Long parentId) {
		try {
			Menu functionsObj = menuService.findOne(menu.getId());
			functionsObj.setName(menu.getName());
			functionsObj.setParent(menu.getParent());
			functionsObj.setDescription(menu.getDescription());
			if (parentId != null) {
				Menu parent = new Menu();
				parent.setId(parentId);
				functionsObj.setParent(parent);
			}
			functionsObj.setSystemFunction(menu.getSystemFunction());
			functionsObj.setIcon(menu.getIcon());
			functionsObj.setPath(menu.getPath());
			functionsObj.setSequence(menu.getSequence());
			//NULL时菜单页面不能正常显示，默认图标
 			if(StringUtils.isEmpty(menu.getIcon())){
 				functionsObj.setIcon("");
 			}
			menuService.update(functionsObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[DecisionController : update]" + menu, e);
			return failure("更新失败" + e.getMessage());
		}
	}
 
	@ApiOperation(value = "刪除")
	@RequiresPermissions("menu:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkId",value = "菜单ID",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{pkId}")
	public ResultJson delete(@ApiIgnore Menu menu,@PathVariable Long pkId) {
		try {
			menu.setId(pkId);
			menuService.delete(menu.getId());
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[ParameterController : delete]" + menu, e);
			return failure("刪除失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("menu:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "菜单ID",paramType = "path",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public Menu getOne(@PathVariable String id) {
		return menuService.findOne(Long.valueOf(id));
	}

	@ApiOperation(value = "查找所有功能树形菜单")
	@RequiresPermissions("menu:query")
	@GetMapping(value = "/tree-node-list")
	public List<TreeNode> findAll(@ApiParam(name = "menuQuery",value = "菜单查询对象",required = true) MenuQuery menuQuery) {
		List<TreeNode> treeNode = new ArrayList<>();
		try {
			List<Menu> menu = menuService.findParent(menuQuery);
			treeNode = menuService.getTree(menu, null);
		} catch (Exception e) {
			log.error("[MenuController : tree-node-list]" + menuQuery, e);
		}
		return treeNode;
	
	}
	
	@ApiOperation(value = "查找所有非报表功能树形菜单")
	@RequiresPermissions("menu:query")
	@GetMapping(value = "/without-report")
	public List<TreeNode> findWithOutReport(@ApiParam(name = "menuQuery",value = "菜单查询对象",required = true) MenuQuery menuQuery) {
		List<TreeNode> treeNode = new ArrayList<>();
		try {
			List<Menu> menu = menuService.findParent(menuQuery);
			treeNode = menuService.getTreeWithOutReport(menu, null);
		} catch (Exception e) {
			log.error("[MenuController : without-report]" + menuQuery, e);
		}
		return treeNode;
	
	}
	
	@ApiOperation(value = "查找用户所有功能树形菜单")
	@GetMapping(value = "/user-menu")
	public List<TreeNode> findUserMenu() {
		List<TreeNode> trees = new ArrayList<>();
		try {
			// 从session获取用户信息
			trees =menuService.findUserMenu();
		} catch (Exception e) {
			log.error("[MenuController : user-menu]", e);
		}
		return trees;
	}

}
