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

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * <p>
 * ip地址工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class IPAddressUtil {
	/**
	 * 获取本地局域网的ip地址
	 *
	 * @return
	 */
	public static String getLocalIpAddress(){
		try{
			if(isWindowsOS()){
				return InetAddress.getLocalHost().getHostAddress();
			}else{
				return getLinuxLocalIpAddress(true);
			}
		}catch(Exception e){
			e.printStackTrace();
    		return "127.0.0.1";
		}
	}
	
	/**
	 * 获取linux下的本地ip地址
	 *
	 * @param localAddr
	 * @return
	 * @throws Exception
	 */
	public static String getLinuxLocalIpAddress(boolean localAddr) throws Exception{
			String ip = "127.0.0.1";
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				if (netInterface.isVirtual() || netInterface.isLoopback() || !netInterface.isUp()) {
					continue;
				}
				List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
				for (InterfaceAddress addr : InterfaceAddress) {
					InetAddress Ip = addr.getAddress();
					if (Ip != null && Ip instanceof Inet4Address && !Ip.isLoopbackAddress()) {
						if (localAddr) {
							if (Ip.isSiteLocalAddress()) {
								// 内网IP
								ip = Ip.getHostAddress();
								break;
							}
						} else {
							if (!Ip.isSiteLocalAddress()) {
								ip = Ip.getHostAddress();
								break;
							}
						}
					}
				}
			}
			return ip;
	}
	
	 /**
	 * 判断操作系统是否是Windows
	 *
	 * @return
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}
	
	/**
	 * 获取客户端ip地址
	 *
	 * @param request
	 * @return
	 */
	public static String getClientIpAddress(HttpServletRequest  request) {
		String client_ip = request.getHeader("x-forwarded-for");
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	        client_ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	        client_ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(client_ip == null || client_ip.length() == 0 || "unknown".equalsIgnoreCase(client_ip)) {
	        client_ip = request.getRemoteAddr();
	        if(client_ip.equals("127.0.0.1") || client_ip.equals("0:0:0:0:0:0:0:1")){
	            //根据网卡取本机配置的IP
	            InetAddress inet = null;
	            try {
	                inet = InetAddress.getLocalHost();
	            } catch (UnknownHostException e) {
	                e.printStackTrace();
	            }
	            client_ip = inet.getHostAddress();
	        }  
	    }  
	    //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
	    if(client_ip != null && client_ip.length() > 15){ //"***.***.***.***".length() = 15  
	        if(client_ip.indexOf(",") > 0){
	            client_ip = client_ip.substring(0,client_ip.indexOf(","));
	        }
	    }
	    return client_ip;
	}
	public static void main(String[] args) {
		System.out.println(getLocalIpAddress());
	}
}
