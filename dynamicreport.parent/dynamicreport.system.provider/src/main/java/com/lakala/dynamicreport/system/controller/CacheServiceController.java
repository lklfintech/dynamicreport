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
import com.lakala.dynamicreport.system.model.CacheService;
import com.lakala.dynamicreport.system.model.ServerList;
import com.lakala.dynamicreport.system.query.CacheServiceQuery;
import com.lakala.dynamicreport.system.service.ICacheServiceService;
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
 * 缓存配置controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cache-service")
@Api(tags = "缓存配置管理",description = "缓存配置控制器(CacheServiceController)")
public class CacheServiceController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(CacheServiceController.class);

	@Autowired
	private ICacheServiceService cacheServiceService;
	
	@Autowired
	private IServerListService serverListService;
  
	@ApiOperation(value = "分页")
	@RequiresPermissions("sys_cache_service:query")
	@GetMapping(value = "/list")
 	public PageBean<CacheService> list(@ApiParam(name = "query",value = "缓存配置查询对象") CacheServiceQuery query) {
		PageBean<CacheService> pb = new PageBean<>();
		Page<CacheService> page = cacheServiceService.findCacheServiceCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("sys_cache_service:query")
	@GetMapping(value = "/list-all")
	public List<CacheService> listAll(@ApiParam(name = "query",value = "缓存配置查询对象",required = true) CacheServiceQuery query) {
		return cacheServiceService.findCacheService(query);
	}

	@ApiOperation(value = "保存缓存配置")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "cacheService",value = "缓存配置对象",required = true) CacheService cacheService) {
 		try {
			cacheService.setStatus(StatusConstants.ACTIVE);
			cacheServiceService.save(cacheService);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[CacheServiceController : save]" + cacheService, e);
			return failure("保存失败" + e.getMessage());
		}
	}
	
	@ApiOperation(value = "更新缓存配置")
	@RequiresPermissions("sys_cache_service:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "roleKey",value = "角色key",required = true)
	})
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "cacheService",value = "缓存配置对象",required = true) CacheService cacheService,
							 String roleKey) {
		try {
			CacheService updateObj = cacheServiceService.findOne(cacheService.getId());
			ServerList serverList = serverListService.findOne(cacheService.getServerList().getId());
			updateObj.setServerList(serverList);
			updateObj.setContextPath(cacheService.getContextPath());
			updateObj.setServicePath(cacheService.getServicePath());
			updateObj.setDescription(cacheService.getDescription());
			updateObj.setHttpmethod(cacheService.getHttpmethod());
			updateObj.setStatus(cacheService.getStatus());
			cacheServiceService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[CacheServiceController : update]" + cacheService, e);
			return failure("更新数据失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除缓存配置")
	@RequiresPermissions("sys_cache_service:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			cacheServiceService.delete(id);
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[CacheServiceController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(),e);
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("sys_cache_service:saveOrUpdate")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "pkRoleKey",value = "缓存配置key",required = true)
	})
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "cacheService",value = "缓存配置对象",required = true) CacheService cacheService,
								   String pkRoleKey) {
		try {
			if (cacheService.getId() != null) {
				return update(cacheService,pkRoleKey);
			} else {
				return save(cacheService);
			}
		} catch (Exception e) {
			log.error("[RoleController : saveOrUpdate]" + cacheService, e);
			return failure("新增或者更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("sys_cache_service:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public CacheService getOne(@PathVariable Long id) {
		return cacheServiceService.findOne(id);
	}
	
	@ApiOperation(value = "刷新服务")
	@RequiresPermissions("sys_cache_service:refresh")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "ids",value = "缓存配置IDS",required = true)
	})
	@PutMapping(value = "/refresh")
	public ResultJson refresh(String ids) {
		ResultJson resultJson=new ResultJson();
		resultJson.setSuccess(true);
		resultJson.setMsg("刷新缓存成功!");
		try{
			 cacheServiceService.refresh(ids);
		}catch(Exception e){
			resultJson.setSuccess(false);
			resultJson.setObj(e);
			resultJson.setMsg("刷新缓存失败!"+e.getMessage());
		}
 	   return resultJson;
	}

}
