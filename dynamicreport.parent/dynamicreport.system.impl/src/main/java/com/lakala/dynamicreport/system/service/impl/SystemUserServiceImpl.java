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
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.system.model.Role;
import com.lakala.dynamicreport.system.model.SystemFunction;
import com.lakala.dynamicreport.system.model.User;
import com.lakala.dynamicreport.system.query.RoleQuery;
import com.lakala.dynamicreport.system.query.SystemUserQuery;
import com.lakala.dynamicreport.system.query.UserQuery;
import com.lakala.dynamicreport.system.repository.ISystemUserRepository;
import com.lakala.dynamicreport.system.repository.IUserRoleRepository;
import com.lakala.dynamicreport.system.service.IRoleService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import com.lakala.dynamicreport.system.utils.UserConvertPwd;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "systemUserService")
public class SystemUserServiceImpl extends BaseServiceImpl<ISystemUserRepository,User, String> implements ISystemUserService {

	@Autowired
	ISystemUserRepository systemUserRepository;

	@Autowired
	IUserRoleRepository userRoleRepository;

	@Autowired
	IRoleService roleService;

	//登录授权请求URI筛选
	private static final String AUTH_CONTAINS_PATH = "/page/rptTemplate/index.html";


	@Override
	public List<User> findUser(SystemUserQuery systemUserQuery) {
		return systemUserRepository.findAll(specification(systemUserQuery));
	}

	@Override
	public Page<User> findUserCriteria(final SystemUserQuery systemUserQuery) {
		return systemUserRepository.findAll(specification(systemUserQuery), PageForList.getPageable("id",systemUserQuery));
	}

	@Override
	public List<User> findRoleUser(String currentUser) {
		return systemUserRepository.findRoleUserByGroupType(currentUser,"Rule_%");
	}

	@Override
	public List<String> getAllSameRoleUsers() {
		List<String> users = Lists.newArrayList();
		users = getRoleUsersByGroupType("Rule_%");
		return users;
	}

	@Override
	public List<String> getAllSameReportRoleUsers() {
		List<String> users = Lists.newArrayList();
		users = getRoleUsersByGroupType("Report_%");
		return users;
	}

