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

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redis服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IRedisService {

	 boolean set(final String key, final String value);

	 String get(final String key);

	 boolean expire(final String key, long expire);

	 <T> boolean setList(String key, List<T> list);

	 <T> List<T> getList(String key, Class<T> clz);

	 long lpush(final String key, Object obj);

	 long rpush(final String key, Object obj);

	 String lpop(final String key);

	 boolean existsKey(String key);

	/**
	 * 自增 key不存在则为1 key存在则自增
	 * 
	 * @param key
	 */
	 Long incrby(final String key);

	/**
	 * 自增不存在数值会默认初始化0然后加1，传入时间后自动销毁
	 * 
	 * @param key
	 * @param liveTime
	 */
	 Long incrby(final String key, final long liveTime);

	/**
	 * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
	 *
	 * @param oldKey
	 * @param newKey
	 */
	 void renameKey(String oldKey, String newKey);

	/**
	 * newKey不存在时才重命名
	 *
	 * @param oldKey
	 * @param newKey
	 * @return 修改成功返回true
	 */
	 boolean renameKeyNotExist(String oldKey, String newKey);

	/**
	 * 删除key
	 *
	 * @param key
	 */
	 void deleteKey(String key);

	/**
	 * 删除多个key
	 *
	 * @param keys
	 */
	 void deleteKey(String... keys);

	/**
	 * 删除Key的集合
	 *
	 * @param keys
	 */
	 void deleteKey(Collection<String> keys);

	/**
	 * 设置key的生命周期
	 *
	 * @param key
	 * @param time
	 * @param timeUnit
	 */
	 void expireKey(String key, long time, TimeUnit timeUnit);

	/**
	 * 指定key在指定的日期过期
	 *
	 * @param key
	 * @param date
	 */
	 void expireKeyAt(String key, Date date);

	/**
	 * 查询key的生命周期
	 *
	 * @param key
	 * @param timeUnit
	 * @return
	 */
	 long getKeyExpire(String key, TimeUnit timeUnit);

	/**
	 * 将key设置为永久有效
	 *
	 * @param key
	 */
	 void persistKey(String key);
}
