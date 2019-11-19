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
package com.lakala.dynamicreport.datamodel.query;

import com.lakala.dynamicreport.common.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * <p>
 * 报表查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "报表查询对象",description = "报表查询对象(ReportDataQuery)",
        parent = BaseQuery.class)
public class ReportDataQuery extends BaseQuery {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "查询的select语句",name="selectStatement")
	private String selectStatement;

	@ApiModelProperty(value = "查询的group by语句",name="groupByStatement")
    private String groupByStatement;

	@ApiModelProperty(value = "查询的order by语句",name="orderByStatement")
    private String orderByStatement;

	@ApiModelProperty(value = "查询参数",name="paramMap")
    private transient Map<String, Object> paramMap;

	@ApiModelProperty(value = "数据集ID",name="dataListId")
    private Long dataListId;

	@ApiModelProperty(value = "页面上选择的维度",name="dimensionIds")
    private String dimensionIds = null;

	@ApiModelProperty(value = "页面上选择的指标",name="targetIds")
    private String targetIds = null;

	@ApiModelProperty(value = "页面上显示的组件ID",name="componentId")
    private String componentId = null;

	@ApiModelProperty(value = "页面上显示的图形组件类型ID",name="chartTypeId")
    private String chartTypeId = null;

	@ApiModelProperty(value = "透视表维度",name="pivotDimensionId")
    private String pivotDimensionId = null;

	@ApiModelProperty(value = "透视表指标",name="pivotTargetId")
    private String pivotTargetId = null;

	@ApiModelProperty(value = "PROVIDER或者CONSUMER 前者查库,后者走缓存,默认前者",name="source")
    private String source;
    
    public String getSelectStatement() {
        return selectStatement;
    }

    public void setSelectStatement(String selectStatement) {
        this.selectStatement = selectStatement;
    }

    public String getGroupByStatement() {
        return groupByStatement;
    }

    public void setGroupByStatement(String groupByStatement) {
        this.groupByStatement = groupByStatement;
    }

    public String getOrderByStatement() {
        return orderByStatement;
    }

    public void setOrderByStatement(String orderByStatement) {
        this.orderByStatement = orderByStatement;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Long getDataListId() {
        return dataListId;
    }

    public void setDataListId(Long dataListId) {
        this.dataListId = dataListId;
    }

    public String getDimensionIds() {
        return dimensionIds;
    }

    public void setDimensionIds(String dimensionIds) {
        this.dimensionIds = dimensionIds;
    }

    public String getTargetIds() {
        return targetIds;
    }

    public void setTargetIds(String targetIds) {
        this.targetIds = targetIds;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getChartTypeId() {
        return chartTypeId;
    }

    public void setChartTypeId(String chartTypeId) {
        this.chartTypeId = chartTypeId;
    }

    public String getPivotDimensionId() {
        return pivotDimensionId;
    }

    public void setPivotDimensionId(String pivotDimensionId) {
        this.pivotDimensionId = pivotDimensionId;
    }

    public String getPivotTargetId() {
        return pivotTargetId;
    }

    public void setPivotTargetId(String pivotTargetId) {
        this.pivotTargetId = pivotTargetId;
    }
    
	public String getSource() {
		if(StringUtils.isEmpty(source)){
			return "PROVIDER";
		}else{
			return source;
		}
	}

	public void setSource(String source) {
		this.source = source;
	}
}