	/**
	 * 根据角色组的某一类的角色查询
	 *	例: 其他用户只能看属于同一个角色组，并且角色组为Report_开头的角色
	 *
	 * @param roleGroup "Report_%"
	 * @return
	 */
	public List<String> getRoleUsersByGroupType(String roleGroup) {
		List<String> users = Lists.newArrayList();
		// 从session获取用户信息
		Session session = SecurityUtils.getSubject().getSession();
		ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		if (!(userInfo != null && StringUtils.isNotBlank(userInfo.getUsername()))) {
			return users;
		}
		String userName = userInfo.getUsername();
		List<User> userList;
		// 管理员可以看所有数据
		if (GlobalConstants.USER_ADMIN.equalsIgnoreCase(userName)) {
			userList = systemUserRepository.findAll();
		} else {
			// 其他用户只能看属于同一个角色组，并且角色组为不同类型(Report_)开头的角色
			users.add(userInfo.getUsername());
			userList = systemUserRepository.findRoleUserByGroupType(userInfo.getUsername(),roleGroup);
		}
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				if (!users.contains(user.getId())) {
					users.add(user.getId());
				}
			}
		}
		return users;
	}

	@Override
	public String getLoginUser() {
		String userName = "";
		Session session = SecurityUtils.getSubject().getSession();
		ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		if (userInfo != null && StringUtils.isNotBlank(userInfo.getUsername())) {
			userName = userInfo.getUsername();
		}
		return userName;
	}

	/**
	 * 拼凑查询条件
	 */
	private Specification<User> specification(final SystemUserQuery systemUserQuery) {
		List<Predication> list  = Lists.newArrayList();
		if (null != systemUserQuery) {
				list.add(Predication.get(OperationEnum.EQUAL,"id",systemUserQuery.getId(),String.class,OperationEnum.AND));
				list.add(Predication.get(OperationEnum.EQUAL,"status",systemUserQuery.getStatus(),String.class,OperationEnum.AND));
				list.add(Predication.get(OperationEnum.LIKE,"name",systemUserQuery.getName(),String.class,OperationEnum.AND));
				list.add(Predication.get(OperationEnum.LIKE,"nickName",systemUserQuery.getNickName(),String.class,OperationEnum.AND));
				list.add(Predication.get(OperationEnum.LIKE,"name",systemUserQuery.getSearchText(),String.class,OperationEnum.OR));
				list.add(Predication.get(OperationEnum.LIKE,"nickName",systemUserQuery.getSearchText(),String.class,OperationEnum.OR));
		}
		return SpecificationUtil.whereAndOr(list);
	}

	/**
	 * 添加登录用户所拥有的角色到list<String>
	 *
	 * @param roles
	 * @param list
	 */
	public void addRoleList(List<Role> roles, List<String> list) {
		for (Role role : roles) {
			List<SystemFunction> functions = role.getSystemFunctions();
			if (CollectionUtils.isEmpty(functions)||!StatusConstants.ACTIVE.equalsIgnoreCase(role.getStatus())) {
				continue;
			}
			for (SystemFunction function : functions) {
				if (!StatusConstants.ACTIVE.equalsIgnoreCase(function.getStatus())) {
					continue;
				}
				list.add(function.getId());
			}
		}
	}

	@Override
	public String auth(String username, String password) {
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		currentUser.login(token);
		Session session = currentUser.getSession();
		SavedRequest savedRequest = (SavedRequest)session.getAttribute("shiroSavedRequest");
		ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		List<String> list = Lists.newArrayList();
		User user = findOne(username);
		List<Role> roles = user.getRoles();
		if (CollectionUtils.isNotEmpty(roles)) {
			addRoleList(roles, list);
			userInfo.setRoleName(roles.get(0).getName());
		}
		userInfo.setPermissions(list);
		session.setAttribute(GlobalConstants.SESSION_USER_INFO, userInfo);

		String successUrl = null;
		if(savedRequest != null && savedRequest.getRequestURI().contains(AUTH_CONTAINS_PATH)){
			if(savedRequest.getQueryString() != null){
				successUrl = savedRequest.getRequestURI()+"?"+savedRequest.getQueryString();
			}else{
				successUrl = savedRequest.getRequestURI();
			}
			session.removeAttribute("shiroSavedRequest");
		}else{
			successUrl = "index.html";
		}
		return successUrl;
	}

	@Override
	@Transactional
	public ResultJson modifyPwd(UserQuery user) {
		ResultJson result = new ResultJson();
		Session session = SecurityUtils.getSubject().getSession();
		ActiveUser userInfo = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);
		User userObj = findOne(userInfo.getUsername());
		if (userObj != null && userObj.getPassword().equals(UserConvertPwd.encrypt(user.getOldPassword()))) {
			userObj.setPassword(UserConvertPwd.encrypt(user.getPassword()));
			systemUserRepository.saveAndFlush(userObj);
			result.setSuccess(true);
			result.setMsg("修改用户密码成功！");
		} else {
			result.setSuccess(false);
			result.setMsg("用户旧密码错误！");
		}
		return result;
	}

	@Override
	@Transactional
	public void updateByParams(User systemUser, String pkId) {
		User systemUsersObj=findOne(pkId);
		systemUsersObj.setName(systemUser.getName());
		systemUsersObj.setNickName(systemUser.getNickName());
		if (!StringUtils.isEmpty(systemUser.getPassword())) {
			systemUsersObj.setPassword(UserConvertPwd.encrypt(systemUser.getPassword()));
		}
		systemUsersObj.setAddress(systemUser.getAddress());
		systemUsersObj.setStatus(systemUser.getStatus());
		systemUsersObj.setEmail(systemUser.getEmail());
		systemUsersObj.setPhone(systemUser.getPhone());
		systemUsersObj.setRemark(systemUser.getRemark());
		systemUsersObj.setBirthday(systemUser.getBirthday());
		systemUsersObj.setLocked(systemUser.getLocked());
		systemUsersObj.setSex(systemUser.getSex());
		systemUsersObj.setStatus(systemUser.getStatus());
		systemUserRepository.saveAndFlush(systemUsersObj);
	}

	@Override
	public List<Role> getRules(String id, String searchText) {
		if(StringUtils.isBlank(id)){
			id = getLoginUser();
		}
		User model = findOne(id);
		List<Role> roles = model.getRoles();
		List<Long> roleIds = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(roles)) {
			for (Role role : roles) {
				roleIds.add(role.getId());
			}
		}
		RoleQuery query = new RoleQuery();
		query.setSearchText(searchText);
		query.setStatus(StatusConstants.ACTIVE);
		List<Role> list =  roleService.findRole(query);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Role role : list) {
				if (roleIds.contains(role.getId())) {
					role.setSelected(Boolean.TRUE);
				} else {
					role.setSelected(Boolean.FALSE);
				}
			}
		}
		return list;
	}
}
