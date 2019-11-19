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
package com.lakala.dynamicreport.report.controller;

import com.alibaba.fastjson.JSONArray;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.report.model.CfgComponent;
import com.lakala.dynamicreport.report.model.CfgComponentCol;
import com.lakala.dynamicreport.report.model.CfgPageComponent;
import com.lakala.dynamicreport.report.query.CfgComponentColQuery;
import com.lakala.dynamicreport.report.query.CfgComponentQuery;
import com.lakala.dynamicreport.report.query.CfgPageComponentQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentColService;
import com.lakala.dynamicreport.report.service.ICfgComponentService;
import com.lakala.dynamicreport.report.service.ICfgPageComponentService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * 报表组件controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/component")
@Api(tags = "报表组件管理",description = "报表组件控制器(CfgComponentController)")
public class CfgComponentController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(CfgComponentController.class);

    @Autowired
    private ICfgComponentService cfgComponentService;

    @Autowired
    private ICfgPageComponentService cfgPageComponentService;

    @Autowired
    private ICfgComponentColService cfgComponentColService;

    @ApiOperation(value = "分页")
    @RequiresPermissions("cfgComponent:query")
    @GetMapping(value = "/list")
    public PageBean<CfgComponent> list(@ApiParam(name = "query",value = "组件查询对象",required = true) CfgComponentQuery query) {
        PageBean<CfgComponent> pb = new PageBean<>();
        Page<CfgComponent> page = cfgComponentService.findCfgComponentCriteria(query);
        pb.setRowsCount(page.getTotalElements());
        pb.setPageTotal(page.getTotalPages());
        pb.setData(page.getContent());
        return pb;
    }

    @ApiOperation(value = "查询所有数据")
    @RequiresPermissions("cfgComponent:query")
    @RequestMapping(value = "/query",method = RequestMethod.POST)
    public List<CfgComponent> listAll(@ApiParam(name = "query",value = "组件查询对象",required = true) CfgComponentQuery query) {
        return cfgComponentService.findCfgComponent(query);
    }

    @ApiOperation(value = "查询单条数据")
    @RequiresPermissions("cfgComponent:query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
    })
    @GetMapping(value = "/list/{id}")
    public CfgComponent getOne(@PathVariable String id) {
        return cfgComponentService.findOne(id);
    }

    @ApiOperation(value = "删除数据")
    @RequiresPermissions("cfgComponent:delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "组件ID",paramType = "path",required = true)
    })
    @DeleteMapping(value = "/{id}")
    public ResultJson delete(@PathVariable  String id) {
        try {
            cfgComponentService.batchDelete(id);
            return success("刪除成功");
        } catch (Exception e) {
            log.error("刪除失败", e);
            return failure("刪除失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "保存报表关联多组件")
    @RequiresPermissions("cfgPage:relateComponent")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "页面ID",required = true),
            @ApiImplicitParam(name = "ids",value = "组件IDS",required = true),
            @ApiImplicitParam(name = "allIds",value = "页面组件IDS",required = true)
    })
    @PostMapping(value = "/save-component")
    public ResultJson saveComponent(String id, String ids, String allIds) {
        try {
            cfgPageComponentService.save(id, ids, allIds);
            return success("报表关联组件成功");
        } catch (Exception e) {
            log.error("[CfgComponentController : saveComponent]" + "id:" + id + "ids:" + ids, e);
            return failure("更新失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询报表关联多个组件")
    @GetMapping(value = "/realte-component")
    public List<CfgComponent> queryRealteComponent(@ApiParam(name = "cfgComponentQuery",value = "组件查询对象",required = true) CfgComponentQuery cfgComponentQuery) {
        cfgComponentQuery.setStatus(StatusConstants.ACTIVE);
        CfgPageComponentQuery cfgPageComponentQuery = new CfgPageComponentQuery();
        cfgPageComponentQuery.setPage(cfgComponentQuery.getPage());
        List<CfgPageComponent> cfgPageComponents = cfgPageComponentService.findCfgPageComponent(cfgPageComponentQuery);
        List<CfgComponent> cfgComponents = cfgComponentService.findCfgComponent(cfgComponentQuery);
        for (CfgComponent cfgComponent : cfgComponents) {
            for (CfgPageComponent cfgPageComponent : cfgPageComponents) {
                if (cfgComponent.getId().equals(cfgPageComponent.getComponent().getId())) {
                    cfgComponent.setSelected(true);
                    break;
                }
            }
        }
        return cfgComponents;
    }

    @ApiOperation(value = "根据DataListId获取表描述")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataListId",value = "数据集ID",dataType="Long",required = true),
            @ApiImplicitParam(name = "id",value = "组件ID",required = true)
    })
    @GetMapping(value = "/columns/init")
    public ResultJson columns(Long dataListId, String id) {
        ResultJson result = new ResultJson();
        result.setSuccess(false);
        try {
            result = cfgComponentService.findColumns(dataListId,id);
        } catch (Exception e) {
            log.error("getQueryCols error: {}", e);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @ApiOperation(value = "保存数据")
    @RequiresPermissions("cfgComponent:saveOrUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "component",value = "JSON格式字符串(CfgComponent对象)",required = true),
            @ApiImplicitParam(name = "componentColumns",value = "组件列集合字符串(List<CfgComponentCol>)",required = true)
    })
    @PostMapping(value = "/save")
    public ResultJson save(String component, String componentColumns, HttpServletRequest request) {
        // 保存
        try {
            CfgComponent updateObj = cfgComponentService.getParams(component,"save");
            List<CfgComponentCol> cols = JSONArray.parseArray(componentColumns, CfgComponentCol.class);
            cfgComponentService.saveCfgComponent(updateObj,cols);
            return success("保存成功");
        } catch (Exception e) {
            log.error("保存失败", e);
            return failure("保存失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据")
    @RequiresPermissions("cfgComponent:saveOrUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "component",value = "JSON格式字符串(CfgComponent对象)",required = true),
            @ApiImplicitParam(name = "componentColumns",value = "组件列集合字符串(List<CfgComponentCol>)",required = true)
    })
    @PutMapping(value = "/update")
    public ResultJson update(String component, String componentColumns, HttpServletRequest request) {
        try {
            CfgComponent updateObj = cfgComponentService.getParams(component,"update");
            List<CfgComponentCol> cols = JSONArray.parseArray(componentColumns, CfgComponentCol.class);
            cfgComponentService.saveCfgComponent(updateObj,cols);
            return success("操作成功");
        } catch (Exception e) {
            return failure("操作失败:" + e.getMessage());
        }

    }

    @ApiOperation(value = "根据您组件ID查询组件列信息")
    @RequiresPermissions("cfgComponent:saveOrUpdate")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "componentId",value = "组件ID",paramType = "path",required = true)
    })
    @GetMapping(value = "/columns/{componentId}/query")
    public ResultJson queryColumnsByComponentId(@PathParam(value = "componentId") @PathVariable String componentId) {
        ResultJson result = new ResultJson();
        result.setSuccess(false);
        try {
            CfgComponentColQuery cfgComponentColQuery = new CfgComponentColQuery();
            cfgComponentColQuery.setComponent(componentId);

            List<CfgComponentCol> columns = cfgComponentColService.findCfgComponentCol(cfgComponentColQuery);
            for (CfgComponentCol fgComponentCol : columns) {
                if (fgComponentCol.getGroup() != null) {
                    fgComponentCol.setGroupId(fgComponentCol.getGroup().getId());
                }
            }
            result.setSuccess(true);
            result.setObj(columns);
        } catch (Exception e) {
            log.error("queryColumnsByComponentId error: {}", e);
            result.setMsg(e.getMessage());
        }
        return result;
    }

}
