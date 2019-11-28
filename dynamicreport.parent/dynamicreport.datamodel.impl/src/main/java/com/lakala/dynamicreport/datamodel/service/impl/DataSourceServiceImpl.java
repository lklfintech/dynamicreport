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
package com.lakala.dynamicreport.datamodel.service.impl;

import static com.lakala.dynamicreport.common.constants.GlobalConstants.DRIVER_HIVE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.constants.HttpConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.common.utils.DesUtil;
import com.lakala.dynamicreport.datamodel.constants.DataModelConstants;
import com.lakala.dynamicreport.datamodel.model.DataSource;
import com.lakala.dynamicreport.datamodel.query.DataSourceQuery;
import com.lakala.dynamicreport.datamodel.repository.IDataSourceRepository;
import com.lakala.dynamicreport.datamodel.service.IDataSourceService;
import com.lakala.dynamicreport.system.model.CacheService;
import com.lakala.dynamicreport.system.model.ServerList;
import com.lakala.dynamicreport.system.repository.CacheServiceRepository;

/**
 * <p>
 * 操作数据源接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class DataSourceServiceImpl extends BaseServiceImpl<IDataSourceRepository,DataSource, Long> implements IDataSourceService {

	private Logger log = LoggerFactory.getLogger(DataSourceServiceImpl.class);


	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private IDataSourceRepository dataSourceRepository;

	@Value("${datamodel.druid.datasource.initialSize}")
	private String initialSize;
	@Value("${datamodel.druid.datasource.minIdle}")
	private String minIdle;
	@Value("${datamodel.druid.datasource.maxActive}")
	private String maxActive;
	@Value("${datamodel.druid.datasource.maxWait}")
	private String maxWait;
	@Value("${datamodel.druid.datasource.minEvictableIdleTimeMillis}")
	private String minEvictableIdleTimeMillis;
	@Value("${datamodel.druid.datasource.testWhileIdle}")
	private String testWhileIdle;
	@Value("${datamodel.druid.datasource.testOnBorrow}")
	private String testOnBorrow;
	@Value("${datamodel.druid.datasource.testOnReturn}")
	private String testOnReturn;
	@Value("${datamodel.druid.datasource.poolPreparedStatements}")
	private String poolPreparedStatements;
	@Value("${datamodel.druid.datasource.validationQuery}")
	private String validationQuery;
	@Value("${datamodel.druid.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private String maxPoolPreparedStatementPerConnectionSize;

	@Autowired
	private CacheServiceRepository cacheServiceRepository;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void refreshDataSource(DataSource datasource) throws BusinessException {
		// 验证数据源
		validDataSource(datasource);

		// 初始化数据源连接池
		initDataSourceConPools(datasource);
	}

	/**
	 * 验证数据源
	 *
	 * @param datasource
	 */
	private void validDataSource(DataSource datasource){
		try {
			// 如果是hive库，且需要Kerberos认证，则先进行认证
			if (datasource.getDriverClass().contains("HiveDriver") && "Y".equalsIgnoreCase(datasource.getHiveKerberos())) {
				System.setProperty("java.security.krb5.conf", datasource.getHiveKrb5());
				Configuration configuration = new Configuration();
				configuration.set("hadoop.security.authentication", "Kerberos");
				configuration.set("keytab.file", datasource.getHiveKeytab());
				configuration.set("kerberos.principal", datasource.getUsername());
				UserGroupInformation.setConfiguration(configuration);
				UserGroupInformation.loginUserFromKeytab(datasource.getUsername(), datasource.getHiveKeytab());
			}
			// 测试数据源连接是否有效
			Class.forName(datasource.getDriverClass());
			Connection con = DriverManager.getConnection(datasource.getUrl(), datasource.getUsername(), datasource.getPassword());
			con.close();
		} catch (Exception e) {
			log.error(String.format("Url【%s】,userName【%s】,【%s】", datasource.getUrl(), datasource.getUsername(), "请检查数据源配置:" + e.getMessage()));
			throw new BusinessException("请检查数据源配置:" + e.getMessage());
		}
	}

	/**
	 *初始化数据源连接池
	 *
	 * @param datasource
	 */
	private void initDataSourceConPools(DataSource datasource){
		try {
			ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

			//构建DS bean
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(DruidDataSource.class);
			beanDefinitionBuilder.addPropertyValue("username", datasource.getUsername());
			beanDefinitionBuilder.addPropertyValue("password", datasource.getPassword());
			beanDefinitionBuilder.addPropertyValue("url", datasource.getUrl());
			beanDefinitionBuilder.addPropertyValue("initialSize", initialSize);
			beanDefinitionBuilder.addPropertyValue("minIdle", minIdle);
			beanDefinitionBuilder.addPropertyValue("maxActive", maxActive);
			beanDefinitionBuilder.addPropertyValue("maxWait", maxWait);
			beanDefinitionBuilder.addPropertyValue("minEvictableIdleTimeMillis", minEvictableIdleTimeMillis);
			if (!DRIVER_HIVE.equalsIgnoreCase(datasource.getDriverClass())) {
				beanDefinitionBuilder.addPropertyValue("validationQuery", validationQuery);
				beanDefinitionBuilder.addPropertyValue("testWhileIdle", testWhileIdle);
				beanDefinitionBuilder.addPropertyValue("testOnBorrow", testOnBorrow);
				beanDefinitionBuilder.addPropertyValue("testOnReturn", testOnReturn);
			}
			beanDefinitionBuilder.addPropertyValue("poolPreparedStatements", poolPreparedStatements);
			beanDefinitionBuilder.addPropertyValue("maxPoolPreparedStatementPerConnectionSize", maxPoolPreparedStatementPerConnectionSize);
			// 关闭失败后重连
			beanDefinitionBuilder.addPropertyValue("connectionErrorRetryAttempts", "0");
			beanDefinitionBuilder.addPropertyValue("breakAfterAcquireFailure", "true");
			List<Filter> ls = new ArrayList<>();
			ls.add(applicationContext.getBean("stat-filter", StatFilter.class));
			beanDefinitionBuilder.addPropertyValue("proxyFilters", ls);//采用自定义的配置
			// 如果数据源ds bean存在，则先删除
			if (beanFactory.containsBean(datasource.getIdentifier() + DataModelConstants.DS_NAME)) {
				beanFactory.removeBeanDefinition(datasource.getIdentifier() + DataModelConstants.DS_NAME);
			}
			beanFactory.registerBeanDefinition(datasource.getIdentifier() + DataModelConstants.DS_NAME, beanDefinitionBuilder.getBeanDefinition());

			//构建对应的jdbcTemplate Bean
			BeanDefinitionBuilder jbTemp = BeanDefinitionBuilder.rootBeanDefinition(JdbcTemplate.class);
			DruidDataSource druidDataSource = applicationContext.getBean(datasource.getIdentifier() + DataModelConstants.DS_NAME, DruidDataSource.class);
			jbTemp.addConstructorArgValue(druidDataSource);
			// 如果数据源jdbcTemplate bean存在，则先删除
			if (beanFactory.containsBean(datasource.getIdentifier())) {
				beanFactory.removeBeanDefinition(datasource.getIdentifier());
			}
			beanFactory.registerBeanDefinition(datasource.getIdentifier(), jbTemp.getBeanDefinition());
		} catch (Exception e) {
			log.error(String.format("Url【%s】,userName【%s】,【%s】", datasource.getUrl(), datasource.getUsername(), "初始数据源异常:" + e.getMessage()));
			throw new BusinessException("初始数据源异常:" + e.getMessage());
		}
	}

	@Override
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		try {
			// 如果是hive库，刷新数据源，避免认证过时问题
			if (dataSource.getDriverClass().contains("HiveDriver") && "Y".equalsIgnoreCase(dataSource.getHiveKerberos())) {
				refreshDataSource(dataSource);
			}
			return applicationContext.getBean(dataSource.getIdentifier(), JdbcTemplate.class);
		} catch (Exception e) {
			log.error(String.format("请检查数据源【%s】配置:【%s】", dataSource.getIdentifier(), e.getMessage()));
			throw new BusinessException(String.format("请检查数据源【%s】配置:【%s】", dataSource.getIdentifier(), e.getMessage()));
		}
	}

	@Override
	public void inactiveDS(String key) {
		try {
			ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
			//删除bean ds
			if (beanFactory.containsBean(key + DataModelConstants.DS_NAME)) {
				beanFactory.removeBeanDefinition(key + DataModelConstants.DS_NAME);
			}
			//删除bean jdbcTemplate
			if (beanFactory.containsBean(key)) {
				beanFactory.removeBeanDefinition(key);
			}
		} catch (Exception e) {
			String err = String.format("%s 删除数据源Bean失败:%s", key, e.getMessage());
			log.error(err);
			throw new BusinessException(err);
		}
	}

	@Override
	public Page<DataSource> findDatasourceCriteria(DataSourceQuery dataSourceQuery) {
		return dataSourceRepository.findAll(specification(dataSourceQuery), PageForList.getPageable("modifiedDate",dataSourceQuery));
	}

	@Override
	public List<DataSource> findDatasource(DataSourceQuery dataSourceQuery) {
		return dataSourceRepository.findAll(specification(dataSourceQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<DataSource> specification(final DataSourceQuery dataSourceQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != dataSourceQuery) {
			list.add(Predication.get(OperationEnum.EQUAL,"id",dataSourceQuery.getId(),Long.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"status",dataSourceQuery.getStatus(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.EQUAL,"identifier",dataSourceQuery.getIdentifier(),String.class,OperationEnum.AND));
			list.add(Predication.get(OperationEnum.LIKE,"name",dataSourceQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"identifier",dataSourceQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"url",dataSourceQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"driverClass",dataSourceQuery.getSearchText(),String.class,OperationEnum.OR));
			list.add(Predication.get(OperationEnum.LIKE,"username",dataSourceQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.whereAndOr(list);
	}

	@Override
	public void initDataSour() {
		DataSourceQuery dataSourceQuery = new DataSourceQuery();
		dataSourceQuery.setStatus(StatusConstants.ACTIVE);
		List<DataSource> dss = findDatasource(dataSourceQuery);
		for (DataSource ds : dss) {
			try {
				ds.setPassword(DesUtil.decrypt(ds.getPassword()));
				refreshDataSource(ds);
			} catch (Exception e) {
				log.error(String.format("初始化数据源【%s】异常【%s】", ds.getIdentifier(), e.getMessage()));
			}
		}
	}

	@Override
	@Transactional
	public void saveOrUpdateByUpdate(DataSource dataSource) throws Exception{
		ResultJson resultJson = new ResultJson();
		resultJson.setSuccess(true);

		DataSource updateObj = findOne(dataSource.getId());
		String oldPwd=updateObj.getPassword();
		updateObj.setName(dataSource.getName());
		updateObj.setUrl(dataSource.getUrl());
		updateObj.setDriverClass(dataSource.getDriverClass());
		updateObj.setUsername(dataSource.getUsername());
		updateObj.setPassword(dataSource.getPassword());
		updateObj.setDescription(dataSource.getDescription());
		updateObj.setStatus(dataSource.getStatus());

		updateObj.setHiveKrb5(dataSource.getHiveKrb5());
		updateObj.setHiveKeytab(dataSource.getHiveKeytab());

		String pwd="";
		if("******".equals(dataSource.getPassword())){//没修改密码
			pwd=DesUtil.decrypt(oldPwd);
		}else{
			pwd=dataSource.getPassword();
		}

		updateObj.setPassword(DesUtil.encrypt(pwd));

		dataSourceRepository.save(updateObj);

		refresh(updateObj.getIdentifier());
	}

	@Override
	@Transactional
	public ResultJson saveOrUpdateBySave(DataSource dataSource) throws Exception{
		ResultJson resultJson = new ResultJson();
		resultJson.setSuccess(true);

		DataSourceQuery query = new DataSourceQuery();
		query.setIdentifier(dataSource.getIdentifier());
		List<DataSource> list = findDatasource(query);
		if (CollectionUtils.isNotEmpty(list)) {
			resultJson.setSuccess(false);
			resultJson.setMsg(String.format("保存失败，数据源标识%s有重复",dataSource.getIdentifier()));
			return resultJson;
		}
		dataSource.setStatus(dataSource.getStatus());

		if(StatusConstants.ACTIVE.equals(dataSource.getStatus())){
			//启用数据源,刷新bean
			refreshDataSource(dataSource);
		}
		dataSource.setPassword(DesUtil.encrypt(dataSource.getPassword()));
		dataSourceRepository.save(dataSource);
		resultJson.setMsg(String.format("保存成功"));
		return resultJson;
	}

	private void refresh(String identifier){
		StringBuilder strBul = new StringBuilder();
		List<CacheService> cacheServices = cacheServiceRepository.findAll();
		for(CacheService cacheService:cacheServices){

			if(!StatusConstants.ACTIVE.equals(cacheService.getStatus())){
				continue;
			}
			ServerList serverList = cacheService.getServerList();
			strBul.append(HttpConstants.HTTP).append(HttpConstants.SIGN_THREE).append(HttpConstants.SIGN_ONE).append(serverList.getIp()).append(":").append(serverList.getPort());
			if (!StringUtils.isBlank(cacheService.getContextPath())) {
				strBul.append(HttpConstants.SIGN_TWO).append(cacheService.getContextPath()).append(HttpConstants.SIGN_TWO);
			}
			strBul.append(cacheService.getServicePath());
			strBul.append("?identifier="+identifier);
			String url = strBul.toString();
			log.info("请求地址:{}", url);
			ResponseEntity<String> respone = restTemplate.getForEntity(url, String.class);
			String body=respone.getBody();
			JSONObject obj=JSONObject.parseObject(body);
			log.info(obj.getString("msg"));
		}
	}
	@Override
	public void refreshDSBean(String identifier) throws Exception{
		DataSource dataSource=dataSourceRepository.findByIdentifier(identifier);
		dataSource.setPassword(DesUtil.decrypt(dataSource.getPassword()));
		if(StatusConstants.ACTIVE.equals(dataSource.getStatus())){
			refreshDataSource(dataSource);
		}else{
			//停用  销毁 bean
			inactiveDS(dataSource.getIdentifier());
		}

	}



}
