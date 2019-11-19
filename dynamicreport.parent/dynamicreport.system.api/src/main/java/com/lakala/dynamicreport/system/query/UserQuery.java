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
package com.lakala.dynamicreport.system.query;

import com.lakala.dynamicreport.common.model.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 用户查询对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@ApiModel(value = "用户查询对象",description = "用户查询对象（UserQuery）",parent = BaseQuery.class)
public class UserQuery extends BaseQuery{
    private static final long serialVersionUID = 2784529794293373570L;

    @ApiModelProperty(value = "新密码",name = "password")
    private String password;

    @ApiModelProperty(value = "旧密码",name = "oldPassword")
    private String oldPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
