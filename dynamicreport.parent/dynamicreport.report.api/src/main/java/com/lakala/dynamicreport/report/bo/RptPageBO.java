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

import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.model.CfgParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 报表BO
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "报表BO",description = "报表BO(RptPageBO)")
public class RptPageBO implements Serializable {

    private static final long serialVersionUID = -6324837689228670919L;


    @ApiModelProperty(value = "报表组件集合",name="componentList")
    private List<RptComponentBO> componentList;

    @ApiModelProperty(value = "报表参数集合",name="paramList")
    private List<CfgParam> paramList;

    @ApiModelProperty(value = "报表页面",name="page")
    private CfgPage page;

    public List<RptComponentBO> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<RptComponentBO> componentList) {
        this.componentList = componentList;
    }

    public List<CfgParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<CfgParam> paramList) {
        this.paramList = paramList;
    }

    public CfgPage getPage() {
        return page;
    }

    public void setPage(CfgPage page) {
        this.page = page;
    }
}
