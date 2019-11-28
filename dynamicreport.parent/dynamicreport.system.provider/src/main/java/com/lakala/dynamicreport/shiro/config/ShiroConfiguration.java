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

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.redis.config.RedisCacheManager;
import com.lakala.dynamicreport.redis.config.RedisSessionDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * shiro配置
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Configuration
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ShiroConfiguration {

	@Value("${cache.type}")
	private String cacheType;

	@Autowired
	RedisTemplate redisTemplate;

	/**
	 * Shiro的Web过滤器Factory 命名:shiroFilter
	 *
	 * @param securityManager
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// Shiro的核心安全接口,这个属性是必须的
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl("/login.html");
		shiroFilterFactoryBean.setSuccessUrl("/index.html");
		shiroFilterFactoryBean.setUnauthorizedUrl("403.html");
		Map<String, Filter> filterMap = new LinkedHashMap<>();
		filterMap.put("authc", new AuthorizationFilter());
		filterMap.put("reportAuthc", new ReportAuthorizationFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		/*
		 * 定义shiro过滤链 Map结构
		 * Map中key(xml中是指value值)的第一个'/'代表的路径是相对于HttpServletRequest
		 * .getContextPath()的值来的
		 * anon：它对应的过滤器里面是空的,什么都没做,这里.do和.jsp后面的*表示参数,比方说login.jsp?main这种
		 * authc：该过滤器下的页面必须验证后才能访问
		 * ,它是Shiro内置的一个拦截器org.apache.shiro.web.filter.authc
		 * .FormAuthenticationFilter
		 */
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		/*
		 * 过滤链定义，从上向下顺序执行，一般将 / ** 放在最为下边:这是一个坑呢，一不小心代码就不好使了
		 * authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
		 */
		// filterChainDefinitionMap.put("/", "anon")
		filterChainDefinitionMap.put("/decisionTryOut/**", "anon");
		filterChainDefinitionMap.put("/assets/**", "anon");
		filterChainDefinitionMap.put("/login/auth", "anon");
		filterChainDefinitionMap.put("/login/logout", "anon");
		filterChainDefinitionMap.put("login.html", "anon");
		filterChainDefinitionMap.put("index.html", "anon");
		filterChainDefinitionMap.put("/modifyPwd.html", "anon");
		filterChainDefinitionMap.put("/login/modifyPwd", "anon");
		filterChainDefinitionMap.put("/error", "anon");
		filterChainDefinitionMap.put("/tryOut/getData", "anon");
		filterChainDefinitionMap.put("/tryOut/get", "anon");
		filterChainDefinitionMap.put("/tryOut/fileUpload", "anon");
		filterChainDefinitionMap.put("/decisionConsumer/getData", "anon");
		//filterChainDefinitionMap.put("/decision/getAsyncData", "anon");
		filterChainDefinitionMap.put("/decisionConsumer/get", "anon");
		// filterChainDefinitionMap.put("/decision/deployment", "anon");
		// filterChainDefinitionMap.put("/tryOut/**", "anon");
		filterChainDefinitionMap.put("/feature/**", "anon");// 特征工程消费者
		// filterChainDefinitionMap.put("/parallelServiceInvoke/invoke",
		// "anon");
		filterChainDefinitionMap.put("/tenantCache/refresh", "anon");
		filterChainDefinitionMap.put("/rptTemplate/index.html","reportAuthc");
		filterChainDefinitionMap.put("/report/**","reportAuthc");

		//swagger filter
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/swagger-resources", "anon");
		filterChainDefinitionMap.put("/swagger-resources/configuration/security", "anon");
		filterChainDefinitionMap.put("/swagger-resources/configuration/ui", "anon");
		filterChainDefinitionMap.put("/v2/api-docs", "anon");
		filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
		filterChainDefinitionMap.put("/data-source/refresh", "anon");
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 这里需要设置一个cookie的名称 原因就是会跟原来的session的id值重复的
	 *
	 * @return
	 */
	@Bean
	public SimpleCookie simpleCookie() {
		SimpleCookie cookie = new SimpleCookie("REDISSESSION");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(-1);
		return cookie;
	}

	@Bean
	public RedisSessionDao redisSessionDao() {
		RedisSessionDao dao = new RedisSessionDao();
		dao.setRedisTemplate(redisTemplate);
		return dao;
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		RedisCacheManager manager = new RedisCacheManager();
		manager.setRedisTemplate(redisTemplate);
		return manager;
	}

	@Bean
	public DefaultSessionManager configWebSessionManager() {
		RedisSessionDao redisSessionDao = redisSessionDao();
		RedisCacheManager redisCacheManager = redisCacheManager();
		MyWebSessionManager manager = new MyWebSessionManager();
		manager.setCacheManager(redisCacheManager);// 加入缓存管理器
		manager.setSessionDAO(redisSessionDao);// 设置SessionDao
		manager.setDeleteInvalidSessions(true);// 删除过期的session
		manager.setGlobalSessionTimeout(redisSessionDao.getExpireTime());// 设置全局session超时时间
		manager.setSessionValidationSchedulerEnabled(true);// 是否定时检查session
		manager.setSessionIdCookie(simpleCookie());
		return manager;
	}

	/**
	 * 不指定名字的话，自动创建一个方法名第一个字母小写的bean
	 */
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(userRealm());
		if (StringUtils.isNotBlank(cacheType) && cacheType.equals(GlobalConstants.REDIS_CACHE)) {
			// 注入缓存管理器
			securityManager.setCacheManager(redisCacheManager());// 这个如果执行多次，也是同样的一个对象
			// session管理器
			securityManager.setSessionManager(configWebSessionManager());
		}
		return securityManager;
	}

	/**
	 * Shiro Realm 继承自AuthorizingRealm的自定义Realm,即指定Shiro验证用户登录的类为自定义的
	 */
	@Bean
	public UserRealm userRealm() {
		UserRealm realm = new UserRealm();
		realm.setCachingEnabled(false);
		realm.setAuthenticationCachingEnabled(false);
		return realm;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码; ） 可以扩展凭证匹配器，实现 输入密码错误次数后锁定等功能，下一次
	 */
	@Bean(name = "credentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		// 散列算法:这里使用MD5算法
		hashedCredentialsMatcher.setHashAlgorithmName("md5");
		// 散列的次数，比如散列两次，相当于 md5(md5(""))
		hashedCredentialsMatcher.setHashIterations(2);
		// storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
		hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
		return hashedCredentialsMatcher;
	}

	/**
	 * Shiro生命周期处理器
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
	 * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator
	 * (可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 */
	@Bean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
}
