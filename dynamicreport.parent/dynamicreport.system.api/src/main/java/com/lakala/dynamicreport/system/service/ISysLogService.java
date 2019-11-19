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

import com.lakala.dynamicreport.common.model.ResultSysLog;
import com.lakala.dynamicreport.common.service.IBaseService;
import com.lakala.dynamicreport.system.model.SysLog;
import com.lakala.dynamicreport.system.query.SysLogQuery;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * <p>
 * 日志服务接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface ISysLogService extends IBaseService<SysLog, Long>{

	/**
	 * 参数带条件分页查询
	 * 
	 * @param sysLogQuery
	 * @return
	 */
	Page<SysLog> findUserCriteria(SysLogQuery sysLogQuery);

	/**
	 * 参数带条件查询
	 * 
	 * @param sysLogQuery
	 * @return
	 */
	List<SysLog> findSysLog(SysLogQuery sysLogQuery);

	/**
	 * 新增
	 * 
	 * @param sysLog
	 * @return
	 */
	SysLog save(ResultSysLog sysLog);

}
