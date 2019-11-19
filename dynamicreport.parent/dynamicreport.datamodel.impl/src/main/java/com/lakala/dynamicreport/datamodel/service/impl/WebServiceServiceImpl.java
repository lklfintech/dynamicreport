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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlImporter;
import com.eviware.soapui.model.iface.Operation;
import com.google.common.collect.Maps;
import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.datamodel.constants.DataModelConstants;
import com.lakala.dynamicreport.datamodel.model.WsMethodInfo;
import com.lakala.dynamicreport.datamodel.service.IWebServiceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * <p>
 * webservice接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service
public class WebServiceServiceImpl implements IWebServiceService{
	private static Logger log=LoggerFactory.getLogger(WebServiceServiceImpl.class);
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<WsMethodInfo> getMethodByUrl(String address) throws IOException, DocumentException{

		URL url = new URL(address);
		InputStream openStream = url.openStream();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();  
		byte[] buffer = new byte[2048];  
		int length = 0;  
		while((length = openStream.read(buffer)) != -1) {  
			bos.write(buffer, 0, length);//写入输出流  
		}  
		openStream.close();
		//把wsdl读取为字符串  为后续找参数类型做准备
		String wsdlStr=new String(bos.toByteArray(), "UTF-8");

		openStream.close();

		Map<String,Object> result = new HashMap<>();
		
		WsdlInterface[] wsdls = null ;
		try {
			WsdlProject project = new WsdlProject();//启动了 有个线程关闭不了
			wsdls = WsdlImporter.importWsdl(project, address);
		} catch (Exception  e1) {
			 throw new BusinessException(e1.getMessage());
		}
		

		WsdlInterface wsdl = wsdls[0];
		List<Operation> operationList = wsdl.getOperationList();
		for (int i = 0; i < operationList.size(); i++) {
			Operation operation = operationList.get(i);
			WsdlOperation op = (WsdlOperation) operation;
			Map<String,Object> tmp = new HashMap<>();
			tmp.put(op.getName(), op.createRequest(true));
			result.put(i+"", tmp);
		}

		if ((result != null) && (result.size() != 0)) {
			List<WsMethodInfo> importWsdl = new ArrayList<>();
			Set keySet = result.keySet();
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				WsMethodInfo info = new WsMethodInfo();
				List<String> inputType = new ArrayList<>();
				List<String> inputNames = new ArrayList<>();
				List<String> isparent = new ArrayList<>();
				String next = ""+ iterator.next();
				HashMap hashMap = (HashMap) result.get(next);
				Set<String> keySet2 = hashMap.keySet();
				Iterator<String> iterator2 = keySet2.iterator();
				if (iterator2.hasNext()) {
					String next2 = iterator2.next();
					String string = (String) hashMap.get(next2);
					//处理targetNameSpace
					String qname=string.substring(string.lastIndexOf("\"http://")+1,string.lastIndexOf("\">"));
					info.setTargetNameSpace(qname);

					String soap11 = "http://schemas.xmlsoap.org/soap/envelope";
					String soap12 = "http://www.w3.org/2003/05/soap-envelope";
					InputStreamReader is = new InputStreamReader(new ByteArrayInputStream(string.getBytes("utf-8")));
					BufferedReader ibr = new BufferedReader(is);
					String readLine = ibr.readLine();
					if (readLine != null) {
						if (readLine.indexOf(soap11) >= 0)
							info.setTargetXsd("11");
						else if (readLine.indexOf(soap12) >= 0) {
							info.setTargetXsd("12");
						}
					}
					ibr.close();
					is.close();

					info.setMethodSoapAction(string);

					Document read = DocumentHelper.parseText(string);
					Element rootElement = read.getRootElement();
					List<Element> elements = rootElement.elements();
					for (Element element : elements) {
						if ("Body".equals(element.getName())) {
							List<Element> elements2 = element.elements();
							info.setMethodName(next2);
							for (Element element2 : elements2) {
								getParameter(element2, 1, 1, inputType, inputNames, isparent);
								info.setInputNames(inputNames);
								info.setInputType(inputType);
								info.setOutputType(isparent);
							}
						}
					}

					importWsdl.add(info);
				}
			}

			for(WsMethodInfo infos:importWsdl){
				List<String> typpes=new ArrayList<>();
				List<String> names=new ArrayList<>();
				for(String input:infos.getInputNames()){
					if(input.contains(":"))
						input=input.split(":")[1];

					names.add(input);

					try {
						String strr=wsdlStr.substring(wsdlStr.indexOf("\""+input+"\"")+input.length()+2);
						String str2=strr.substring(0,strr.indexOf("/>"));
						String[] str3=str2.split("\"");
						String type=str3[1].split(":")[1];
						typpes.add(type);
					} catch (Exception e) {
						typpes.add("unknown");
						log.error("接口【{}】 寻找参数类型失败:【{}】",address,e.getMessage());
					}
				}
				infos.setInputType(typpes);
				infos.setInputNames(names);
			}
			return importWsdl;
		}
		throw new BusinessException("wsdl接口无数据");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void getParameter(Element element2, int gen, int genParent, List<String> inputType, List<String> inputNames,
			List<String> isparent) {
		if (element2 != null) {
			List<Element> elements3 = element2.elements();
			if ((elements3 != null) && (CollectionUtils.isNotEmpty(elements3)))
				for (Element element : elements3) {
					inputType.add(gen + "," + genParent);
					inputNames.add(element.getQualifiedName());
						List e = element.elements();
						if ((e != null) && (CollectionUtils.isNotEmpty(e))) {
							isparent.add("1");
							int gen1 = gen + gen;
							getParameter(element, gen1, gen, inputType, inputNames, isparent);
						} else {
							isparent.add("0");
						}
				}
		}
	}

