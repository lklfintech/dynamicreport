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
package com.lakala.dynamicreport.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 定义排序的字段名和顺序，用于DataGridQuery中，以支持多字段排序
 * eg: order by date desc, sequence asc
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "排序对象",description = "排序对象(Sort)")
public class Sort implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "排序model属性名",name="name")
    private String name;

    @ApiModelProperty(value = "排序顺序",name="order")
    private SortDirection order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortDirection getOrder() {
        return order;
    }

    public void setOrder(SortDirection order) {
        this.order = order;
    }
}