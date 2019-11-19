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
package com.lakala.dynamicreport.hibernate;

import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.BaseEntity;
import com.lakala.dynamicreport.common.utils.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * <p>
 * Hiberate拦截器：实现创建人，创建时间，创建人名称自动注入; 修改人,修改时间,修改人名自动注入;
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Component
public class HiberAspect extends EmptyInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(HiberAspect.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		// do nothing
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		if(logger.isDebugEnabled()) {
			logger.debug("invoke onFlushDirty:{}", entity.getClass().getName());
		}
		if (entity instanceof BaseEntity) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("modifiedDate".equals(propertyNames[i])) {
					currentState[i] = DateUtils.nowTimeStamp();
				} else if ("modifiedUser".equals(propertyNames[i])) {
					// 从session获取用户信息
					Session session = SecurityUtils.getSubject().getSession();
					ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
					if (userInfo != null) {
						currentState[i] = userInfo.getUsername();
					} else {
						currentState[i] = "System";
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return false;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if(logger.isDebugEnabled()) {
			logger.debug("invoke onSave:{}", entity.getClass().getName());
		}
		if (entity instanceof BaseEntity) {
			for (int i = 0; i < propertyNames.length; i++) {
				if ("createdDate".equals(propertyNames[i]) || "modifiedDate".equals(propertyNames[i])) {
					state[i] = DateUtils.nowTimeStamp();
				} else if ("createdUser".equals(propertyNames[i]) || "modifiedUser".equals(propertyNames[i])) {
					// 从session获取用户信息
					Session session = SecurityUtils.getSubject().getSession();
					ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
					if (userInfo != null) {
						state[i] = userInfo.getUsername();
					} else {
						state[i] = "System";
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		// do something after tran

	}
}
