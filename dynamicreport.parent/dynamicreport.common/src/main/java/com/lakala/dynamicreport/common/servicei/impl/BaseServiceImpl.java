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
package com.lakala.dynamicreport.common.servicei.impl;

import com.lakala.dynamicreport.common.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 基础服务接口实现类
 * IRepository : 服务Dao，例：CacheServiceRepository
 * Entity : 实体类，例：CacheService
 * Type ：主键类型，例：Long or String。。。。。
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-27
 */
public class BaseServiceImpl<IRepository extends JpaRepository<Entity, Type>&JpaSpecificationExecutor<Entity>,Entity,Type extends java.io.Serializable>
                                        implements IBaseService<Entity,Type> {

    @Autowired
    IRepository repository;

    @Override
    @Transactional
    public void batchDelete(String ids){
        List<Entity> objectList = new ArrayList<>();
        if (null != ids && !"".equals(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                if (null != id && !"".equals(id)) {
                    Entity Object = repository.findById((Type)id).orElse(null);
                    objectList.add(Object);
                }
            }
        }
        repository.deleteInBatch(objectList);
    }

    @Override
    @Transactional
    public void delete(Type id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Entity update(Entity object) {
        return repository.saveAndFlush(object);
    }

    @Override
    @Transactional
    public Entity save(Entity serverList) {
        return repository.save(serverList);
    }

    /**
     * 如需要对获取到不同返回效果
     *
     * 1:repository.findById(id).orElse(null);
     * 2:通过 optional.isPresent() 来判断是否获取到实体
     *
     * @param id
     * @return
     */
    @Override
    public Entity findOne(Type id) {
//        Optional optional = repository.findById(id);
//        if (optional.isPresent()) {
            return repository.findById(id).orElse(null);
//        }
//        return null;
    }

}
