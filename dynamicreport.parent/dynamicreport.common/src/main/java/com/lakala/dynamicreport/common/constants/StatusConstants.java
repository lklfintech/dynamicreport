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
 * 状态常量类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class StatusConstants {

	/**
	 * 启用
	 */
	public static final String ACTIVE = "ACTIVE";

	/**
	 * 失效
	 */
	public static final String INACTIVE = "INACTIVE";
	/**
	 * 是
	 */
	public final static String YES = "是";

	/**
	 * 否
	 */
	public final static String NE = "否";

	/**
	 * 是
	 */
	public final static String YES_FLAG = "Y";

	/**
	 * 否
	 */
	public final static String NO_FLAG = "N";

	// hide public constructors
	private StatusConstants() {
		throw new IllegalStateException("Status Constants class");
	}
}
