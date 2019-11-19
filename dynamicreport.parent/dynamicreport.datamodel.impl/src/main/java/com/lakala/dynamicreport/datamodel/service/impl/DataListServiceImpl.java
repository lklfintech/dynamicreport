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
package com.lakala.dynamicreport.datamodel.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.redis.IRedisService;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.datamodel.constants.DataModelConstants;
import com.lakala.dynamicreport.datamodel.model.*;
import com.lakala.dynamicreport.datamodel.query.DataListQuery;
import com.lakala.dynamicreport.datamodel.query.DataParameterGroupQuery;
import com.lakala.dynamicreport.datamodel.query.ReportDataQuery;
import com.lakala.dynamicreport.datamodel.repository.IDataListRelParamterGroupRepository;
import com.lakala.dynamicreport.datamodel.repository.IDataListRepository;
import com.lakala.dynamicreport.datamodel.service.IDataListService;
import com.lakala.dynamicreport.datamodel.service.IDataParameterGroupService;
import com.lakala.dynamicreport.datamodel.service.IDataParameterService;
import com.lakala.dynamicreport.datamodel.service.IDataSourceService;
import com.lakala.dynamicreport.datamodel.util.JdbcTemplateSupport;
import com.lakala.dynamicreport.datamodel.util.PageResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lakala.dynamicreport.common.constants.GlobalConstants.DRIVER_HIVE;

