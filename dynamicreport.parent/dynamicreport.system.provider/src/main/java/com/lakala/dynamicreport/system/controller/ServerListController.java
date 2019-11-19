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
import com.lakala.dynamicreport.system.model.ServerList;
import com.lakala.dynamicreport.system.query.ServerListQuery;
import com.lakala.dynamicreport.system.service.IServerListService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 服务器配置controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/server-list")
@Api(tags = "服务器配置管理",description = "服务器配置控制器(ServerListController)")
public class ServerListController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(ServerListController.class);

	@Autowired
	private IServerListService serverListService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("sys_server_list:query")
	@GetMapping(value = "/list")
 	public PageBean<ServerList> list(@ApiParam(name = "query",value = "服务配置查询对象",required = true) ServerListQuery query) {
		PageBean<ServerList> pb = new PageBean<>();
		Page<ServerList> page = serverListService.findServerListCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("sys_server_list:query")
	@GetMapping(value = "/list-all")
	public List<ServerList> listAll(@ApiParam(name = "query",value = "服务配置查询对象",required = true) ServerListQuery query) {
		return serverListService.findServerList(query);
	}

	@ApiOperation(value = "保存服务器配置")
	@RequiresPermissions("sys_server_list:saveOrUpdate")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "serverList",value = "服务配合对象",required = true) ServerList serverList) {
 		try {
			serverList.setStatus(StatusConstants.ACTIVE);
			serverListService.save(serverList);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[ServerListController : save]" + serverList, e);
			return failure("保存失败" + e.getMessage());
		}
	}
	
	@ApiOperation(value = "更新服务器配置")
	@RequiresPermissions("sys_server_list:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "roleKey",value = "角色Key",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "serverList",value = "服务配置对象",required = true) ServerList serverList,String roleKey) {
		try {
			ServerList updateObj = serverListService.findOne(serverList.getId());
 			updateObj.setIp(serverList.getIp());
			updateObj.setPort(serverList.getPort());
			updateObj.setDescription(serverList.getDescription());
			updateObj.setStatus(serverList.getStatus());
			serverListService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[ServerListController : update]" + serverList, e);
			return failure("更新数据失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除服务器配置")
	@RequiresPermissions("sys_server_list:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			serverListService.delete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[ServerListController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("sys_server_list:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkRoleKey",value = "角色Key",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "serverList",value = "服务配置对象",required = true) ServerList serverList,String pkRoleKey) {
		try {
			if (serverList.getId() != null) {
				return update(serverList,pkRoleKey);
			} else {
				return save(serverList);
			}
		} catch (Exception e) {
			log.error("[RoleController : save-update]" + serverList, e);
			return failure("新增或者更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("sys_server_list:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public ServerList getOne(@PathVariable Long id) {
		return serverListService.findOne(id);
	}
	
}
