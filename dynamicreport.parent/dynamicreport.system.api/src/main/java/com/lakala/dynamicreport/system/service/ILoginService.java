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
package com.lakala.dynamicreport.system.service;

import com.lakala.dynamicreport.common.model.ActiveUser;

/**
 * <p>
 * 登录接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@FunctionalInterface
public interface ILoginService {

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	ActiveUser getUser(String username, String password);
}
