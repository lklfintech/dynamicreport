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
package com.lakala.dynamicreport.datamodel.service;

import com.lakala.dynamicreport.datamodel.model.WsMethodInfo;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * webservice接口
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IWebServiceService {
	/**
	 * 根据URL 获取方法
	 *
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws Exception
	 */
	 List<WsMethodInfo> getMethodByUrl(String url) throws IOException, DocumentException;
	
	/**
	 * 执行ws接口
	 *
	 * @param params
	 * @return 
	 */
	 String executeService(String params);

	
	/**
	 *
	 *
	 * @param url
	 * @param method
	 * @param params
	 * @param sid 
	 * @return
	 */
	 Map<String,Object> callWS(String url, String method, Object[] params, String sid);
	
	
}
