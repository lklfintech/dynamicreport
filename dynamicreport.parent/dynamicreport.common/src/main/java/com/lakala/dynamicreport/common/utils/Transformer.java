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
package com.lakala.dynamicreport.common.utils;

import org.hibernate.transform.AliasToBeanResultTransformer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用于动态SQL查询，转化对象工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class Transformer extends AliasToBeanResultTransformer{
	 
	private static final long serialVersionUID = 1L;
	private Class<?>  resultClass;
	
	public Transformer(Class<?>  resultClass) {
		super(resultClass);
		this.resultClass = resultClass;
	}

	/**
	 * 转换
	 *
	 * @param tuple
	 * @param aliases
	 * @return
	 */
	public List<Object> transformTuple(Object[] tuple, String[] aliases) {
		  List<Object> list = new ArrayList<Object>();
		  Object obj = null;
			try {
				obj = resultClass.newInstance();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}
		  Method[] methods = resultClass.getMethods();// 返回这个类里面方法的集合  
		  for(int k=0;k<aliases.length;k++){
			  String aliase=aliases[k];
			  char[] ch = aliase.toCharArray();  
			  ch[0] = Character.toUpperCase(ch[0]);  
			  String s = new String(ch);  
			  String[] names = new String[] { ("set" + s).intern(),  
			    ("get" + s).intern(), ("is" + s).intern(),  
			    ("read" + s).intern() };  
			  Method setter = null;  
			  Method getter = null;  
			  int length = methods.length;  
			  for (int i = 0; i < length; ++i) {  
			   Method method = methods[i];  
			   /** 
			    * 检查该方法是否为公共方法,如果非公共方法就继续 
			    */  
			  if (!Modifier.isPublic(method.getModifiers()))  
			    continue;  
			    String methodName = method.getName();  
 			   for (String name : names) {  
			    if (name.equals(methodName)) {  
			     if (name.startsWith("set") || name.startsWith("read"))  
			      setter = method;  
			     else if (name.startsWith("get") || name.startsWith("is"))  
			      getter = method;  
			  
			    }  
			   }  
			  }
			  if(getter!=null){
				  //Object[] param = buildParam(getter.getReturnType().getName(), tuple[k]);  
				  try {  
				   setter.invoke(obj, tuple[k]);
				  } catch (Exception e) {  
				   e.printStackTrace();  
				  }
			  }
		  }
		  list.add(obj);
		  return list;
	}
	 
}
 
 
 