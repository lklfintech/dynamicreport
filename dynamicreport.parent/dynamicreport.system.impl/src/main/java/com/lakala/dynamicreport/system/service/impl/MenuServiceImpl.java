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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.system.model.Menu;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.MenuQuery;
import com.lakala.dynamicreport.system.repository.IMenuRepository;
import com.lakala.dynamicreport.system.repository.IRoleRepository;
import com.lakala.dynamicreport.system.service.IMenuService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "menuService")
public class MenuServiceImpl extends BaseServiceImpl<IMenuRepository,Menu,Long> implements IMenuService {

	@Autowired
	IMenuRepository menuRepository;
 
	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	ISystemUserService systemUserService;
 
 
	@Override
	public List<Menu> findParent(MenuQuery menuQuery) {
		return menuRepository.findTopMenus();
	}

	@Override
	@Transactional
	public void deleteByFunction(String function) {
		menuRepository.deleteByFunction(function);
	}

	@Override
	public List<TreeNode> getTreeWithOutReport(List<Menu> menuList, List<String> menuIds) {
		List<TreeNode> trees = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(menuList)) {
			for (Menu menu : menuList) {
				if("report".equalsIgnoreCase(menu.getSystemFunction().getType())){
					continue;
				}
				TreeNode node = new TreeNode();
				node.setText(menu.getName());
				node.setId(menu.getId().toString());
				node.setUrl(menu.getPath());
				node.setIcon(menu.getIcon());
				if (CollectionUtils.isNotEmpty(menuIds) && menuIds.contains(String.valueOf(menu.getId()))) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("checked", Boolean.TRUE);
					node.setState(map);
				}
				if (CollectionUtils.isNotEmpty(menu.getChildMenu())) {
					node.setNodes(getTreeWithOutReport(menu.getChildMenu(), menuIds));
				}
				trees.add(node);
			}
		}
		return trees;
	}

	@Override
	public List<TreeNode> getTree(List<Menu> menuList, List<String> menuIds) {
		List<TreeNode> trees = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(menuList)) {
			for (Menu menu : menuList) {
				TreeNode node = new TreeNode();
				node.setText(menu.getName());
				node.setId(menu.getId().toString());
				node.setUrl(menu.getPath());
				node.setIcon(menu.getIcon());
				if (CollectionUtils.isNotEmpty(menuIds) && menuIds.contains(String.valueOf(menu.getId()))) {
					Map<String, Object> map = Maps.newHashMap();
					map.put("checked", Boolean.TRUE);
					node.setState(map);
				}
				if (CollectionUtils.isNotEmpty(menu.getChildMenu())) {
					node.setNodes(getTree(menu.getChildMenu(), menuIds));
				}
				trees.add(node);
			}
		}
		return trees;
	}

	@Override
	public List<TreeNode> findUserMenu() {
		List<TreeNode> trees = Lists.newArrayList();
		Session session = SecurityUtils.getSubject().getSession();
		ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		List<String> functionList = Lists.newArrayList();
		if (userInfo != null) {
			String username = userInfo.getUsername();
			User user = systemUserService.findOne(username);
			List<Role> roles = user.getRoles();
			if (CollectionUtils.isNotEmpty(roles)) {
				addFunctionList(roles, functionList);
			}
			userInfo.setPermissions(functionList);
		}
		MenuQuery menuQuery = new MenuQuery();
		menuQuery.setStatus(StatusConstants.ACTIVE);
		List<Menu> menuList = findParent(menuQuery);
		trees = getTreeNode(menuList, functionList);
		return trees;
	}

	/**
	 * 获取栏目节点
	 *
	 * @param menuList
	 * @param functionIds
	 * @return
	 */
	public List<TreeNode> getTreeNode(List<Menu> menuList, List<String> functionIds) {
		List<TreeNode> trees = Lists.newArrayList();

		if (CollectionUtils.isEmpty(menuList))
			return trees;

		for (Menu menu : menuList) {
			if (CollectionUtils.isNotEmpty(functionIds) && functionIds.contains(menu.getSystemFunction().getId())) {
				TreeNode node = new TreeNode();
				node.setText(menu.getName());
				node.setId(menu.getId().toString());
				node.setUrl(menu.getPath());
				node.setIcon(menu.getIcon());
				if (CollectionUtils.isNotEmpty(menu.getChildMenu())) {
					node.setNodes(getTreeNode(menu.getChildMenu(), functionIds));
				}
				trees.add(node);
			}
		}

		return trees;
	}

	/**
	 * 添加用户权限列表
	 *
	 * @param roles
	 * @param functionList
	 */
	public void addFunctionList(List<Role> roles, List<String> functionList) {
		for (Role role : roles) {
			List<SystemFunction> functions = role.getSystemFunctions();
			if (CollectionUtils.isEmpty(functions)||!StatusConstants.ACTIVE.equalsIgnoreCase(role.getStatus())) {
				continue;
			}
			for (SystemFunction function : functions) {
				if (StatusConstants.ACTIVE.equalsIgnoreCase(function.getStatus())) {
					functionList.add(function.getId());
				}
			}
		}
	}
}
