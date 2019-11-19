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

import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.model.TreeNode;
import com.lakala.dynamicreport.report.model.CfgComponentType;
import com.lakala.dynamicreport.report.query.CfgComponentTypeQuery;
import com.lakala.dynamicreport.report.service.ICfgComponentTypeService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报表组件类型controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/component-type")
@Api(tags = "报表组件类型管理",description = "报表组件类型控制器(CfgComponentTypeController)")
public class CfgComponentTypeController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(CfgComponentTypeController.class);

    @Autowired
    private ICfgComponentTypeService cfgComponentTypeService;

    @ApiOperation(value = "分页")
    @RequiresPermissions("cfgComponentType:query")
    @GetMapping(value = "/list")
    public PageBean<CfgComponentType> list(@ApiParam(name = "query",value = "组件类型查询对象",required = true) CfgComponentTypeQuery query) {
        PageBean<CfgComponentType> pb = new PageBean<>();
        Page<CfgComponentType> page = cfgComponentTypeService.findCfgComponentTypeCriteria(query);
        pb.setRowsCount(page.getTotalElements());
        pb.setPageTotal(page.getTotalPages());
        pb.setData(page.getContent());
        return pb;
    }

    @ApiOperation(value = "查询所有数据")
    @RequiresPermissions("cfgComponentType:query")
    @GetMapping(value = "/list-all")
    public List<CfgComponentType> listAll(@ApiParam(name = "query",value = "组件类型查询对象",required = true) CfgComponentTypeQuery query) {
        query.setSortOrder(" ct_sequence asc ");
        return cfgComponentTypeService.findCfgComponentType(query);
    }

    @ApiOperation(value = "查询单条数据")
    @RequiresPermissions("cfgComponentType:query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "操作行为",paramType = "path",dataType="String",required = true)
    })
    @GetMapping(value = "/list/{id}")
    public CfgComponentType getOne(@PathVariable String id) {
        return cfgComponentTypeService.findOne(id);
    }

    @ApiOperation(value = "保存数据")
    @RequiresPermissions("cfgComponentType:saveOrUpdate")
    @PostMapping(value = "/save")
    public ResultJson save(@ApiParam(name = "cfgComponentType",value = "组件类型对象",required = true) CfgComponentType cfgComponentType,
                           HttpServletRequest request) {
        // 保存
        try {
            cfgComponentTypeService.save(cfgComponentType);
            return success("保存成功");
        } catch (Exception e) {
            log.error("保存失败", e);
            return failure("保存失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除数据")
    @RequiresPermissions("cfgComponentType:delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "组件类型ID",paramType = "path",required = true)
    })
    @DeleteMapping(value = "/{id}")
    public ResultJson delete(@PathVariable String id) {
        try {
            cfgComponentTypeService.batchDelete(id);
            return success("刪除成功");
        } catch (Exception e) {
            log.error("刪除失败", e);
            return failure("刪除失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据")
    @RequiresPermissions("cfgComponentType:saveOrUpdate")
    @PutMapping(value = "/update")
    public ResultJson update(@ApiParam(name = "cfgComponentType",value = "组件类型对象",required = true) CfgComponentType cfgComponentType,
                             HttpServletRequest request) {
        try {
            CfgComponentType updateObj = cfgComponentTypeService.findOne(cfgComponentType.getId());
            updateObj.setKey(cfgComponentType.getKey());
            updateObj.setName(cfgComponentType.getName());
            updateObj.setParent(cfgComponentType.getParent());
            updateObj.setRemark(cfgComponentType.getRemark());
            updateObj.setStatus(cfgComponentType.getStatus());
            updateObj.setSequence(cfgComponentType.getSequence());
            cfgComponentTypeService.update(updateObj);
            return success("更新成功");
        } catch (Exception e) {
            log.error("更新失败", e);
            return failure("更新失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询组件类型节点")
    @RequiresPermissions("cfgComponentType:query")
    @GetMapping(value = "/tree-view")
    public List<TreeNode> queryTreeview() {
        List<TreeNode> treeNode = new ArrayList<>();
        try {
            List<CfgComponentType> componentTypes = cfgComponentTypeService.findTopComponentTypes();
            treeNode = cfgComponentTypeService.getTree(componentTypes, null);
        } catch (Exception e) {
            log.error("查询顶层组件类型错误：{}", e);
        }
        return treeNode;
    }

}