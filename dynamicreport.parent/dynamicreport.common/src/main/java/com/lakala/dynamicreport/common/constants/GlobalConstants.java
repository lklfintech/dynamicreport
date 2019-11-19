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
 * 通用常量类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class GlobalConstants {
    /**
     * session中存放用户信息的key值
     */
    public static final String SESSION_USER_INFO = "userInfo";

    /**
     * 正常消息类型（规则输出）
     */
    public static final String MSG_TYPE_NORMAL = "normal";

    /**
     * 错误消息类型（规则输出）
     */
    public static final String MSG_TYPE_ERROR = "error";

    /**
     * 管理员用户
     */
    public static final String USER_ADMIN = "admin";

    /**
     * 参数名
     */
    public static final String PARAM_FIELD_NAME = "fieldName";
    public static final String PARAM_OPERATOR = "operator";
    public static final String PARAM_VALUE = "value";
    public static final String PARAM_CALL_BACK = "callback";
    public static final String PARAM_IDENTIFIER = "identify";
    public static final String PARAM_STATUS = "status";
    public static final String PARAM_ORIGIN = "origin";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_TYPE = "type";

    /**
     * 请求成功标志
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 请求失败标志
     */
    public static final String FAILED = "FAILED";

    /**
     * redis前缀
     */
    public static final String REDIS_CACHE_PREFIX = "dynamicreport_";

    // shiro redis prefix
    public static final String REDIS_CACHE_SHIRO_PREFIX = "shiro_";

    // rule redis prefix
    public static final String REDIS_CACHE_RULE_PREFIX = "rule_";

    // user redis prefix
    public static final String REDIS_CACHE_USER_PREFIX = "user_";

    // decision redis prefix
    public static final String REDIS_CACHE_DECISION_PREFIX = "decision_";

    // decision identifier redis prefix
    public static final String REDIS_CACHE_DECISION_IDENTIFIER_PREFIX = "decision_ientifier_";

    // default decision identifier redis prefix
    public static final String REDIS_CACHE_DEFAULT_DECISION_PREFIX = "default_decision_";

    // card type redis prefix
    public static final String REDIS_CACHE_CARD_TYPE_PREFIX = "card_type_";

    // matrix type redis prefix
    public static final String REDIS_CACHE_MATRIX_TYPE_PREFIX = "matrix_type_";

    // card type identifier redis prefix
    public static final String REDIS_CACHE_CARD_TYPE_IDENTIFIER_PREFIX = "card_type_ientifier_";

    // matrix type identifier redis prefix
    public static final String REDIS_CACHE_MATRIX_TYPE_IDENTIFIER_PREFIX = "matrix_type_ientifier_";

    // ACTIVITI ENGINE DISTRIBUTED ID KEY
    public static final String ACTIVITI_ENGINE_DISTRIBUTED_ID_KEY = "ACTIVITI_ENGINE_DISTRIBUTED_ID_KEY_";

    /**
     * 本地缓存为0
     */
    public static final String LOCAL_CACHE = "0";

    /**
     * redis缓存为1
     */
    public static final String REDIS_CACHE = "1";

    /**
     * log日志中括号
     */
    public static final String BRACKETS_5 = "{}:{}:{}:{}:{}";

    public static final String BRACKETS_4 = "{}:{}:{}:{}";

    public static final String BRACKETS_3 = "{}:{}:{}";

    public static final String BRACKETS_2 = "{}:{}";

    public static final String BRACKETS_1 = "{}";

    public static final String SYSTEM_ERR = "系统异常";

    public static final String LEFT_BRACKETS = "(";

    public static final String RIGHT_BRACKETS = ")";

    public static final String COMMA = ",";

    /**
     * 服务类型
     */
    public static final String SERVICE_LKL_RSA = "LKL_RSA";

    /**
     * 工作流规则加密标识true加密，false不加密
     */
    public static final boolean WORK_FLOW_DECRYPT_FLAG = true;

    // hide public constructors
    private GlobalConstants() {
        throw new IllegalStateException("Global Constants class");
    }

    /**
     * 本地缓存为0
     */
    public static final String COMMON_ZERO = "0";

    /**
     * redis缓存为1
     */
    public static final String COMMON_ONE = "1";

    /**
     * HIVE驱动名
     */
    public static final String DRIVER_HIVE = "org.apache.hive.jdbc.HiveDriver";
}
