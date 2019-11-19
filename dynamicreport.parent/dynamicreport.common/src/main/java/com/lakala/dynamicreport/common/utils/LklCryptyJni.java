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

import com.lakala.dynamicreport.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

/**
 * <p>
 * 密码加密工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class LklCryptyJni {
	private static Logger logger=LoggerFactory.getLogger(LklCryptyJni.class);
    private static final int BUFFERSIZE=8192;
    private static final String DLLFILENAME="lklcrypt";

    // hide public constructors
    private LklCryptyJni() {
        throw new IllegalStateException("LklCryptyJni class");
    }
    
    private static void loadLib(String libName) throws IOException {
        System.getProperty("os.name");
        String libExtension =  ".so";
        String prefix =   "lib";
        String libFullName = prefix + libName + libExtension;
        // 动态库的输出目录 可自行设置
        String nativeTempDir = System.getProperty("user.dir");


        InputStream in = null;
        BufferedInputStream reader = null;
        FileOutputStream writer = null;
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        File extractedLibFile = new File(nativeTempDir + File.separator + prefix + uuid + libExtension);
        if (extractedLibFile.exists()) {
            extractedLibFile.delete();
        }

        try {
            // “/”代表Jar包的根目录
            in = LklCryptyJni.class.getResourceAsStream("/" + libFullName);
            if (in == null)
                in = LklCryptyJni.class.getResourceAsStream(libFullName);

            reader = new BufferedInputStream(in);
            writer = new FileOutputStream(extractedLibFile);

            byte[] buffer = new byte[BUFFERSIZE];

            while (reader.read(buffer) > 0) {
                writer.write(buffer);
                buffer = new byte[BUFFERSIZE];
            }
        } catch (IOException e) {
        	logger.error(e.getMessage());
            throw e;
        } finally {
            extractedLibFile.deleteOnExit();
            if (null != in) {
                in.close();
            }
            if (null != reader) {
            	reader.close();
            }
            if (null != writer) {
                writer.close();
            }
        }

        System.load(extractedLibFile.toString());

    }
    static {
        try {
            loadLib(DLLFILENAME);
            int retval = doInit();
            if (retval < 0) {
                throw new BusinessException("decrypty lib init failure,retcode=" + retval);
            }

        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public static String encrypt(String src)
    {
        if(StringUtils.isBlank(src))
            return src;
        return doEncrypt(src);
    }

    public static String decrypt(String userName,String password,String src)
    {
        if(src==null||src.length()==0)
            return src;
        return doDecrypt(userName,password,src);
    }

    /*crypt*/
    private static native String doEncrypt(String src);

    /*decrypt*/
    private static native String doDecrypt(String userName,String password,String src);

    /*init*/
    private static native int doInit();

}
