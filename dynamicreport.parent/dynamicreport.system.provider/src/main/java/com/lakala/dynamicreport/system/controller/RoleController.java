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
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.RoleQuery;
import com.lakala.dynamicreport.system.service.IRoleFunctionService;
import com.lakala.dynamicreport.system.service.IRoleService;
import com.lakala.dynamicreport.system.service.IUserRoleService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理",description = "角色控制器(RoleController)")
public class RoleController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IRoleFunctionService roleFunctionService;


	@Autowired
	private IUserRoleService userRoleService;
	
	@ApiOperation(value = "分页")
	@RequiresPermissions("role:query")
	@GetMapping(value = "/list")
 	public PageBean<Role> list(@ApiParam(name = "query",value = "角色查询对象") RoleQuery query) {
		PageBean<Role> pb = new PageBean<>();
		Page<Role> page = roleService.findRoleCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("role:query")
	@GetMapping(value = "/list-all")
	public List<Role> listAll(@ApiParam(name = "query",value = "角色查询对象",required =true) RoleQuery query) {
		return roleService.findRole(query);
	}

	@ApiOperation(value = "保存角色")
	@RequiresPermissions("role:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "role",value = "角色对象") Role role) {
 		try {
			role.setStatus(StatusConstants.ACTIVE);
			roleService.save(role);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[RoleController : save]" + role, e);
			return failure("保存失败" + e.getMessage());
		}
	}
	
	@ApiOperation(value = "更新角色")
	@RequiresPermissions("role:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "roleKey",value = "角色key值",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "role",value = "角色对象") Role role,
							 String roleKey) {
		try {
			Role updateObj = roleService.findOne(role.getId());
			updateObj.setKey(roleKey);
			updateObj.setName(role.getName());
			updateObj.setDescription(role.getDescription());
			updateObj.setStatus(role.getStatus());
			roleService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[RoleController : update]" + role, e);
			return failure("更新数据失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除角色")
	@RequiresPermissions("role:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			roleService.delete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[RoleController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("role:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkRoleKey",value = "角色key",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "role",value = "角色对象") Role role,
								   String pkRoleKey) {
		try {
			if (role.getId() != null) {
				return update(role,pkRoleKey);
			} else {
				return save(role);
			}
		} catch (Exception e) {
			log.error("[RoleController : save-update]" + role, e);
			return failure("新增或者更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public Role getOne(@PathVariable Long id) {
		return roleService.findOne(id);
	}
	
	@ApiOperation(value = "角色关联多个功能")
	@RequiresPermissions("role:saveFunctions")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "角色ID",dataType="Long",required = true),
	        @ApiImplicitParam(name = "ids",value = "功能IDS",required = true)
	})
	@PostMapping(value = "/save-functions")
	public ResultJson saveFunctions(Long id, String ids) {
		try {
			roleFunctionService.save(id, ids);
			return success("角色关联功能成功");
		} catch (Exception e) {
			log.error("[RoleController : save-functions]" + "id:" + id + "ids:" + ids, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "通过功能ID查询角色")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "功能ID",required = true)
	})
	@GetMapping(value = "/role-functions")
	public List<String> findRoleFunctions(String id) {
		List<BigInteger> roleIds = new ArrayList<>();
		List<String> roleIdStrs = new ArrayList<>();
		try {
			roleIds = roleFunctionService.findRoleByFunctionId(id);
			for(BigInteger roleId:roleIds){
				roleIdStrs.add(roleId.toString());
			}
		} catch (Exception e) {
			log.error("[RoleController : role-functions]" + "id:" + id, e);
		}
		return roleIdStrs;
	}

	@ApiOperation(value = "获取角色下的用户")
	@RequiresPermissions("role:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "角色ID",required = true),
	        @ApiImplicitParam(name = "searchText",value = "搜索内容")
	})
	@GetMapping(value = "/users")
	public List<User> getUsers(String id,String searchText) {
		List<User> list = roleService.getUsers(id,searchText);
		return list;
	}

	@ApiOperation(value = "角色关联多个用户")
	@RequiresPermissions("role:saveUsers")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "角色ID",required = true),
	        @ApiImplicitParam(name = "ids",value = "用户ID"),
	        @ApiImplicitParam(name = "allIds",value = "用户所有IDS")
	})
	@PostMapping(value = "/save-users")
	public ResultJson saveUsers(String id, String ids,String allIds) {
		try {
			userRoleService.saveUser(id, ids,allIds);
			return success("用户关联角色成功");
		} catch (Exception e) {
			log.error("[RoleController : save-users]" + "id:" + id + "ids:" + ids, e);
			return failure("更新失败" + e.getMessage());
		}
	}
}