	@Override
	public String executeService(String params) {

		JSONArray jsArr=JSONArray.parseArray(params);

		String url="";
		String method="";
		List<Map<String,String>> ls=new ArrayList<>();

		int i=0;
		for(Object obj:jsArr){
			JSONObject jsObj=JSONObject.parseObject(String.valueOf(obj));
			String value=jsObj.getString(DataModelConstants.VALUE);
			if(i==0){
				url=value;
			}else if(i==1){
				method=value;
			}else{
				Map<String,String> map=new HashMap<>();
				map.put("type",jsObj.getString("type"));
				map.put(DataModelConstants.VALUE,value);
				ls.add(map);
			}
			i++;
		}
		return callWsService(url, method, ls);
	}

	private String callWsService(String url,String method,List<Map<String, String>> ls){

		if(ls==null)ls=new ArrayList<>();
		Object[] obj=new Object[ls.size()];
		int j=0;
		for(Map<String,String> map:ls){
			String type=map.get("type");
			String value=map.get(DataModelConstants.VALUE);
			obj=transferParam(type, value, obj,j++);
		}

		Client client = dcf.createClient(url);
		Object[] objects = new Object[0];
		try {
			if(obj.length==0){
				objects = client.invoke(method);
			}else{
				objects = client.invoke(method,obj);
			}
		} catch (Exception e) {
			log.error(String.format("调用服务【%s】,方法【%s】失败,参数【%s】,原因:【%s】", url,method,ls.toString(),e.getMessage()));
			throw new BusinessException(String.format("调用服务【%s】,方法【%s】失败,参数【%s】,原因:【%s】", url,method,ls.toString(),e.getMessage()));			
		}finally{
			try {
				client.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		return JSONObject.toJSONString(objects[0]);
	}


	private static Object[] transferParam(String type,String value,Object[] obj,int j){
		try {
			if("string".equalsIgnoreCase(type)||"unknow".equalsIgnoreCase(type)||"null".equalsIgnoreCase(type)||StringUtils.isEmpty(type)){
				obj[j]=value;
				return obj;
			}
			if("int".equalsIgnoreCase(type)||"integer".equalsIgnoreCase(type)){
				obj[j]=Integer.valueOf(value);
				return obj;
			}
			if("decimal".equalsIgnoreCase(type)){
				obj[j]=BigDecimal.valueOf(Double.valueOf(value));
				return obj;
			}
			if("float".equalsIgnoreCase(type)){
				obj[j]=Float.valueOf(value);
				return obj;
			}
			if("long".equalsIgnoreCase(type)){
				obj[j]=Long.valueOf(value);
				return obj;
			}
			if("array".equalsIgnoreCase(type)){
				obj[j]=value.split(",");
				return obj;
			}
			if("double".equalsIgnoreCase(type)){
				obj[j]=Double.valueOf(value);
				return obj;
			}
			if("boolean".equalsIgnoreCase(type)){
				obj[j]=Boolean.valueOf(value);
				return obj;
			}

			obj[j]="";
			return obj;
		} catch (Exception e) {
			log.error(String.format(String.format("转型失败【%s】==>【%s】,【%s】",value,type,e.getMessage())));
			throw new BusinessException(String.format("转型失败【%s】==>【%s】,【%s】",value,type,e.getMessage()));
		}
	}

	static JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();

	@Override
	public Map<String,Object> callWS(String url,String method,Object[] params,String sid){

		Client client = dcf.createClient(url);
		Object[] objects = new Object[0];
		try {
			if(params.length==0){
				objects = client.invoke(method);
			}else{
				objects = client.invoke(method,params);
			}
		} catch (Exception e) {
			log.error(String.format("SID【%s】,调用服务【%s】,方法【%s】失败,参数【%s】,原因:【%s】",sid, url,method,Arrays.toString(params),e.getMessage()));
			throw new BusinessException(String.format("SID【%s】,调用服务【%s】,方法【%s】失败,参数【%s】,原因:【%s】",sid, url,method,Arrays.toString(params),e.getMessage()));			
		}finally{
			try {
				client.close();
			} catch (Exception e) {
					log.error(e.getMessage());
			}
		}
		if(objects[0] instanceof List){
			List<String> ls=JSONArray.parseArray(String.valueOf(objects[0]),String.class);
			if(CollectionUtils.isEmpty(ls))
				return Maps.newHashMap();
			return JSONObject.parseObject(ls.get(0));
		}
		if(objects[0] instanceof Map){
			return JSONObject.parseObject(JSONObject.toJSONString(objects[0]));
		}
		return JSONObject.parseObject(String.valueOf(objects[0]));
	}
 

}

