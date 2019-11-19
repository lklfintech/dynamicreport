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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.lakala.dynamicreport.datamodel.service.IDataSourceService;

/**
 * <p>
 * springBoot启动后初始化入口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Component
// 被spring容器管理
@Order(1)
// 如果多个自定义ApplicationRunner，用来标明执行顺序
public class ApplicationStartup implements ApplicationRunner {

	private Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

	@Autowired
	IDataSourceService dataSourceService;

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		try {
			dataSourceService.initDataSour();
		} catch (Exception e) {
			log.error("启动初始化数据源异常:{}", e.getMessage());
		}
	}

}