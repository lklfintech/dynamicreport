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
package com.lakala.dynamicreport.common.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * list分页展示
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class PageForList<T>  {
 
 	private static final Logger logger = LoggerFactory.getLogger(PageForList.class);

	/**
	 * list分页展示
	 *
	 * @param page
	 * @param pageSize
	 * @param list
	 * @return
	 */
	public   List<T> getListPage(int page, int pageSize,
			List<T> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		int totalCount = list.size();
		page = page - 1;
		int fromIndex = page * pageSize;
		int toIndex = ((page + 1) * pageSize);
		if (toIndex > totalCount) {
			toIndex = totalCount;
			//fromIndex=0;
		}
		return list.subList(fromIndex, toIndex);
	}

	/**
	 * 返回总页数
	 *
	 * @param obj
	 * @param pageSize
	 * @return
	 */
	public   int getPages(List<T> obj, Integer pageSize) {
		int count = obj.size() / pageSize;
		if (obj.size() == 0) {
			return 0;
		}
		if (obj.size() <= pageSize) {
			return 1;
		} else if (count % pageSize == 0) {
			return count;
		} else {
			return count + 1;
		}

	}

	/**
	 * 分页公共参数部分
	 *
	 * @param defaultSortName
	 * @param query
	 * @return
	 */
	public static Pageable getPageable(String defaultSortName,Object query){
		Pageable pageable = null;
		try {
			String sortOrderValue = getSimpleFieldValue(query,"sortOrder") != null?
										String.valueOf(getSimpleFieldValue(query,"sortOrder")):"";
			String sortNameValue = getSimpleFieldValue(query,"sortName") != null?
										String.valueOf(getSimpleFieldValue(query,"sortName")):"";
			int pageNumberValue = getSimpleFieldValue(query,"pageNumber") != null?
										(int)getSimpleFieldValue(query,"pageNumber"):1;
			int pageSizeValue = getSimpleFieldValue(query,"pageSize") != null?
										(int)getSimpleFieldValue(query,"pageSize"):10;

			Sort.Direction direction = Sort.Direction.DESC;

			if (null != sortOrderValue && sortOrderValue.equalsIgnoreCase(SortDirection.ASC.toString())) {
				direction = Sort.Direction.ASC;
			}
			if (StringUtils.isNotBlank(sortNameValue)) {
				defaultSortName = sortNameValue;
			}
			// 分页是从0开始
			int pageNumber = pageNumberValue;
			if (pageNumber == 0) {
				pageNumber = 1;
			}
//			pageable = new PageRequest(pageNumber - 1, pageSizeValue, direction, defaultSortName);
			pageable =PageRequest.of(pageNumber - 1, pageSizeValue, direction, defaultSortName);
		}catch (Exception e){
			logger.error(String.format("获取Pageable异常：%s",e.getMessage()),e);
		}

		return pageable;
	}

	/**
	 * 分页公共参数部分 获取Sort排序对象
	 *
	 * @param defaultSortName
	 * @param query
	 * @return
	 */
	public static Sort getSort(String defaultSortName,Object query){
		Sort sort = null;
		try {
			String sortOrderValue = getSimpleFieldValue(query,"sortOrder") != null?
										String.valueOf(getSimpleFieldValue(query,"sortOrder")):"";
			String sortNameValue = getSimpleFieldValue(query,"sortName") != null?
										String.valueOf(getSimpleFieldValue(query,"sortName")):"";

			Sort.Direction direction = Sort.Direction.ASC;

			if (SortDirection.DESC.toString().equalsIgnoreCase(sortOrderValue)) {
				direction = Sort.Direction.DESC;
			}
			if (StringUtils.isNotBlank(sortNameValue)) {
				defaultSortName = sortNameValue;
			}
			sort = new Sort(direction,defaultSortName);
		}catch (Exception e){
			logger.error(String.format("获取Sort异常：%s",e.getMessage()),e);
		}

		return sort;
	}

	/**
	 * 返回对象特定属性的值，如果属性不存在，返回null
	 *
	 * @param o
	 * @param filedName
	 * @return
	 * @throws Exception
	 */
	public static Object getSimpleFieldValue(Object o, String filedName) throws Exception {
		try {
			Field f = o.getClass().getField(filedName);
			f.setAccessible(true);
			return f.get(o);
		}catch(NoSuchFieldException e){
			//出现不支持属性不做处理，返回null即可
			return null;
		}
	}

}
