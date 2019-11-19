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

import com.alibaba.fastjson.JSONObject;
import com.lakala.dynamicreport.common.constants.GlobalConstants;
import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.ActiveUser;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.common.utils.CreateFileUtils;
import com.lakala.dynamicreport.common.utils.JsonUtils;
import com.lakala.dynamicreport.report.bo.RptPageBO;
import com.lakala.dynamicreport.report.model.CfgPage;
import com.lakala.dynamicreport.report.query.CfgPageQuery;
import com.lakala.dynamicreport.report.service.ICfgPageService;
import com.lakala.dynamicreport.system.service.ISystemUserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * <p>
 * 报表页面controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/cfg/page")
@Api(tags = "报表页面管理",description = "报表页面控制器(CfgPageController)")
public class CfgPageController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(CfgPageController.class);

    @Autowired
    private ICfgPageService cfgPageService;

    @Autowired
    private ISystemUserService systemUserService;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${report.servletPath}")
    private String servletPath;

    @ApiOperation(value = "分页")
    @RequiresPermissions("cfgPage:query")
    @GetMapping(value = "/list")
    public PageBean<CfgPage> list(@ApiParam(name = "query",value = "页面查询对象",required = true) CfgPageQuery query) {
        PageBean<CfgPage> pb = new PageBean<>();
        query.setUsers(systemUserService.getAllSameReportRoleUsers());
        Page<CfgPage> page = cfgPageService.findCfgPageCriteria(query);
        pb.setRowsCount(page.getTotalElements());
        pb.setPageTotal(page.getTotalPages());
        pb.setData(page.getContent());
        return pb;
    }

    @ApiOperation(value = "查询所有数据")
    @RequiresPermissions("cfgPage:query")
    @GetMapping(value = "/list-all")
    public List<CfgPage> listAll(@ApiParam(name = "query",value = "页面查询对象",required = true) CfgPageQuery query) {
        query.setUsers(systemUserService.getAllSameReportRoleUsers());
        return cfgPageService.findCfgPage(query);
    }

    @ApiOperation(value = "查询单条数据")
    @RequiresPermissions("cfgPage:query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="String",required = true)
    })
    @GetMapping(value = "/list/{id}")
    public CfgPage getOne(@PathVariable String id) {
        return cfgPageService.findOne(id);
    }

    @ApiOperation(value = "保存数据")
    @RequiresPermissions("cfgPage:saveOrUpdate")
    @PostMapping(value = "/save")
    public ResultJson save(@ApiParam(name = "cfgPage",value = "页面对象",required = true) CfgPage cfgPage,
                           HttpServletRequest request) {
        // 保存
        try {
            cfgPage.setPublished(StatusConstants.NO_FLAG);
            cfgPageService.save(cfgPage);
            return success("保存成功");
        } catch (Exception e) {
            log.error("保存失败", e);
            return failure("保存失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除数据")
    @RequiresPermissions("cfgPage:delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "页面ID",paramType = "path",required = true)
    })
    @DeleteMapping(value = "/{id}")
    public ResultJson delete(@PathVariable String id) {
        try {
            cfgPageService.batchDelete(id);
            return success("刪除成功");
        } catch (Exception e) {
            log.error("刪除失败", e);
            return failure("刪除失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据")
    @RequiresPermissions("cfgPage:saveOrUpdate")
    @PutMapping(value = "/update")
    public ResultJson update(@ApiParam(name = "cfgPage",value = "页面对象",required = true) CfgPage cfgPage,
                             HttpServletRequest request) {
        try {
            CfgPage updateObj = cfgPageService.findOne(cfgPage.getId());
            updateObj.setTitle(cfgPage.getTitle());
            updateObj.setRemark(cfgPage.getRemark());
            updateObj.setStatus(cfgPage.getStatus());
            cfgPageService.update(updateObj);
            if (StatusConstants.INACTIVE.equals(updateObj.getStatus())) {
                cfgPageService.undeployService(request,cfgPage.getId());//取消发布
            }
            return success("更新成功");
        } catch (Exception e) {
            log.error("更新失败", e);
            return failure("更新失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "发布")
    @RequiresPermissions("cfgPage:deploy")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageId",value = "页面ID",required = true),
            @ApiImplicitParam(name = "parent",value = "操作行为",dataType="Long",required = true),
            @ApiImplicitParam(name = "roleIds",value = "角色IDS",required = true)
    })
    @PostMapping(value = "/deploy")
    public ResultJson deploy(String pageId, Long parent, String roleIds) {
        try {
            //往 menu和function 插数据
            ResultJson resultJson = cfgPageService.deploy( pageId,  parent,  roleIds,servletPath);
            return resultJson;
        } catch (Exception e) {
            log.error("发布失败", e);
            return failure("发布失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "取消发布")
    @RequiresPermissions("cfgPage:deploy")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageId",value = "页面ID",required = true)
    })
    @PutMapping(value = "/undeploy")
    public ResultJson undeploy(ServletRequest request, String pageId) {
        try {
            return cfgPageService.undeployService(request,pageId);
        } catch (Exception e) {
            log.error("取消发布失败", e);
            return failure("取消发布失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "报表配置导出")
    @RequiresPermissions("cfgPage:exportJson")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "导出报表IDS",required = true)
    })
    @GetMapping(value = "/json/export")
    public ResultJson exportJson(HttpServletRequest request, HttpServletResponse response, String ids) {
        ResultJson resultJson = new ResultJson();
        resultJson.setSuccess(false);
        if (StringUtils.isNotBlank(ids)) {
            List<RptPageBO> pageList = cfgPageService.exportJson(ids);
            if (CollectionUtils.isEmpty(pageList)) {
                resultJson.setMsg("未查到报表记录！");
            } else {
                String formatJsonStr = JsonUtils.formatJsonFile(JSONObject.toJSONString(pageList));
                StringBuilder fileName = new StringBuilder("rptExport_");
                fileName.append(System.currentTimeMillis()).append(".json");
                try {
                    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                    ServletOutputStream out = response.getOutputStream();
                    out.write(formatJsonStr.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.error("export report config failed, {}", e);
                    resultJson.setMsg(e.getMessage());
                }
            }
        } else {
            resultJson.setMsg("请选择需要导出的报表！");
        }
        resultJson.setSuccess(true);
        resultJson.setMsg("报表配置导出成功！");
        return resultJson;
    }

    @ApiOperation(value = "报表配置导入")
    @RequiresPermissions("cfgPage:importJson")
    @PostMapping(value = "/json/import")
    public ResultJson importJson(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        ResultJson resultJson = new ResultJson();
        if (null != file) {
            try {
                String rptJsonStr = CreateFileUtils.readFile(file.getInputStream());
                cfgPageService.importJson(rptJsonStr);
                resultJson.setSuccess(true);
                resultJson.setMsg("导入成功！");
            } catch (Exception e) {
                log.error("导入报表数据失败，原因：{}", e);
                resultJson.setSuccess(false);
                resultJson.setMsg(e.getMessage());
            }
        }
        return resultJson;
    }
    
    @ApiOperation(value = "报表预览权限查询")
    @RequiresPermissions("cfgPage:preview")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageId",value = "页面ID",required = true)
    })
    @GetMapping(value = "/preview")
    public ResultJson preview(ServletRequest request, String pageId) {
        if(StringUtils.isEmpty(pageId)){
        	return failure("预览失败");
        }
		HttpServletRequest req=(HttpServletRequest)request;
		HttpSession session=req.getSession();
		ActiveUser userDetail = (ActiveUser) session.getAttribute(GlobalConstants.SESSION_USER_INFO);

        CfgPage cfgPage=cfgPageService.findOne(pageId);
		if(cfgPage!=null&&(cfgPage.getCreatedUser().equals(userDetail.getUsername())||"超级管理员".equals(userDetail.getRoleName()))){
			return success("成功");
		}else{ 
			return failure("无预览权限");
		}
    }
}