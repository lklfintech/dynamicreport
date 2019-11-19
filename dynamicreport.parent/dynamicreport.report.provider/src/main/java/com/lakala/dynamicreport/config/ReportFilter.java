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
package com.lakala.dynamicreport.config;

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.service.ICfgPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
 
/**
 * <p>
 * 针对报表的权限做过滤
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@WebFilter(filterName = "reportFilter", urlPatterns = "/page/rptTemplate/*")
public class ReportFilter implements Filter {

	private Logger log=LoggerFactory.getLogger(ReportFilter.class);

	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
    private ICfgPageService cfgPageService;

	/**
	 * 执行过滤操作
	 *
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		HttpServletResponse res=(HttpServletResponse)response;
		HttpServletRequest req=(HttpServletRequest)request;
		
		Object pageId=req.getParameter("pageId");
		Object preview=req.getParameter("preview");

		String page403=contextPath+"/403.html";
		String errInf=String.format("非法访问报表【%s】,请求拒绝", pageId);
		
		HttpSession session=req.getSession();
		ActiveUser userDetail = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		
		if(userDetail==null){
			chain.doFilter(request, response);
			return ;
		}
		
		//报表预览  本人及超级管理员可操作
		if("Y".equalsIgnoreCase(String.valueOf(preview))){
			CfgPage cfgPage=cfgPageService.findOne(String.valueOf(pageId));
			if(cfgPage!=null&&(cfgPage.getCreatedUser().equals(userDetail.getUsername())||"超级管理员".equals(userDetail.getRoleName()))){
				chain.doFilter(request, response);
				return ;
			}else{
				log.info(errInf);
				res.sendRedirect(page403);
				return ;
			}
		}
		
		//校验权限
		List<String> permissions=userDetail.getPermissions();
		if(permissions==null){
			chain.doFilter(request, response);
			return ;
		}

		boolean redirect=true;
		for(String str:permissions){
			if(str.equalsIgnoreCase(String.valueOf(pageId))){
				chain.doFilter(request, response);
				redirect=false;
				break;
			}
		}
		if(redirect){
			log.info(errInf);
			res.sendRedirect(page403);
		}

	}

	/**
	 * 过滤器初始化
	 *
	 * @param config
	 */
	@Override
	public void init(FilterConfig config) {
		//过滤器初始化
		
	}

	/**
	 * 过滤器销毁
	 */
	public void destroy() {
		//过滤器初始化
	}

}