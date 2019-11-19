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
package com.lakala.dynamicreport.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * spring工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Component  
public class SpringUtils implements ApplicationContextAware {  
 
    private static ApplicationContext applicationContext = null;  
 
    @Override  
    public void setApplicationContext(ApplicationContext applicationContext) {  
        if(SpringUtils.applicationContext == null){  
            SpringUtils.applicationContext  = applicationContext;  
        }  
    }  
 
    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {  
        return applicationContext;  
    }  
 
    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name){  
        return getApplicationContext().getBean(name);  
    }  
 
    /**
     * 通过class获取Bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){  
        return getApplicationContext().getBean(clazz);  
    }  
 
    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){  
        return getApplicationContext().getBean(name, clazz);  
    }  
 
} 