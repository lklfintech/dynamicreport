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
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.SystemUserQuery;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import com.lakala.dynamicreport.system.service.IUserRoleService;
import com.lakala.dynamicreport.system.utils.UserConvertPwd;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lakala.dynamicreport.common.constants.GlobalConstants.SESSION_USER_INFO;

/**
 * <p>
 * 用户controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/system-user")
@Api(tags = "用户管理",description = "用户控制器(SystemUserController)")
public class SystemUserController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(SystemUserController.class);

	@Autowired
	private ISystemUserService systemUserService;

	@Autowired
	private IUserRoleService userRoleService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("system_user:query")
	@GetMapping(value = "/list")
	public PageBean<User> list(@ApiParam(name = "query",value = "系统用户查询对象") SystemUserQuery query) {
		PageBean<User> pb = new PageBean<>();
		Page<User> page = systemUserService.findUserCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("system_user:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "action",value = "操作行为",required = true),
	        @ApiImplicitParam(name = "pkId",value = "用户ID",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "systemUser",value = "用户对象") User systemUser,
								   String action, String pkId) {
		try {
			if ("update".equals(action)) {
				return update(systemUser,pkId);
			} else {
				return save(systemUser);
			}
		} catch (Exception e) {
			log.error("[SystemUserController : save_update]" + systemUser, e);
			return failure("更新:" + e.getMessage());
		}
	}

	@ApiOperation(value = "保存参数")
	@RequiresPermissions("system_user:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "systemUser",value = "用户对象") User systemUser) {
		try {
			User systemUsers2=systemUserService.findOne(systemUser.getId());
			if(systemUsers2!=null){
				log.info(GlobalConstants.BRACKETS_2, "用户ID重复!",JSONObject.toJSONString(systemUser));
				return failure("用户ID重复!");
			}
			systemUser.setPassword(UserConvertPwd.encrypt(systemUser.getPassword()));
			systemUserService.save(systemUser);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[SystemUserController : save]" + systemUser, e);
			return failure("保存" + e.getMessage());
		}
	}

	@ApiOperation(value = "更新参数")
	@RequiresPermissions("system_user:saveOrUpdate")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pkId",value = "用户ID",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "systemUser",value = "用户对象") User systemUser, String pkId) {
		try {
			systemUserService.updateByParams(systemUser,pkId);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[SystemUserController : update]" + systemUser, e);
			return failure("更新" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除")
	@RequiresPermissions("system_user:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable String id) {
		if(StringUtils.isEmpty(id)){
			return failure("刪除失败");
		}

		Session session = SecurityUtils.getSubject().getSession();
		ActiveUser userInfo = (ActiveUser) session.getAttribute(SESSION_USER_INFO);
		if(GlobalConstants.USER_ADMIN.equalsIgnoreCase(id)){
			return failure("admin用户不能删除！");
		}
		if(userInfo!=null&&userInfo.getUsername().equalsIgnoreCase(id)){
			return failure("刪除失败，自己不能删除自己！");
		}
		try {
			systemUserService.delete(id);
		} catch (Exception e) {
			log.error("[SystemUserController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(),e);
		}

		return success("刪除成功");
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("system_user:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "主键ID",paramType = "path",dataType="String",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public User getOne(@PathVariable String id) {
		return systemUserService.findOne(id);
	}

	@ApiOperation(value = "获取用户下的角色")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "用户ID",required = true),
	        @ApiImplicitParam(name = "searchText",value = "搜索内容")
	})
	@GetMapping(value = "/roles")
	public List<Role> getRoles(String id, String searchText) {
		List<Role> list = systemUserService.getRules(id,searchText);
		return list;
	}

	@ApiOperation(value = "用户关联多个角色")
	@RequiresPermissions("system_user:saveRoles")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "用户ID",required = true),
	        @ApiImplicitParam(name = "ids",value = "现有用户角色IDS",required = true),
	        @ApiImplicitParam(name = "allIds",value = "原有用户角色IDS",required = true)
	})
	@PostMapping(value = "/save-roles")
	public ResultJson saveRoles(String id, String ids, String allIds) {
		try {
			userRoleService.save(id, ids,allIds);
			return success("用户关联角色成功");
		} catch (Exception e) {
			log.error("[SystemUserController : save_roles]" + "id:" + id + "ids:" + ids, e);
			return failure("更新" + e.getMessage());
		}
	}  

}
