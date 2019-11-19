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
package com.lakala.dynamicreport.redis.config;

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis Session配置
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class RedisSessionDao extends CachingSessionDAO {
	private Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);

	private String cacheKeyPrefix = GlobalConstants.REDIS_CACHE_PREFIX + GlobalConstants.REDIS_CACHE_SHIRO_PREFIX;

	/**
	 * Session超时时间，单位为毫秒
	 */
	private long expireTime = 60L * 1000 * 30;

	/**
	 * Redis操作类
	 */
	private RedisTemplate redisTemplate;

	public RedisSessionDao() {
		super();
	}

	public RedisSessionDao(long expireTime, RedisTemplate redisTemplate) {
		super();
		this.expireTime = expireTime;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void doUpdate(Session session) {
		// if (session == null || session.getId() == null) {
		// return;
		// }
		// if(logger.isDebugEnabled()){
		// logger.debug("update session {}",session.getId());
		// }
		// redisTemplate.opsForValue().set(cacheKeyPrefix + session.getId(),
		// session, expireTime, TimeUnit.MILLISECONDS);
	}

	@Override
	public void doDelete(Session session) {
		if (null == session) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("delete session {}", session.getId());
		}
		redisTemplate.opsForValue().getOperations().delete(cacheKeyPrefix + session.getId());
	}

	/**
	 * 获取活跃的session，可以用来统计在线人数，如果要实现这个功能，可以在将session加入redis时指定一个session前缀，
	 * 统计的时候则使用keys("session-prefix*")的方式来模糊查找redis中所有的session集合
	 *
	 * @return
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		return redisTemplate.keys(cacheKeyPrefix + "*");
	}

	@Override
	protected Serializable doCreate(Session session) {
		if (logger.isDebugEnabled()) {
			logger.debug("doCreate session {}", session.getId());
		}
		Serializable sessionId = this.generateSessionId(session);
		this.assignSessionId(session, sessionId);
		redisTemplate.opsForValue().set(cacheKeyPrefix + session.getId(), session, expireTime, TimeUnit.MILLISECONDS);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		return null;
		// if (sessionId == null) {
		// return null;
		// }
		// if(logger.isDebugEnabled()){
		// logger.debug("doCreate session {}",sessionId);
		// }
		// return (Session) redisTemplate.opsForValue().get(cacheKeyPrefix +
		// sessionId);
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
