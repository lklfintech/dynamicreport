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
package com.lakala.dynamicreport.druid.config;

import com.alibaba.druid.support.http.StatViewServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.WebInitParam;

/**
 * <p>
 * 配置druid 监控
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@WebServlet(urlPatterns = "/druid/*", 
initParams={
		@WebInitParam(name="allow",value=""),// IP白名单 (没有配置或者为空，则允许所有访问)
		@WebInitParam(name="deny",value=""),// IP黑名单 (存在共同时，deny优先于allow)
		@WebInitParam(name="resetEnable",value="true")// 禁用HTML页面上的“Reset All”功能
})

public class DruidStatViewServlet extends StatViewServlet {
	private static final long serialVersionUID = 1L;

}
