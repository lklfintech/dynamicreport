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

import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.system.model.SysLog;
import com.lakala.dynamicreport.system.query.SysLogQuery;
import com.lakala.dynamicreport.system.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/sys-log")
@Api(tags = "系统日志管理",description = "系统日志控制器(SysLogController)")
public class SysLogController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(SysLogController.class);

    @Autowired
    ISysLogService sysLogService;
 
	/**
	 * 分页
	 * @return
	 */
	@ApiOperation(value = "分页")
	@RequiresPermissions("sys_log:query")
	@GetMapping(value = "/list")
	public PageBean<SysLog> list(@ApiParam(name = "query",value = "系统日志查询对象",required = true) SysLogQuery query) {
		PageBean<SysLog> pb = new PageBean<>();
		Page<SysLog> page = sysLogService.findUserCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

}
