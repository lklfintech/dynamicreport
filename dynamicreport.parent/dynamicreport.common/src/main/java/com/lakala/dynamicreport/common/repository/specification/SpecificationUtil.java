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

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.lakala.dynamicreport.common.utils.DateUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 多条件参数的拼接
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class SpecificationUtil {


    /**
     * 多条件 where and查询，不包含or
     *
     * @param ps
     * @return
     */
    public static <T> Specification<T> where(List<Predication> ps) {
        return (root, query, builder) ->
                builder.and(getPredicateArray(root, builder, ps));
    }

    /**
     * 多条件 or 查询
     *
     * @param ps
     * @return
     */
    public static <T> Specification<T> or(List<Predication> ps) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.or(getPredicateArray(root, builder, ps));
    }

    /**
     * 多条件 where and和or的组合查询
     * 分装and条件参数和or条件参数
     *
     * @param ps
     * @return
     */
    public static <T> Specification<T> whereAndOr(List<Predication> ps) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) ->
                builder.and(getPredicateArray(root, builder, ps));
    }

    /**
     * 获取多条件查询数组Predicate[]
     *
     * @param root
     * @param builder
     * @param ps
     * @return
     */
    private static Predicate[] getPredicateArray(Root<?> root, CriteriaBuilder builder, List<Predication> ps) {
        List<Predicate> predicateList = Lists.newArrayList();
        List<Predicate> orList = Lists.newArrayList();
        ps.forEach(p -> {
            Expression exp;
            //区分属性名为关联对象属性时，例：serverList.ip
            if (p.getName().contains(".")) {
                String[] nameArr = p.getName().split("\\.");
                exp = root.get(nameArr[0]).get(nameArr[1]);
            }else{
                exp  = root.get(p.getName());
            }
            //参数所属组别
            if (!StringUtils.isEmpty(p.getValue()) && OperationEnum.AND.equals(p.getParamsGroup())) {

                Predicate predicate = buildPredicate(builder, exp, p);
                predicateList.add(predicate);
            }

            if (!StringUtils.isEmpty(p.getValue()) && OperationEnum.OR.equals(p.getParamsGroup())) {

                Predicate predicate = buildPredicate(builder, exp, p);
                orList.add(predicate);
            }
        });
        if (orList.size() > 0) {
            predicateList.add(builder.or(orList.toArray(new Predicate[orList.size()])));
        }

        return predicateList.toArray(new Predicate[predicateList.size()]);
    }

    /**
     * 创建查询sql
     *
     * @param cb
     * @param exp
     * @param p
     * @return
     */
    private static Predicate buildPredicate(CriteriaBuilder cb, Expression exp, Predication p) {
        Predicate predicate;
        switch (p.getOperator()) {
            case LIKE:
                predicate = cb.like(exp.as(p.getParamsClass()), "%"+p.getValue().toString()+"%");
                break;
            case EQUAL:
                predicate = cb.equal(exp.as(p.getParamsClass()), p.getValue());
                break;
            case NE:
                predicate = cb.notEqual(exp.as(p.getParamsClass()), p.getValue());
                break;
            case GT:
                predicate = cb.greaterThan(exp.as(p.getParamsClass()), (Comparable) p.getValue());
                break;
            case GTOET:
                predicate = cb.greaterThanOrEqualTo(exp.as(p.getParamsClass()), (Comparable) p.getValue());
                break;
            case LT:
                predicate = cb.lessThan(exp.as(p.getParamsClass()), (Comparable) p.getValue());
                break;
            case LTOET:
                predicate = cb.lessThanOrEqualTo(exp.as(p.getParamsClass()), (Comparable) p.getValue());
                break;
            case NULL:
                predicate = cb.isNull(exp.as(p.getParamsClass()));
                break;
            case NOTNULL:
                predicate = cb.isNotNull(exp.as(p.getParamsClass()));
                break;
            case IN:
                predicate = getIn(exp, p.getValue());
                break;
            case NOTIN:
                predicate = getIn(exp, p.getValue()).not();
                break;
            case BETWEEN:
                predicate = getBetween(cb,exp,p);
                break;
            default:
                throw new IllegalArgumentException("非法的查询操作");
        }
        return predicate;
    }

    /**
     * 多条件 in 查询
     *
     * @param exp
     * @param value
     * @param <T>
     * @return
     */
    private static <T> Predicate getIn(Expression<T> exp, T value) {
        if (value instanceof Object[]) {
            return exp.in((Object[]) value);
        }
        if (value instanceof Collection) {
            return exp.in((Collection) value);
        }
            throw new IllegalArgumentException("非法的in操作");
    }

    /**
     * 多条件 between 查询
     *
     * @param exp
     * @param p
     * @return
     */
    private static <T> Predicate getBetween(CriteriaBuilder cb, Expression exp, Predication p) {
        JSONArray arr = JSONArray.parseArray(p.getValue().toString());
        if (arr.size() <= 1) {
            return null;
        }
        if (p.getParamsClass() == Timestamp.class) {
            Timestamp startDateTime = DateUtils.formatFromYYYYMMDDhhmmss(arr.getString(0));
            Timestamp endDateTime = DateUtils.formatFromYYYYMMDDhhmmss(arr.getString(1));
            return cb.between(exp,startDateTime,endDateTime);
        }
        if (p.getParamsClass() == String.class) {
            String value1 = arr.getString(0);
            String value2 = arr.getString(1);
            return cb.between(exp,value1,value2);
        }
            throw new IllegalArgumentException("非法的between操作");
    }

}