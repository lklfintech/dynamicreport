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
package com.lakala.dynamicreport.common.redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Redis服务接口实现类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class RedisServiceImpl implements IRedisService {

	/**
	 * 默认过期时长，单位：秒
	 */
	public static final long DEFAULT_EXPIRE = 60L * 60 * 24;

	/**
	 * 不设置过期时长
	 */
	public static final long NOT_EXPIRE = -1;

	@Resource
	private RedisTemplate<String, ?> redisTemplate;
	
	@Override
	public boolean set(final String key, final String value) {
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			connection.set(serializer.serialize(key), serializer.serialize(value));
			return Boolean.TRUE;
		});
	}
	
	@Override
	public String get(final String key) {
		return redisTemplate.execute((RedisCallback<String>) connection -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			byte[] value = connection.get(serializer.serialize(key));
			return serializer.deserialize(value);
		});
	}

	/**
	 * 自增 key不存在则为1 key存在则自增
	 * 
	 * @param key
	 */
	@Override
	public Long incrby(final String key) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> connection.incr(key.getBytes()));
	}

	/**
	 * 自增不存在数值会默认初始化0然后加1，传入时间后自动销毁
	 * 
	 * @param key
	 * @param liveTime
	 */
	@Override
	public Long incrby(final String key, final long liveTime) {
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
			Long incr = connection.incr(key.getBytes());
			connection.expire(key.getBytes(), liveTime);
			return incr;
		});
	}
	@Override
	public boolean expire(final String key, long expire) {
		return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
	}
	@Override
	public <T> boolean setList(String key, List<T> list) {
		String value = JSONObject.toJSONString(list);
		return set(key, value);
	}
	@Override
	public <T> List<T> getList(String key, Class<T> clz) {
		String json = get(key);
		if (json != null) {
			return JSONObject.parseArray(json, clz);
		}
		return new ArrayList<>();
	}
	@Override
	public long lpush(final String key, Object obj) {
		final String value = JSONObject.toJSONString(obj);
		return  redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			return connection.lPush(serializer.serialize(key), serializer.serialize(value));
		});
	}
	@Override
	public long rpush(final String key, Object obj) {
		final String value = JSONObject.toJSONString(obj);
		return redisTemplate.execute((RedisCallback<Long>) connection -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			return connection.rPush(serializer.serialize(key), serializer.serialize(value));
		});
	}
	@Override
	public String lpop(final String key) {
		return redisTemplate.execute((RedisCallback<String>) connection -> {
			RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
			byte[] res = connection.lPop(serializer.serialize(key));
			return serializer.deserialize(res);
		});
	}

	/**
	 * 判断key是否存在
	 */
	@Override
	public boolean existsKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
	 *
	 * @param oldKey
	 * @param newKey
	 */
	@Override
	public void renameKey(String oldKey, String newKey) {
		redisTemplate.rename(oldKey, newKey);
	}

	/**
	 * newKey不存在时才重命名
	 *
	 * @param oldKey
	 * @param newKey
	 * @return 修改成功返回true
	 */
	@Override
	public boolean renameKeyNotExist(String oldKey, String newKey) {
		return redisTemplate.renameIfAbsent(oldKey, newKey);
	}

	/**
	 * 删除key
	 *
	 * @param key
	 */
	@Override
	public void deleteKey(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 删除多个key
	 *
	 * @param keys
	 */
	@Override
	public void deleteKey(String... keys) {
		Set<String> kSet = Stream.of(keys).map(k -> k).collect(Collectors.toSet());
		redisTemplate.delete(kSet);
	}

	/**
	 * 删除Key的集合
	 *
	 * @param keys
	 */
	@Override
	public void deleteKey(Collection<String> keys) {
		Set<String> kSet = keys.stream().map(k -> k).collect(Collectors.toSet());
		redisTemplate.delete(kSet);
	}

	/**
	 * 设置key的生命周期
	 *
	 * @param key
	 * @param time
	 * @param timeUnit
	 */
	@Override
	public void expireKey(String key, long time, TimeUnit timeUnit) {
		redisTemplate.expire(key, time, timeUnit);
	}

	/**
	 * 指定key在指定的日期过期
	 *
	 * @param key
	 * @param date
	 */
	@Override
	public void expireKeyAt(String key, Date date) {
		redisTemplate.expireAt(key, date);
	}

	/**
	 * 查询key的生命周期
	 *
	 * @param key
	 * @param timeUnit
	 * @return
	 */
	@Override
	public long getKeyExpire(String key, TimeUnit timeUnit) {
		return redisTemplate.getExpire(key, timeUnit);
	}

	/**
	 * 将key设置为永久有效
	 *
	 * @param key
	 */
	@Override
	public void persistKey(String key) {
		redisTemplate.persist(key);
	}
}