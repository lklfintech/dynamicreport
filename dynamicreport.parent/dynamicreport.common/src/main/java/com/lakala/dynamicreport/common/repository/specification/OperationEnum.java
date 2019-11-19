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
package com.lakala.dynamicreport.common.repository.specification;

/**
 * <p>
 * 多条件查询 查询操作方式
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public enum OperationEnum {
    // 模糊查询 %param%
    LIKE,
    // 等于
    EQUAL,
    // 不等于
    NE,
    // 大于
    GT,
    // 大于等于
    GTOET,
    // 小于
    LT,
    // 小于等于
    LTOET,
    // 为空
    NULL,
    // 不为空
    NOTNULL,
    // 包含
    IN,
    // 不包含
    NOTIN,
    //区间
    BETWEEN,
    // 参数组别
    AND,
    // 参数组别
    OR,
    WHERE
}