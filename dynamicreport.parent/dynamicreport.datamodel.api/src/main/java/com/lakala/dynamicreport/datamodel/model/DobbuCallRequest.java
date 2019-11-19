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
package com.lakala.dynamicreport.datamodel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * dubbo接口服务对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "dubbo接口服务对象",description = "dubbo接口服务对象(DobbuCallRequest)")
public class DobbuCallRequest implements Serializable{

	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "地址",name="address")
	private String address;

	@ApiModelProperty(value = "接口名称",name="interfaceName")
	private String interfaceName;

	@ApiModelProperty(value = "方法名",name="method")
    private String method;

	@ApiModelProperty(value = "版本号",name="version")
    private String version;

	@ApiModelProperty(value = "服务组",name="serviceGroup")
    private String serviceGroup;

	@ApiModelProperty(value = "会话名称",name="applicationName")
    private String applicationName;

	@ApiModelProperty(value = "协议名称",name="protocolName")
    private String protocolName;

	@ApiModelProperty(value = "协议端口号",name="protocolPort")
    private String protocolPort;

	@ApiModelProperty(value = "参数类型",name="paramType")
    private List<String> paramType;
    private transient List<Object> param;
    private transient Map<String,List<Map<String,String>>> paramMap;
 
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getParamType() {
		return paramType;
	}

	public void setParamType(List<String> paramType) {
		this.paramType = paramType;
	}

	public List<Object> getParam() {
		return param;
	}

	public void setParam(List<Object> param) {
		this.param = param;
	}

	public Map<String, List<Map<String, String>>> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, List<Map<String, String>>> paramMap) {
		this.paramMap = paramMap;
	}

	public String getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(String serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolPort() {
		return protocolPort;
	}

	public void setProtocolPort(String protocolPort) {
		this.protocolPort = protocolPort;
	}

	@Override
    public String toString() {
        return "DobbuCallRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", method='" + method + '\'' +
                ", paramType=" + paramType +
                ", param=" + param +
                ", address='" + address + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
