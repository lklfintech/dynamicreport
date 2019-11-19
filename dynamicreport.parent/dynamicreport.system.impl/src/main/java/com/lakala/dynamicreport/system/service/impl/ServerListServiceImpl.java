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

import java.util.List;

import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.lakala.dynamicreport.system.model.ServerList;
import com.lakala.dynamicreport.system.query.ServerListQuery;
import com.lakala.dynamicreport.system.repository.ServerListRepository;
import com.lakala.dynamicreport.system.service.IServerListService;

/**
 * <p>
 * 角色服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "serverListService")
public class ServerListServiceImpl extends BaseServiceImpl<ServerListRepository,ServerList, Long> implements IServerListService {

	@Autowired
	ServerListRepository serverListRepository;

	@Override
	public List<ServerList> findServerList(ServerListQuery serverListQuery) {
		return serverListRepository.findAll(specification(serverListQuery));
	}

	@Override
	public Page<ServerList> findServerListCriteria(final ServerListQuery serverListQuery) {
		return serverListRepository.findAll(specification(serverListQuery), PageForList.getPageable("id",serverListQuery));
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<ServerList> specification(final ServerListQuery serverListQuery) {
		List<Predication> list = Lists.newArrayList();
		if (null != serverListQuery) {
			list.add(Predication.get(OperationEnum.LIKE,"ip",serverListQuery.getSearchText(),String.class,OperationEnum.AND));
		}
		return SpecificationUtil.where(list);
	}

}
