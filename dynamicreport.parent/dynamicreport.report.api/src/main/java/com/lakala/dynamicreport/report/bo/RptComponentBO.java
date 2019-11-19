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
package com.lakala.dynamicreport.report.bo;

import com.lakala.dynamicreport.report.model.CfgComponent;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报表组件BO
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "报表组件BO",description = "报表组件BO(RptComponentBO)")
public class RptComponentBO implements Serializable {

    @ApiModelProperty(value = "报表组件",name="component")
    private CfgComponent component;

    @ApiModelProperty(value = "组件所有字段",name="componentColList")
    private List<CfgComponentCol> componentColList;

    @ApiModelProperty(value = "维度字段",name="dimensionList")
    private List<CfgComponentCol> dimensionList;

    @ApiModelProperty(value = "指标字段",name="targetList")
    private List<CfgComponentCol> targetList;

    @ApiModelProperty(value = "透视维度字段",name="pivotDimensionList")
    private List<CfgComponentCol> pivotDimensionList;

    @ApiModelProperty(value = "透视指标字段",name="pivotTargetList")
    private List<CfgComponentCol> pivotTargetList;

    @ApiModelProperty(value = "组件数据",name="dataList")
    private List<Map<String, Object>> dataList;

    public CfgComponent getComponent() {
        return component;
    }

    public void setComponent(CfgComponent component) {
        this.component = component;
    }

    public List<CfgComponentCol> getComponentColList() {
        return componentColList;
    }

    public void setComponentColList(List<CfgComponentCol> componentColList) {
        this.componentColList = componentColList;
    }

    public List<CfgComponentCol> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(List<CfgComponentCol> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public List<CfgComponentCol> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<CfgComponentCol> targetList) {
        this.targetList = targetList;
    }

    public List<CfgComponentCol> getPivotDimensionList() {
        return pivotDimensionList;
    }

    public void setPivotDimensionList(List<CfgComponentCol> pivotDimensionList) {
        this.pivotDimensionList = pivotDimensionList;
    }

    public List<CfgComponentCol> getPivotTargetList() {
        return pivotTargetList;
    }

    public void setPivotTargetList(List<CfgComponentCol> pivotTargetList) {
        this.pivotTargetList = pivotTargetList;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
}
