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
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * 对没有登录的报表请求进行拦截, 全部返回json信息. 覆盖掉shiro原本的跳转login.html的拦截方式
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ReportAuthorizationFilter extends FormAuthenticationFilter {

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		if(SecurityUtils.getSubject().isAuthenticated()) {
			return true;//已经登录过
		}else{
			super.saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}

	/**
	 *@Override
	 *protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
	 *	return super.onAccessDenied(request, response);
	 *}
	 */
	@Bean
	public FilterRegistrationBean registration(ReportAuthorizationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean(filter);
		registration.setEnabled(false);
		return registration;
	}

}
