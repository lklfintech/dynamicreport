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
package com.lakala.dynamicreport.common.service;

/**
 * <p>
 * 基础服务接口
 * Entity 实体类
 *Type 主键id类型
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public interface IBaseService<Entity,Type> {

    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDelete(String ids);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Type id);

    /**
     * 更新
     *
     * @param object
     * @return
     */
    Entity update(Entity object);

    /**
     * 新增
     *
     * @param serverList
     * @return
     */
    Entity save(Entity serverList);

    /**
     * 根据pk查找
     *
     * @param id
     * @return
     */
    Entity findOne(Type id);


}
