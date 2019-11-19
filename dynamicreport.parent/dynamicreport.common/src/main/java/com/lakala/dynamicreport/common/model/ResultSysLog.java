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

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;

/**
 * <p>
 * 用户操作日志类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class ResultSysLog  implements Cloneable{
    
 
	/**
	 * 操作的用户名
	 */
    private String userName; 

	/**
	 * 操作URL
	 */
    private String url;
    
	/**
	 * 参数
	 */
    private String params; 
  
	/**
	 * ip地址
	 */
    private String ip;
    
	/**
	 * 类型
	 */
    private String type; 
  
	/**
	 * 信息
	 */
    private String message;

	/**
	 * 操作时间
	 */
    private Timestamp createDate;

	 

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 使得序列化student3的时候也会将teacher序列化
	 *
	 * @return
	 * @throws Exception
	 */
	public Object deepCopt()throws Exception
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream  oos = new ObjectOutputStream(bos);
		oos.writeObject(this);
		//将当前这个对象写到一个输出流当中，，因为这个对象的类实现了Serializable这个接口，所以在这个类中
		//有一个引用，这个引用如果实现了序列化，那么这个也会写到这个输出流当中

		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return ois.readObject();
		//这个就是将流中的东西读出类，读到一个对象流当中，这样就可以返回这两个对象的东西，实现深克隆
	}

}
