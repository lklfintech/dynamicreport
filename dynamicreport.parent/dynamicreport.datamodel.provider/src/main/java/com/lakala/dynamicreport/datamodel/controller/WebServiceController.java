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

import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.datamodel.model.WsMethodInfo;
import com.lakala.dynamicreport.datamodel.service.IWebServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * webservice服务controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Api(tags = "webservice服务管理",description = "webservice服务控制器(WebServiceController)")
@RestController
@RequestMapping("/web-service")
public class WebServiceController extends BaseController {

	private Logger log=LoggerFactory.getLogger(WebServiceController.class);
	@Autowired
	private IWebServiceService webServiceService;


	@ApiOperation(value = "获取方法集合")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "wdslURL",value = "webServiceUrl地址",required = true)
	})
	@GetMapping(value = "/method-by-url")
	public ResultJson  getMethodByUrl(String wdslURL){
		ResultJson rj=new ResultJson();
		try {
			if(!wdslURL.toUpperCase().endsWith("?WSDL"))
				return failure("WSDL接口错误");
			List<WsMethodInfo> infos= webServiceService.getMethodByUrl(wdslURL);
			rj.setObj(infos);
		} catch (Exception e) {
			rj.setSuccess(false);
			rj.setMsg("调用失败:"+e.getMessage());
			log.error("{}调用失败:{}",wdslURL,e.getMessage());
		}
		return rj;
	}

	@ApiOperation(value = "服务执行参数")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "params",value = "请求参数",required = true)
	})
	@PostMapping(value = "/execute-service")
	public ResultJson executeService(String params) {
		try {
			String res=webServiceService.executeService(params);
			return success(res);
		} catch (Exception e) {
			log.error("{}调用服务失败:{}",params,e.getMessage());
			return failure("调用服务失败:"+e.getMessage());
		}
		
	}

}
