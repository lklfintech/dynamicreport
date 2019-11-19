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
import com.lakala.dynamicreport.datamodel.query.DataParameterQuery;
import com.lakala.dynamicreport.datamodel.service.IDataParameterService;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据参数controller
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestController
@RequestMapping("/data-parameter")
@Api(tags = "数据参数管理",description = "数据参数控制器(DataParameterController)")
public class DataParameterController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(DataParameterController.class);

	@Autowired
	private IDataParameterService dataParameterService;

	@ApiOperation(value = "分页")
	@RequiresPermissions("dataParameter:query")
	@GetMapping(value = "/list")
	public PageBean<DataParameter> list(@ApiParam(name = "query",value = "数据参数查询对象",required = true) DataParameterQuery query) {
		PageBean<DataParameter> pb = new PageBean<DataParameter>();
		Page<DataParameter> page = dataParameterService.findDataParameterCriteria(query);
		pb.setRowsCount(page.getTotalElements());
		pb.setPageTotal(page.getTotalPages());
		pb.setData(page.getContent());
		return pb;
	}

	@ApiOperation(value = "保存数据参数")
	@RequiresPermissions("dataParameter:save")
	@PostMapping(value = "/save")
	public ResultJson save(@ApiParam(name = "dataParameter",value = "数据参数对象",required = true) DataParameter dataParameter) {
		// 保存
		try {
			DataParameterQuery query = new DataParameterQuery();
			query.setIdentifier(dataParameter.getIdentifier());
			List<DataParameter> list = dataParameterService.findDataParameter(query);
			if (CollectionUtils.isNotEmpty(list)) {
				return failure("保存失败，数据参数标识" + dataParameter.getIdentifier() + "有重复");
			}
 			dataParameterService.save(dataParameter);
			return success("保存成功");
		} catch (Exception e) {
			log.error("[DataParameterController : save]" + dataParameter, e);
			return failure("保存失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "刪除数据参数")
	@RequiresPermissions("dataParameter:delete")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "操作行为",paramType = "path",dataType="Long",required = true)
	})
	@DeleteMapping(value = "/{id}")
	public ResultJson delete(@PathVariable Long id) {
		try {
			DataParameter ds=dataParameterService.findOne(id);
 			dataParameterService.delete(ds.getId());//物理删除数据
			return success("刪除成功");
		} catch (Exception e) {
			log.error("[DataParameterController : delete]" + id, e);
			return failure("刪除失败" + e.getMessage(), e);
		}
	}

	@ApiOperation(value = "更新数据参数")
	@RequiresPermissions("dataParameter:save")
	@PutMapping(value = "/update")
	public ResultJson update(@ApiParam(name = "dataParameter",value = "数据参数对象",required = true) DataParameter dataParameter) {
		try {
			DataParameter updateObj = dataParameterService.findOne(dataParameter.getId());
			
			updateObj.setParamName(dataParameter.getParamName());
			updateObj.setName(dataParameter.getName());
			updateObj.setParamContent(dataParameter.getParamContent());
			updateObj.setPrefix(dataParameter.getPrefix());
			updateObj.setSuffix(dataParameter.getSuffix());
			updateObj.setMandatory(dataParameter.getMandatory());
			updateObj.setDescription(dataParameter.getDescription());
			updateObj.setStatus(dataParameter.getStatus());
 			dataParameterService.update(updateObj);
			return success("更新成功");
		} catch (Exception e) {
			log.error("[DataParameterController : update]" + dataParameter, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "新增或者更新")
	@RequiresPermissions("dataParameter:save")
	@PostMapping(value = "/save-update")
	public ResultJson saveOrUpdate(@ApiParam(name = "dataParameter",value = "数据参数对象",required = true) DataParameter dataParameter) {
		try {
			if (dataParameter.getId() != null) {
				return update(dataParameter);
			} else {
				return save(dataParameter);
			}
		} catch (Exception e) {
			log.error("[DataParameterController : save_update]" + dataParameter, e);
			return failure("更新失败" + e.getMessage());
		}
	}

	@ApiOperation(value = "查询单条数据")
	@RequiresPermissions("dataParameter:query")
	@ApiImplicitParams({
	        @ApiImplicitParam(name = "id",value = "操作行为",paramType = "path",dataType="Long",required = true)
	})
	@GetMapping(value = "/list/{id}")
	public DataParameter getOne(@PathVariable Long id) {
		return dataParameterService.findOne(id);
	}

	@ApiOperation(value = "查询所有数据")
	@RequiresPermissions("dataParameter:query")
	@GetMapping(value = "/list-all")
	public List<DataParameter> queryAll(@ApiParam(name = "dataParameterQuery",value = "数据参数查询对象",required = true) DataParameterQuery dataParameterQuery) {
		dataParameterQuery.setStatus(StatusConstants.ACTIVE);
		return dataParameterService.findDataParameter(dataParameterQuery);
	}
	
}
