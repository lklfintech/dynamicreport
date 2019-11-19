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
package com.lakala.dynamicreport.datamodel.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.datamodel.model.DobbuCallRequest;
import com.lakala.dynamicreport.datamodel.service.IDubboGenericService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * dubbo服务controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Api(tags = "dubbo服务管理",description = "dubbo服务控制器(DubboGenericInvokeController)")
@RestController
@RequestMapping("/dubbo-generic")
public class DubboGenericInvokeController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DubboGenericInvokeController.class);

	@Autowired
	private IDubboGenericService dubboGenericService;
	 
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "泛化调用dubbo")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "json",value = "JSON格式请求参数",required = true)
	})
	@PostMapping(value = "/generic-invoke")
	public ResultJson genericInvoke(String json) {
		log.info("请求Dubbo服务参数:{}",json);
		ResultJson rj=new ResultJson();
		List<String> list=JSONArray.parseArray(json,String.class);
		if(CollectionUtils.isEmpty(list)){
			rj.setObj("请检查各输入项");
			return rj;
		}
		Map<String ,String> allDataMap=new LinkedHashMap<>();
		
		List<Object> param=Lists.newArrayList();
		List<String> paramType=new ArrayList<>();
		
		for(String str:list){
			Map<String,String> map=JSONObject.parseObject(str,Map.class);
			allDataMap.put(map.get("name"),map.get("value"));
		}
		
		for(String key:allDataMap.keySet()){
			if(key.indexOf("type_")>-1){
				paramType.add(allDataMap.get(key));
				param.add(allDataMap.get(key.replace("type_", "param_")));
			}
		}
		
		DobbuCallRequest request=new DobbuCallRequest();
		request.setAddress(allDataMap.get("address"));
		request.setInterfaceName(allDataMap.get("interfaceName"));
		request.setMethod(allDataMap.get("method"));
		request.setParam(param);
		request.setParamType(paramType);
		
		request.setVersion(allDataMap.get("version"));
		request.setApplicationName(allDataMap.get("applicationName"));
		request.setProtocolName(allDataMap.get("protocolName"));
		request.setProtocolPort(allDataMap.get("protocolPort"));
		request.setServiceGroup(allDataMap.get("serviceGroup"));
		
		try {
			String result=dubboGenericService.genericInvoke(request);
			rj.setObj(result);
			return rj;
		} catch (Exception e) {
			
			String msg="请检查参数类型以及参数个数是否符合接口规范</br>";
			rj.setObj(msg+e.getMessage());
			log.error(String.format("参数【%s】,错误信息【%s】", json,msg+e.getCause()));
			return rj;
		}
	}
}
