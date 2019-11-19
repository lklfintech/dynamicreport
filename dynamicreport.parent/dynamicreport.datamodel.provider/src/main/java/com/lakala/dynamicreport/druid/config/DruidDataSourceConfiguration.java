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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.stat.StatFilter;

/**
 * <p>
 * Druid 数据源配置
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Configuration
public class DruidDataSourceConfiguration {
	 
	/**
	 * druid  监听SQL
	 *
	 * @return
	 */
	@Bean(name = "stat-filter")
	@ConfigurationProperties(prefix = "spring.datasource.stat-filter")
	public StatFilter initStatFilter() {
		StatFilter statFilter = new StatFilter();
		statFilter.setMergeSql(false);//监控是否合并sql
		statFilter.setSlowSqlMillis(2000);//慢查询的定义
		statFilter.setLogSlowSql(true);
		return statFilter;
	}
    
    
}
