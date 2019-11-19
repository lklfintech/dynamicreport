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
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * Redis缓存配置
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class RedisCacheManager implements CacheManager {

	private String cacheKeyPrefix = GlobalConstants.REDIS_CACHE_PREFIX + GlobalConstants.REDIS_CACHE_SHIRO_PREFIX;

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public <K, V> Cache<K, V> getCache(String name) {
		return new ShiroRedisCache<>(cacheKeyPrefix + name);
	}

	/**
	 * 为shiro量身定做的一个redis cache,为Authorization cache做了特别优化
	 */
	public class ShiroRedisCache<K, V> implements Cache<K, V> {

		private String cacheKey;

		public ShiroRedisCache(String cacheKey) {
			this.cacheKey = cacheKey;
		}

		@Override
		public V get(K key){
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.get(key);
		}

		@Override
		public V put(K key, V value) {
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			hash.put(key, value);
			return value;
		}

		@Override
		public V remove(K key) {
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			V value = hash.get(key);
			hash.delete(key);
			return value;
		}

		@Override
		public void clear(){
			redisTemplate.delete(cacheKey);
		}

		@Override
		public int size() {
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.size().intValue();
		}

		@Override
		public Set<K> keys() {
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.keys();
		}

		@Override
		public Collection<V> values() {
			BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
			return hash.values();
		}
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}