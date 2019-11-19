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
package com.lakala.dynamicreport.common.constants;

/**
 * <p>
 * 提示信息常量
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class MessageConstants {

    /**
     * 信息
     */
    public static final String MSG_QUERY_VAR_BY_RULE_ID = "根据规则ID查询变量,规则id:{}";

    /**
     * 错误信息
     */
    public static final String ERR_INVOKE_SERVICE = "调用服务出错";

    // hide public constructors
    private MessageConstants() {
        throw new IllegalStateException("Message Constants class");
    }
}
