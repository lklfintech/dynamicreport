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

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.system.query.UserQuery;
import com.lakala.dynamicreport.system.service.ISysLogService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 登录controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/login")
@Api(tags = "登录管理",description = "用户登录控制器(LoginController)")
public class LoginController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);


	@Autowired
	ISystemUserService systemUserService;
	
	@Autowired
	ISysLogService sysLogService;

	@ApiOperation(value = "登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "username",value = "用户名",required = true),
			@ApiImplicitParam(name = "password",value = "密码",required = true)
	})
	@PostMapping(value = "/auth", produces = "application/json;charset=UTF-8")
	public ResultJson auth(String username, String password) {
		try {
			String successUrl = systemUserService.auth(username,password);
			return success("登录成功",successUrl);
		} catch (AuthenticationException e) {
			log.error("登录失败，请检查用户名或者密码是否正确", e);
			return failure("登录失败，请检查用户名或者密码是否正确");
		}
	}

	@ApiOperation(value = "获取用户信息")
	@PostMapping(value = "/user-info", produces = "application/json;charset=UTF-8")
	public ResultJson getUserInfo() {
		ResultJson result = new ResultJson();
		try {
			// 从session获取用户信息k
			Session session = SecurityUtils.getSubject().getSession();
			ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
			if (userInfo == null) {
				session.setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);
				result.setObj(userInfo);
				result.setSuccess(false);
				result.setMsg("获取用户信息失败");
				return result;
			}
			result.setObj(session.getAttribute(GlobalConstants.SESSION_USER_INFO));
			result.setSuccess(true);
			result.setMsg("获取用户信息成功");
		} catch (AuthenticationException e) {
			String msg = "获取用户信息失败";
			log.error(msg, e);
			result.setSuccess(false);
			result.setMsg(msg);
		}
		return result;
	}

	@ApiOperation(value = "修改密码")
	@ApiImplicitParams({
			@ApiImplicitParam(value = "旧密码",name = "oldPassword",paramType = "query",dataType = "String",required = true),
			@ApiImplicitParam(value = "新密码",name = "password",paramType = "query",dataType = "String",required = true)
	})
	@PostMapping(value = "/modify-pwd", produces = "application/json;charset=UTF-8")
	public ResultJson modifyPwd(@ApiIgnore UserQuery user) {
		ResultJson result = new ResultJson();
		try {
			result = systemUserService.modifyPwd(user);
		} catch (AuthenticationException e) {
			log.error("修改用户密码失败！", e);
			result.setSuccess(false);
			result.setMsg("修改用户密码失败！");
		}
		return result;
	}

	@ApiOperation(value = "注销")
	@PostMapping(value = "/logout", produces = "application/json;charset=UTF-8")
	public ResultJson logout() {
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.logout();
			return success("注销成功");
		} catch (Exception e) {
			log.error("注销失败", e);
			return failure("注销失败");
		}
	}
}
