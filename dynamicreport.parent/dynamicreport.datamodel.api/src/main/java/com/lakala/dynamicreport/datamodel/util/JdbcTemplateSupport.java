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
package com.lakala.dynamicreport.datamodel.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * jdbcTemplate工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class JdbcTemplateSupport {

    private JdbcTemplate jdbcTemplate;
    private String dbType;

    /**
     * 构造方法
     *
     * @param jdbcTemplate
     * @param dbType
     */
    public JdbcTemplateSupport(JdbcTemplate jdbcTemplate, String dbType) {
        this.jdbcTemplate = jdbcTemplate;
        if (dbType.contains("oracle")) {
            this.dbType = "oracle";
        } else if (dbType.contains("hive")) {
            this.dbType = "hive";
        } else {
            this.dbType = "mysql";
        }
    }

    /**
     * 分页查询
     *
     * @param sql
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> pagedQuery(String sql, Map<String, Object> params, int pageNum, int pageSize) {
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        // 查询总条数
        int totalCount = queryTotalCount(jdbc, sql, params);
        // 查询明细
        List<Map<String, Object>> list = jdbc.queryForList(buildPaginationSql(sql, new PageParam(pageNum, pageSize), null), params);
        // 返回PageResult
        PageResult<Map<String, Object>> pageResult = new PageResult<>();
        pageResult.setRows(list);
        pageResult.setTotal(totalCount);
        return pageResult;
    }

    /**
     * 分页查询，增加select 语句参数
     *
     * @param sql
     * @param params
     * @param pageNum
     * @param pageSize
     * @param selectStmt
     * @return
     */
    public PageResult<Map<String, Object>> pagedQuery(String sql, Map<String, Object> params, int pageNum, int pageSize, String selectStmt) {
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        // 查询总条数
        int totalCount = queryTotalCount(jdbc, sql, params);
        // 查询明细
        List<Map<String, Object>> list = jdbc.queryForList(buildPaginationSql(sql, new PageParam(pageNum, pageSize), selectStmt), params);
        // 返回PageResult
        PageResult<Map<String, Object>> pageResult = new PageResult<>();
        pageResult.setRows(list);
        pageResult.setTotal(totalCount);
        return pageResult;
    }

    /**
     * 查询总数
     *
     * @param jdbc
     * @param sql
     * @param params
     * @return
     */
    private int queryTotalCount(NamedParameterJdbcTemplate jdbc, String sql, Map<String, Object> params) {
        // 查询总条数
        int totalCount = 0;
        List<Map<String, Object>> totalCntList = jdbc.queryForList(buildTotalCountSql(sql), params);
        if (CollectionUtils.isNotEmpty(totalCntList)) {
            Map<String, Object> totalCntMap = totalCntList.get(0);
            for (Map.Entry<String, Object> entry : totalCntMap.entrySet()) {
                totalCount = Integer.valueOf(entry.getValue().toString());
                break;
            }
        }
        return totalCount;
    }

    /**
     * 生成查询总数sql
     *
     * @param sql
     * @return
     */
    private String buildTotalCountSql(String sql) {
        StringBuffer countSql = new StringBuffer();
        countSql.append("SELECT COUNT(*) FROM( ");
        countSql.append(sql);
        countSql.append(" ) subtable2 ");
        return countSql.toString();
    }

    /**
     * 生成查询分页sql
     *
     * @param sql
     * @param pageParam
     * @return
     */
    private String buildPaginationSql(String sql, PageParam pageParam, String selectStmt) {
        StringBuilder dataSql = new StringBuilder();
        if ("oracle".equalsIgnoreCase(this.dbType)) {
            dataSql.append("SELECT ");
            if (StringUtils.isNotBlank(selectStmt)) {
                dataSql.append(selectStmt);
            } else {
                dataSql.append("*");
            }
            dataSql.append(" FROM");
            dataSql.append("	(");
            dataSql.append("		SELECT");
            dataSql.append("			temp.*,");
            dataSql.append("			ROWNUM num");
            dataSql.append("		FROM");
            dataSql.append("			(").append(sql).append(") temp");
            dataSql.append("		WHERE");
            dataSql.append("			ROWNUM <= ").append(pageParam.getEndIndex());
            dataSql.append("	) WHERE　num > ").append(pageParam.getStartIndex());
        } else if ("hive".equalsIgnoreCase(this.dbType)) {
            dataSql.append("SELECT ");
            dataSql.append(selectStmt);
            dataSql.append(" FROM (").append(sql).append(") temp WHERE row_num >=").append(pageParam.getStartIndex());
            dataSql.append(" limit ").append(pageParam.getRows());
        } else {
            dataSql.append(sql).append(" limit ").append(pageParam.getStartIndex()).append(",").append(pageParam.getRows());
        }
        return dataSql.toString();
    }

}
