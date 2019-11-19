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
package com.lakala.dynamicreport.common.constants;

/**
 * <p>
 * 动态配置化常量,通过Application启动时赋值
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class DynamicConstants {

	private static class SingletonHolder {
		private static final DynamicConstants INSTANCE = new DynamicConstants();

	}

	private DynamicConstants() {

	}

	public static final DynamicConstants getInstance() {
		return SingletonHolder.INSTANCE;

	}

	/**
	 * redis前缀
	 */
	private String redisPrefix = "";

	public String getRedisPrefix() {
		return redisPrefix;
	}

	public void setRedisPrefix(String redisPrefix) {
		this.redisPrefix = redisPrefix;
	}

	public String getEnvRedisPrefix() {
		String prefix = GlobalConstants.REDIS_CACHE_PREFIX + redisPrefix;
		return prefix;
	}

	/**
	 * 分布式ID生成器workerId
	 */
	private String workerId;

	/**
	 * 分布式ID生成器datacenterId
	 */
	private String datacenterId;

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
	}

}
