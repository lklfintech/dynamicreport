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

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  webservice方法详情对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class WsMethodInfo {
    private String methodName;
    private String methodDesc;
    private Map<String, Map> methodName2InputParam = new HashMap();
    private String targetNameSpace;
    private String methodSoapAction;
    private String endPoint;
    private String targetXsd;
    private List<String> inputNames;
    private List<String> inputType;
    private List<String> inputDesc;
    private List<String> outputNames;
    private List<String> outputType;
    private String sep = "#";


    //入参数
    private String wdslURL;
    private String name;
    private String value;

    public String getWdslURL() {
		return wdslURL;
	}

	public void setWdslURL(String wdslURL) {
		this.wdslURL = wdslURL;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getInputNames() {
        return this.inputNames;
    }

    public void setInputNames(List<String> inputNames) {
        this.inputNames = inputNames;
    }

    public Map<String, Map> getMethodName2InputParam() {
        return this.methodName2InputParam;
    }

    public void setMethodName2InputParam(Map<String, Map> methodName2InputParam) {
        this.methodName2InputParam = methodName2InputParam;
    }

    public String getTargetNameSpace() {
        return this.targetNameSpace;
    }

    public void setTargetNameSpace(String targetNameSpace) {
        this.targetNameSpace = targetNameSpace;
    }

    public String getMethodSoapAction() {
        return this.methodSoapAction;
    }

    public void setMethodSoapAction(String methodSoapAction) {
        this.methodSoapAction = methodSoapAction;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTargetXsd() {
        return this.targetXsd;
    }

    public void setTargetXsd(String targetXsd) {
        this.targetXsd = targetXsd;
    }

    public List<String> getOutputNames() {
        return this.outputNames;
    }

    public void setOutputNames(List<String> outputNames) {
        this.outputNames = outputNames;
    }

    public List<String> getInputType() {
        return this.inputType;
    }

    public void setInputType(List<String> inputType) {
        this.inputType = inputType;
    }

    public List<String> getOutputType() {
        return this.outputType;
    }

    public void setOutputType(List<String> outputType) {
        this.outputType = outputType;
    }

    public List<String> getInputDesc() {
        return this.inputDesc;
    }

    public void setInputDesc(List<String> inputDesc) {
        this.inputDesc = inputDesc;
    }

    public String madeNewString() {
        StringBuilder su = new StringBuilder();
        su.append(this.methodName);
        su.append(this.sep);
        su.append(this.inputType == null ? "" : StringUtils.join(this.inputType.toArray(), "@"));
        su.append(this.sep);
        su.append(this.inputNames == null ? "" : StringUtils.join(this.inputNames.toArray(), "@"));
        su.append(this.sep);
        su.append(this.methodDesc == null ? "" : this.methodDesc);
        su.append(this.sep);
        su.append(this.sep);
        su.append(this.methodSoapAction == null ? "" : this.methodSoapAction);
        su.append(this.sep);
        su.append(this.outputType == null ? "" : StringUtils.join(this.outputType.toArray(), "@"));
        su.append(this.sep);
        su.append(this.targetXsd == null ? "" : this.targetXsd);
        return su.toString();
    }

    public String getMethodDesc() {
        return this.methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }
}
