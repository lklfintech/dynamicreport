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
 * 多条件查询 数据操作对象
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class Predication<T> {

    /**
     * 条件查询类型
     */
    private OperationEnum operator;

    /**
     * 属性名称,如果为关联对象属性，用"."号分割,例如：serverList.ip
     */
    private String name;

    /**
     * 属性值
     */
    private T value;

    /**
     * 属性类型
     */
    private Class paramsClass;

    /**
     * 参数所属组 where后and拼接或者是or拼接
     */
    private OperationEnum paramsGroup;

    private Predication() {
    }

    public static <T> Predication<T> get(OperationEnum operator, String name, T value,Class paramsClass,OperationEnum paramsGroup) {
        return new Builder().operator(operator)
                .name(name).value(value).paramsClass(paramsClass).paramsGroup(paramsGroup).build();
    }

    public static class Builder<T> {
        private Predication predication;

        public Builder() {
            this.predication = new Predication();
        }

        public Builder operator(OperationEnum operationEnum) {
            this.predication.operator = operationEnum;
            return this;
        }

        public Builder name(String name) {
            this.predication.name = name;
            return this;
        }

        public Builder value(T value) {
            this.predication.value = value;
            return this;
        }

        public Builder paramsClass(Class c){
            this.predication.paramsClass = c;
            return this;
        }

        public Builder paramsGroup(OperationEnum paramsGroup){
            this.predication.paramsGroup = paramsGroup;
            return this;
        }
        public <T> Predication<T> build() {
            return this.predication;
        }

        public Predication getPredication() {
            return predication;
        }

        public void setPredication(Predication predication) {
            this.predication = predication;
        }
    }

    public OperationEnum getOperator() {
        return operator;
    }

    public void setOperator(OperationEnum operator) {
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Class getParamsClass() {
        return paramsClass;
    }

    public void setParamsClass(Class paramsClass) {
        this.paramsClass = paramsClass;
    }

    public OperationEnum getParamsGroup() {
        return paramsGroup;
    }

    public void setParamsGroup(OperationEnum paramsGroup) {
        this.paramsGroup = paramsGroup;
    }
}