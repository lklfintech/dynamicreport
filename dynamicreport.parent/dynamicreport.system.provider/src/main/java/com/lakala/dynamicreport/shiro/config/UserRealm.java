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
package com.lakala.dynamicreport.shiro.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.system.service.ILoginService;
import com.lakala.dynamicreport.system.utils.UserConvertPwd;

/**
 * <p>
 * 自定义Realm
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class UserRealm extends AuthorizingRealm {
	private Logger logger = LoggerFactory.getLogger(UserRealm.class);

	@Autowired
	private ILoginService loginService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Session session = SecurityUtils.getSubject().getSession();
		// 为当前用户设置角色和权限
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 查询用户的权限
		ActiveUser userDetail = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		if (userDetail != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("登录用户:{}", userDetail.getUsername());
			}
			if (userDetail.getPermissions() != null) {
				authorizationInfo.addStringPermissions(userDetail.getPermissions());
			}
		}
		return authorizationInfo;
	}

	/**
	 * 验证当前登录的Subject LoginController.auth()方法中执行Subject.login()时 执行此方法
	 *
	 * @param authcToken
	 * @return
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {
		String loginName = (String) authcToken.getPrincipal();
		// 获取用户密码
		String password = new String((char[]) authcToken.getCredentials());
		ActiveUser user = loginService.getUser(loginName, password);
		if (user == null) {
			logger.error("{}用户不存在", loginName);
			// 没找到帐号
			throw new UnknownAccountException();
		}
		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(),
		// ByteSource.Util.bytes("salt"), salt=username+salt,采用明文访问时，不需要此句
				getName());
		// session中密码加密
		user.setPassword(UserConvertPwd.encrypt(password));
		// 将用户信息放入session中
		SecurityUtils.getSubject().getSession().setAttribute(GlobalConstants.SESSION_USER_INFO, user);
		return authenticationInfo;
	}
}
