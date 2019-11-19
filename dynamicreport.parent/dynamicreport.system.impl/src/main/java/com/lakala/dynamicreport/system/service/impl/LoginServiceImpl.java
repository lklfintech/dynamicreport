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
package com.lakala.dynamicreport.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.service.ILoginService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import com.lakala.dynamicreport.system.utils.UserConvertPwd;

/**
 * <p>
 * 登录服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "loginService")
public class LoginServiceImpl implements ILoginService {

	@Autowired
	ISystemUserService systemUserService;



	@Override
	public ActiveUser getUser(String username, String password) {
		ActiveUser userDetail = null;
		User user = systemUserService.findOne(username);
		if (user != null && StatusConstants.ACTIVE.equalsIgnoreCase(user.getStatus()) && user.getPassword().equals(UserConvertPwd.encrypt(password))) {
			userDetail = new ActiveUser();
			userDetail.setUsername(username);
			userDetail.setPassword(password);
		}
		return userDetail;
	}
}
