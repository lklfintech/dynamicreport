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

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultSysLog;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.common.utils.DateUtils;
import com.lakala.dynamicreport.system.model.SysLog;
import com.lakala.dynamicreport.system.query.SysLogQuery;
import com.lakala.dynamicreport.system.repository.ISysLogRepository;
import com.lakala.dynamicreport.system.service.ISysLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 日志服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "sysLogServiceImpl")
public class SysLogServiceImpl extends BaseServiceImpl<ISysLogRepository,SysLog, Long> implements ISysLogService {

	@Autowired
	ISysLogRepository sysLogRepository;
	
	private final Logger log = LoggerFactory.getLogger(SysLogServiceImpl.class);

 

    @Override
    public Page<SysLog> findUserCriteria(SysLogQuery sysLogQuery) {
        return sysLogRepository.findAll(specification(sysLogQuery), PageForList.getPageable("id",sysLogQuery));
    }

    @Override
    public List<SysLog> findSysLog(SysLogQuery sysLogQuery) {
        return sysLogRepository.findAll(specification(sysLogQuery));
    }

    /**
     * 拼凑查询条件
     * @throws ParseException 
     */
    private Specification<SysLog> specification(final SysLogQuery sysLogQuery)  {
        List<Predication> list = Lists.newArrayList();
        if (null != sysLogQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"url",sysLogQuery.getUlr(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"userName",sysLogQuery.getUserName(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"ip",sysLogQuery.getIp(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"url",sysLogQuery.getAction(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"type",sysLogQuery.getType(),String.class,OperationEnum.AND));
            //时间区间between
            if (sysLogQuery.getStartDateTime()!=null && sysLogQuery.getEndDateTime()!=null) {
                String[] timeArr = new String[2];
                timeArr[0] = DateUtils.getTimestamp(sysLogQuery.getStartDateTime());
                timeArr[1] = DateUtils.getTimestamp(sysLogQuery.getEndDateTime());
                list.add(Predication.get(OperationEnum.BETWEEN,"createDate", JSONObject.toJSONString(timeArr),Timestamp.class,OperationEnum.AND));
            }
            list.add(Predication.get(OperationEnum.LIKE,"userName",sysLogQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"ip",sysLogQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"message",sysLogQuery.getSearchText(),String.class,OperationEnum.OR));
        }
         return SpecificationUtil.whereAndOr(list);

    }

	@Override
    @Transactional
	public SysLog save(ResultSysLog resultSysLog) {
		SysLog sysLog=new SysLog();
		BeanUtils.copyProperties(resultSysLog,sysLog);
		return sysLogRepository.save(sysLog);
	}
	 
}
