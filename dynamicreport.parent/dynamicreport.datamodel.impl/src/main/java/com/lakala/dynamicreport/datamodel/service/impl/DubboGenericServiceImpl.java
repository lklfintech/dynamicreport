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
package com.lakala.dynamicreport.datamodel.service.impl;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSONObject;
import com.lakala.dynamicreport.datamodel.model.DobbuCallRequest;
import com.lakala.dynamicreport.datamodel.service.IDubboGenericService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * dubbo服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class DubboGenericServiceImpl implements IDubboGenericService{

	@Override
	public String genericInvoke(DobbuCallRequest dobbuCallRequest) {

		// 普通编码配置方式
		ApplicationConfig application = new ApplicationConfig();
		if(StringUtils.isBlank(dobbuCallRequest.getApplicationName())){
			application.setName("feature-consumer");
		}else{
			application.setName(dobbuCallRequest.getApplicationName());
		}

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress(dobbuCallRequest.getAddress());

		if(StringUtils.isNotBlank(dobbuCallRequest.getProtocolName())){
			registry.setProtocol(dobbuCallRequest.getProtocolName());
		}
		if(StringUtils.isNotBlank(dobbuCallRequest.getServiceGroup())){
			registry.setGroup(dobbuCallRequest.getServiceGroup());
		}

		ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
		reference.setApplication(application);
		reference.setRegistry(registry);

		if(StringUtils.isNotBlank(dobbuCallRequest.getVersion())){
			reference.setVersion(dobbuCallRequest.getVersion());
		}

		reference.setInterface(dobbuCallRequest.getInterfaceName());
		reference.setGeneric(true); // 声明为泛化接口

		ReferenceConfigCache cache = ReferenceConfigCache.getCache();
		GenericService genericService = cache.get(reference);
		if(genericService==null){//提供者重新启动后,实例可能为空,重新获取
			cache.destroyAll();
			cache = ReferenceConfigCache.getCache();
			genericService = cache.get(reference);
		}

		List<String> types=dobbuCallRequest.getParamType();
		List<Object> params=dobbuCallRequest.getParam();
		String[] typeArr=new String[types.size()];
		for(int i=0;i<types.size();i++){
			typeArr[i]=parseParameter(types.get(i));
		}

		Object result = genericService.$invoke(dobbuCallRequest.getMethod(),typeArr,params.toArray());
		if(result instanceof String){
			return String.valueOf(result);
		}else{
			return JSONObject.toJSONString(result);
		}
	}

	public String parseParameter(String type) {
		switch (type) {
		case "string":
			return  "java.lang.String";
		case "int":
			return  "java.lang.Integer";
		case "double":
			return  "java.lang.Double";
		case "short":
			return  "java.lang.Short";
		case "float":
			return  "java.lang.Float";
		case "long":
			return  "java.lang.Long";
		case "boolean":
			return  "java.lang.Boolean";
		case "char":
			return  "java.lang.Character";
		case "map":
			return  "java.util.Map";
		case "list":
			return  "java.util.List";	
		case "date":
			return  "java.util.Date";	
		default:
			return  "java.lang.String";
		}
	}

}
