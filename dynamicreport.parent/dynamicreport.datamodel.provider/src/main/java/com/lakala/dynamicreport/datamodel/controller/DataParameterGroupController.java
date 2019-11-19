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
package com.lakala.dynamicreport.datamodel.controller;

import com.lakala.dynamicreport.common.constants.StatusConstants;
import com.lakala.dynamicreport.common.controller.BaseController;
import com.lakala.dynamicreport.common.model.PageBean;
import com.lakala.dynamicreport.common.model.ResultJson;
import com.lakala.dynamicreport.datamodel.model.DataParameter;
import com.lakala.dynamicreport.datamodel.model.DataParameterGroup;
import com.lakala.dynamicreport.datamodel.query.DataParameterGroupQuery;
import com.lakala.dynamicreport.datamodel.query.DataParameterQuery;
import com.lakala.dynamicreport.datamodel.service.IDataListService;
import com.lakala.dynamicreport.datamodel.service.IDataParameterGroupService;
import com.lakala.dynamicreport.datamodel.service.IDataParameterService;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * <p>
 * 数据参数组controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/data-parameter-group")
@Api(tags = "数据参数组管理",description = "数据参数组控制器(DataParameterGroupController)")
public class DataParameterGroupController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DataParameterGroupController.class);

	@Autowired
	private IDataParameterGroupService dataParameterGroupService;

	@Autowired
	private IDataParameterService dataParameterService;

	@Autowired
	private IDataListService dataListService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("dataParameterGroup:query")
	@GetMapping(value = "/list")
	public PageBean<DataParameterGroup> list(@ApiParam(name = "query",value = "数据参数组查询对象",required = true) DataParameterGroupQuery query) {
		PageBean<DataParameterGroup> pb = new PageBean<>();
		Page<DataParameterGroup> page = dataParameterGroupService.findDataParameterGroupCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "保存数据参数组")
	@RequiresPermissions("dataParameterGroup:save")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "dataParameterGroup",value = "数据参数组对象",required = true) DataParameterGroup dataParameterGroup) {
		// 保存
		try {
			DataParameterGroupQuery query = new DataParameterGroupQuery();
			query.setIdentifier(dataParameterGroup.getIdentifier());
			List<DataParameterGroup> list = dataParameterGroupService.findDataParameterGroup(query);
			if (CollectionUtils.isNotEmpty(list)) {
				return failure("保存失败，数据参数组标识" + dataParameterGroup.getIdentifier() + "有重复");
			}
 			dataParameterGroupService.save(dataParameterGroup);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[DataParameterGroupController : save]" + dataParameterGroup, e);
			return failure("保存失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除数据参数组")
	@RequiresPermissions("dataParameterGroup:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "数据主键",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id ) {
		try {
			DataParameterGroup ds=dataParameterGroupService.findOne(id);
 			dataParameterGroupService.delete(ds.getId());//物理删除数据
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[DataParameterGroupController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "更新数据参数组")
	@RequiresPermissions("dataParameterGroup:save")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "dataParameterGroup",value = "数据参数组对象",required = true) DataParameterGroup dataParameterGroup) {
		try {
			DataParameterGroup updateObj = dataParameterGroupService.findOne(dataParameterGroup.getId());

			updateObj.setName(dataParameterGroup.getName());
			updateObj.setContent(dataParameterGroup.getContent());
			updateObj.setDescription(dataParameterGroup.getDescription());
			updateObj.setStatus(dataParameterGroup.getStatus());
 			dataParameterGroupService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[DataParameterGroupController : update]" + dataParameterGroup, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("dataParameterGroup:save")
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "dataParameterGroup",value = "数据参数组对象",required = true) DataParameterGroup dataParameterGroup) {
		if(StatusConstants.ACTIVE.equals(dataParameterGroup.getStatus())){
			
			List<String> paramIdens=dataListService.getSqlParamList(dataParameterGroup.getContent());

			for(String paramIden:paramIdens){
				List<DataParameter> paramList=dataParameterService.findDataParameter(new DataParameterQuery(paramIden));
				if(CollectionUtils.isEmpty(paramList)) 
					return failure(String.format("参数{%s}不存在",paramIden));
				if(StatusConstants.INACTIVE.equals(paramList.get(0).getStatus())) 
					return failure(String.format("参数{%s}停用",paramIden));
			}
		}
		try {
			if (dataParameterGroup.getId() != null) {
				return update(dataParameterGroup);
			} else {
				return save(dataParameterGroup);
			}
		} catch (Exception e) {
			log.error("[DataParameterGroupController : save_update]" + dataParameterGroup, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("dataParameterGroup:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "操作行为",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public DataParameterGroup getOne(@PathVariable Long id) {
		return dataParameterGroupService.findOne(id);
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("dataParameterGroup:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "searchText",value = "搜索内容",paramType = "query",dataType="String")
	})
	@GetMapping(value = "/list-all")
	public List<DataParameterGroup> queryAll(@ApiIgnore DataParameterGroupQuery query) {
		query.setStatus(StatusConstants.ACTIVE);
		return dataParameterGroupService.findDataParameterGroup(query);
	}
}