/**
 * <p>
 * 操作数据集接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "dataListService")
public class DataListServiceImpl extends BaseServiceImpl<IDataListRepository,DataList, Long> implements IDataListService {

    private Logger log = LoggerFactory.getLogger(DataListServiceImpl.class);

    @Autowired
    private IDataListRepository dataListRepository;

    @Autowired
    private IDataParameterGroupService dataParameterGroupService;

    @Autowired
    private IDataParameterService dataParameterService;

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private IDataListRelParamterGroupRepository dataListRelParamterGroupRepository;

    private Pattern pattern = Pattern.compile("\\{(.*?)\\}");

    @Autowired
    private IRedisService redisService;

    @Value("${cache.type}")
    private String cacheType;

    /**
     * 消费者标识,此标识数据从缓存取,不走数据库
     */
    private static final String CONSUMER = "CONSUMER";


    @Override
    public Page<DataList> findDataListCriteria(DataListQuery dataListQuery) {
        return dataListRepository.findAll(specification(dataListQuery), PageForList.getPageable("modifiedDate",dataListQuery));
    }

    @Override
    public List<DataList> findDataList(DataListQuery dataListQuery) {
        return dataListRepository.findAll(specification(dataListQuery));
    }

    @Override
    @Transactional
    public DataList update(DataList dataList) {
        DataList updateObj = findOne(dataList.getId());
        updateObj.setName(dataList.getName());
        updateObj.setQuerySql(dataList.getQuerySql());
        updateObj.setDataSource(dataList.getDataSource());
        updateObj.setRecordCnt(dataList.getRecordCnt());
        updateObj.setDescription(dataList.getDescription());
        updateObj.setStatus(dataList.getStatus());
        return dataListRepository.save(updateObj);
    }

    @Override
    @Transactional
    public DataList save(DataList dataList) {
        DataListQuery query = new DataListQuery();
        query.setIdentifier(dataList.getIdentifier());
        List<DataList> list = findDataList(query);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BusinessException("保存失败，数据源标识" + dataList.getIdentifier() + "有重复");
        }
        return dataListRepository.save(dataList);
    }


    /**
     * 拼凑查询条件
     */
    private Specification<DataList> specification(final DataListQuery dataListQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != dataListQuery) {
            list.add(Predication.get(OperationEnum.EQUAL,"id",dataListQuery.getId(),Long.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"status",dataListQuery.getStatus(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.EQUAL,"identifier",dataListQuery.getIdentifier(),String.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.IN,"createdUser",dataListQuery.getUsers(),List.class,OperationEnum.AND));
            list.add(Predication.get(OperationEnum.LIKE,"name",dataListQuery.getSearchText(),String.class,OperationEnum.OR));
            list.add(Predication.get(OperationEnum.LIKE,"identifier",dataListQuery.getSearchText(),String.class,OperationEnum.OR));
        }
          return SpecificationUtil.whereAndOr(list);
    }

    @Override
    public String transferGroup(String sql, String source) {
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String group = matcher.group(1).trim();
            DataParameterGroup dataParameterGroup = findDataParameterGroup(source, group);
            if (dataParameterGroup == null) {
                sql = sql.replace(matcher.group(0), DataModelConstants.EQUAL_1);
            } else {
                sql = sql.replace(matcher.group(0), dataParameterGroup.getContent());
            }
        }
        return sql;
    }

    @Override
    public Map<String, Object> transferParams(DataList dataList, String sql, Map<String, Object> map, String source) {
        Matcher matcher = pattern.matcher(sql);
        Map<String, Object> params = Maps.newHashMap();
        Map<String, Object> result = Maps.newHashMap();

        while (matcher.find()) {
            String paramIdentifier = matcher.group(1).trim();
            DataParameter dataParameter = findDataParameter(source, paramIdentifier);
            Object val = map.get(paramIdentifier);
            boolean isNull = val == null || StringUtils.isEmpty(String.valueOf(val).trim());

            if (dataParameter == null || isNull) {
                sql = sql.replace(matcher.group(0), DataModelConstants.EQUAL_1);
                continue;
            }

            if (StatusConstants.YES.equals(dataParameter.getMandatory()) && isNull) {
                throw new BusinessException(String.format("参数标识【%s】必输", paramIdentifier));
            }

            String content = dataParameter.getParamContent();
            content = content.replace("?", ":" + paramIdentifier);
            sql = sql.replace(matcher.group(0), content);

            if (StringUtils.isBlank(dataParameter.getPrefix()) && StringUtils.isBlank(dataParameter.getSuffix())) {
                params.put(paramIdentifier, map.get(paramIdentifier));
            } else {
                StringBuilder sb = new StringBuilder("");
                if (StringUtils.isNotBlank(dataParameter.getPrefix())) {
                    sb.append(dataParameter.getPrefix());
                }
                sb.append(map.get(paramIdentifier));
                if (StringUtils.isNotBlank(dataParameter.getSuffix())) {
                    sb.append(dataParameter.getSuffix());
                }
                params.put(paramIdentifier, sb.toString());
            }
        }
        result.put("sql", sql);
        result.put(DataModelConstants.PARAMS, params);
        return result;
    }

    @Override
    public DataList findByIdentifier(String identifier) {
        return dataListRepository.findByIdentify(identifier);

    }

    @Override
    @Cacheable(value = "dataListCache", key = "#identifier")
    public DataList findByIdentifierCache(String identifier) {
        return dataListRepository.findByIdentify(identifier);

    }

    @Override
    @CacheEvict(value = "dataListCache", key = "#identifier")
    public void initByIdentifierCache(String identifier) {
        //删除缓存
    }

    private Map<String, Object> generateSql(DataList dataList, Map<String, Object> parMap, String source) {
        if (parMap == null) {
            parMap = Maps.newHashMap();
        }
        if (!CONSUMER.equals(source)) {
            refreshDataListCache(dataList.getIdentifier());//非消费者,先刷新缓存,后续所有查询不走库
        }

        String js = JSONObject.toJSONString(parMap);
        log.info("数据模型【{}】,请求参数【{}】", dataList.getIdentifier(), js);

        String groupSql = transferGroup(dataList.getQuerySql(), source);

        Map<String, Object> result = transferParams(dataList, groupSql, parMap, source);

        log.info("数据集【{}】,预执行SQL:【{}】", dataList.getIdentifier(), result.get("sql"));

        return result;

    }

    /**
     * 刷新数据集缓存
     *
     * @param identifier
     */
    private void refreshDataListCache(String identifier) {
        initByIdentifierCache(identifier);
        DataList dataList = findByIdentifierCache(identifier);
        for (DataParameterGroup group : dataList.getDataParameterGroup()) {
            dataParameterGroupService.initByIdentifierCache(group.getIdentifier());
            group = dataParameterGroupService.findByIdentifierCache(group.getIdentifier());
            for (DataParameter parameter : group.getDataParameter()) {
                dataParameterService.initByIdentifierCache(parameter.getIdentifier());
                dataParameterService.findByIdentifierCache(parameter.getIdentifier());
            }
        }
    }

    @Override
    public List<Map<String, Object>> queryForReport(ReportDataQuery reportDataQuery) {
        try {
            // 查询数据集
            DataList dl = findOne(reportDataQuery.getDataListId());
            String driverClass = dl.getDataSource().getDriverClass();

            // 生成查询语句
            Map<String, Object> map = generateSql(dl, reportDataQuery.getParamMap(), reportDataQuery.getSource());
            String sql = (String) map.get("sql");
            if (StringUtils.isNotBlank(reportDataQuery.getSelectStatement())) {
                sql = String.format("SELECT %s FROM (%s) subtable ", reportDataQuery.getSelectStatement(), sql);
            }
            // 如果是HIVE库，为提高性能，SQL语句不进行GROUP BY和ORDER BY拼装，同时需要指定limit
            if (DRIVER_HIVE.equalsIgnoreCase(driverClass)) {
                if (org.apache.commons.lang.math.NumberUtils.isNumber(dl.getRecordCnt())) {
                    sql = sql + " LIMIT " + dl.getRecordCnt();
                }
            } else {
                if (StringUtils.isNotBlank(reportDataQuery.getGroupByStatement())) {
                    sql = sql + " GROUP BY " + reportDataQuery.getGroupByStatement();
                }
                if (StringUtils.isNotBlank(reportDataQuery.getOrderByStatement())) {
                    sql = sql + " ORDER BY " + reportDataQuery.getOrderByStatement();
                }
            }

            //获取JdbcTemplate实例
            JdbcTemplate jdbcTemplate = dataSourceService.getJdbcTemplate(dl.getDataSource());
            NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);

            // 执行查询
            Map<String, Object> params = (Map<String, Object>) map.get(DataModelConstants.PARAMS);
            List<Map<String, Object>> resMap = jdbc.queryForList(sql, params);

            log.info("数据集【{}】,预执行SQL:【{}】,成功!,数据量【{}】 ", dl.getIdentifier(), sql, resMap.size());
            return resMap;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public PageBean<Map<String, Object>> queryForReportPage(ReportDataQuery reportDataQuery) {
        try {
            // 查询数据集
            DataList dl = findOne(reportDataQuery.getDataListId());
            String driverClass = dl.getDataSource().getDriverClass();

            // 生成查询语句
            Map<String, Object> map = generateSql(dl, reportDataQuery.getParamMap(), reportDataQuery.getSource());
            String sql = (String) map.get("sql");
            StringBuilder sqlBuilder = new StringBuilder();
            if (DRIVER_HIVE.equalsIgnoreCase(driverClass)) {
                // hive sql sample: select row_number() over (a1,a2) as row_num,a1,a2,a3... from (select * from table) subtable order by ... group by ...
                sqlBuilder.append("SELECT row_number() over (order by ");
                if (StringUtils.isNotBlank(reportDataQuery.getOrderByStatement())) {
                    sqlBuilder.append(reportDataQuery.getOrderByStatement());
                } else {
                    sqlBuilder.append(reportDataQuery.getSelectStatement());
                }
                sqlBuilder.append(") as row_num, subtable.*");
                sqlBuilder.append(" FROM (").append(sql).append(") subtable");
            } else {
                if (StringUtils.isNotBlank(reportDataQuery.getSelectStatement())) {
                    sql = String.format("SELECT %s FROM (%s) subtable ", reportDataQuery.getSelectStatement(), sql);
                    sqlBuilder.append(sql);
                }
            }
            if (StringUtils.isNotBlank(reportDataQuery.getGroupByStatement())) {
                sqlBuilder.append(" group by ").append(reportDataQuery.getGroupByStatement());
            }
            if (StringUtils.isNotBlank(reportDataQuery.getOrderByStatement())) {
                sqlBuilder.append(" order by ").append(reportDataQuery.getOrderByStatement());
            }

            //获取JdbcTemplate实例
            JdbcTemplate jdbcTemplate = dataSourceService.getJdbcTemplate(dl.getDataSource());
            JdbcTemplateSupport jdbc = new JdbcTemplateSupport(jdbcTemplate, dl.getDataSource().getDriverClass());

            // 执行查询
            Map<String, Object> params = (Map<String, Object>) map.get(DataModelConstants.PARAMS);
            PageResult<Map<String, Object>> pr = null;
            if (DRIVER_HIVE.equalsIgnoreCase(driverClass)) {
                pr = jdbc.pagedQuery(sqlBuilder.toString(), params, reportDataQuery.getPageNumber(), reportDataQuery.getPageSize(), reportDataQuery.getSelectStatement());
            } else {
                pr = jdbc.pagedQuery(sqlBuilder.toString(), params, reportDataQuery.getPageNumber(), reportDataQuery.getPageSize());
            }

            // 生成PageBean对象
            PageBean<Map<String, Object>> pb = new PageBean<>();
            if (CollectionUtils.isNotEmpty(pr.getRows())) {
                pb.setData(pr.getRows());
            }
            pb.setPageNo(reportDataQuery.getPageNumber());
            pb.setRowsCount(pr.getTotal());
            pb.setRowsPerPage(reportDataQuery.getPageSize());

            log.info("数据集【{}】, 预执行SQL:【{}】,成功!,数据量【{}】", dl.getIdentifier(), sqlBuilder.toString(), pr.getTotal());
            return pb;
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> queryForFeature(DataList dl, Map<String, Object> parMap) throws UnsupportedEncodingException {
        String source = String.valueOf(parMap.remove("source"));
        Map<String, Object> map = generateSql(dl, parMap, source);

        String sql = (String) map.get("sql");
        Map<String, Object> params = (Map<String, Object>) map.get(DataModelConstants.PARAMS);

        //获取JBs实例
        JdbcTemplate jdbcTemplate = dataSourceService.getJdbcTemplate(dl.getDataSource());

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> resMap = jdbc.queryForList(sql, params);

        log.info(" 数据集【{}】,预执行SQL:【{}】,成功!,数据量【{}】", dl.getIdentifier(), sql, resMap.size());

        return resMap;
    }

    @Override
    public List<String> getSqlParamList(String sql) {
        ArrayList<String> list = new ArrayList<>();
        Matcher m = pattern.matcher(sql);
        while (m.find()) {
            list.add(m.group(1));
        }
        return list;
    }

    @Override
    public DataParameterGroup findDataParameterGroup(String source, String groupIdentifier) {

        if (!CONSUMER.equals(source)) {
            return dataParameterGroupService.findByIdentifier(groupIdentifier);
        }

        if (StringUtils.isNotBlank(cacheType) && cacheType.equals(GlobalConstants.REDIS_CACHE)) {

            String groupJson = redisService.get(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAM_GROUP + groupIdentifier);

            DataParameterGroup group;
            if (StringUtils.isNotBlank(groupJson)) {
                group = JSONObject.parseObject(groupJson, DataParameterGroup.class);
            } else {
                initGroupByIdentifierCache(groupIdentifier);
                groupJson = redisService.get(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAM_GROUP + groupIdentifier);
                if (StringUtils.isBlank(groupJson)) {
                    return null;
                }
                group = JSONObject.parseObject(groupJson, DataParameterGroup.class);
            }
            return group;
        } else {
            return dataParameterGroupService.findByIdentifier(groupIdentifier);
        }
    }

    @Override
    public void initGroupByIdentifierCache(String groupIdentifier) {

        DataParameterGroup group = dataParameterGroupService.findByIdentifier(groupIdentifier);
        if (group == null) {
            redisService.set(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAM_GROUP + groupIdentifier, null);
        } else {
            redisService.set(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAM_GROUP + groupIdentifier, JSONObject.toJSONString(group));
        }
    }

    @Override
    public DataParameter findDataParameter(String source, String paramIdentifier) {

        if (!CONSUMER.equals(source)) {
            return dataParameterService.findByIdentifier(paramIdentifier);
        }

        if (StringUtils.isNotBlank(cacheType) && cacheType.equals(GlobalConstants.REDIS_CACHE)) {

            String parameterJson = redisService.get(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAMETER + paramIdentifier);

            DataParameter parameter;
            if (StringUtils.isNotBlank(parameterJson)) {
                parameter = JSONObject.parseObject(parameterJson, DataParameter.class);
            } else {
                initDataParameterCache(paramIdentifier);
                parameterJson = redisService.get(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAMETER + paramIdentifier);
                if (StringUtils.isBlank(parameterJson)) {
                    return null;
                }
                parameter = JSONObject.parseObject(parameterJson, DataParameter.class);
            }
            return parameter;
        } else {
            return dataParameterService.findByIdentifierCache(paramIdentifier);
        }
    }

    @Override
    public void initDataParameterCache(String paramIdentifier) {

        DataParameter parameter = dataParameterService.findByIdentifier(paramIdentifier);
        if (parameter == null) {
            redisService.set(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAMETER + paramIdentifier, null);
        } else {
            redisService.set(GlobalConstants.REDIS_CACHE_PREFIX + DataModelConstants.TYPE_PARAMETER + paramIdentifier, JSONObject.toJSONString(parameter));
        }
    }

    @Override
    @Transactional
    public ResultJson saveOrUpdate(DataList dataList) {
        ResultJson result = new ResultJson();
        result.setSuccess(true);
        //启用 状态
        Set<Long> groupIds= Sets.newHashSet();
        if(StatusConstants.ACTIVE.equals(dataList.getStatus())){
            //校验组,参数 是否存在
            List<String> groups=getSqlParamList(dataList.getQuerySql());

            for(String group:groups){
                List<DataParameterGroup> groupList=dataParameterGroupService.findDataParameterGroup(new DataParameterGroupQuery(group));
                if (CollectionUtils.isEmpty(groupList)) {
                    result.setMsg(String.format("{%s}不存在", group));
                    result.setSuccess(false);
                    return result;
                }
                if (StatusConstants.INACTIVE.equals(groupList.get(0).getStatus())) {
                    result.setMsg(String.format("组{%s}对应的数据组停用", group));
                    result.setSuccess(false);
                    return result;
                }
                groupIds.add(groupList.get(0).getId());
            }
        }

        if (dataList.getId() != null) {
            DataList updateObj = findOne(dataList.getId());
            updateObj.setName(dataList.getName());
            updateObj.setQuerySql(dataList.getQuerySql());
            updateObj.setDataSource(dataList.getDataSource());
            updateObj.setRecordCnt(dataList.getRecordCnt());
            updateObj.setDescription(dataList.getDescription());
            updateObj.setStatus(dataList.getStatus());
            dataList = dataListRepository.save(updateObj);
        } else {
            DataListQuery query = new DataListQuery();
            query.setIdentifier(dataList.getIdentifier());
            List<DataList> list = findDataList(query);
            if (CollectionUtils.isNotEmpty(list)) {
                throw new BusinessException("保存失败，数据源标识" + dataList.getIdentifier() + "有重复");
            }
            dataList = dataListRepository.save(dataList);
        }

        dataListRelParamterGroupRepository.deleteByListId(dataList.getId());
        for (Long groupId : groupIds) {
            dataListRelParamterGroupRepository.save(new DataListRelParamterGroup(dataList.getId(), groupId));
        }
        result.setMsg("操作成功");
        return result;
    }

    @Override
    public ResultJson querySqlTest(DataList dataList) {
        ResultJson result = new ResultJson();
        result.setSuccess(true);

        DataSource datasource=dataList.getDataSource();
        if (datasource == null) {
            result.setSuccess(false);
            result.setMsg("请选择数据源");
            return result;
        }

        String sql=dataList.getQuerySql();
        if (StringUtils.isEmpty(sql)) {
            result.setSuccess(false);
            result.setMsg("请输入查询SQL");
            return result;
        }
        //处理sql组
        String groupSql=transferGroup(sql,DataModelConstants.PROVIDER);
        Map<String,Object> map=transferParams(dataList, groupSql,new HashMap<String, Object>(),DataModelConstants.PROVIDER);
        groupSql=String.valueOf(map.get("sql"));

        log.info("转换组SQL,源{}===>后:{}",sql,groupSql);
        //测试 只查询一条数据
        sql=String.format("SELECT sub_table.* FROM (%s) sub_table LIMIT 1 ", groupSql);

        JdbcTemplate  jb=dataSourceService.getJdbcTemplate(datasource);
        result.setMsg(JSONUtils.toJSONString(jb.queryForList(sql))+"</br>测试SQL:</br>"+sql);
        return result;
    }
}
