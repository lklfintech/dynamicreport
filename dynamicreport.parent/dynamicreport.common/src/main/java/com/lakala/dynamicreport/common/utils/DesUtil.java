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

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * <p>
 * 3DES加解密工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class DesUtil {
	private static final String ALGORITHM_MD5 = "md5";
	private static final String ALGORITHM_DESEDE = "DESede";//加密算法，可用 DES，DESede，Blowfish
	private static final String CHARSET = "UTF-8";

	final private static String key="lakala_risk";

	/**
	 * 加密
	 *
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String source) throws Exception {
		if(StringUtils.isEmpty(source))return "";
		//生成的密文
		byte[] cryptograph = encryptOrDecrypt(source.getBytes(), get3DESKeyBytes(key), Cipher.ENCRYPT_MODE);
		//通过Base64编码为ASCII字符后传输
		String cryptographStr = Base64.getEncoder().encodeToString(cryptograph);
		return cryptographStr;
	}

	/**
	 * 解密
	 *
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String source) throws Exception{
		if(StringUtils.isEmpty(source))return "";
		byte[] targetBase64 = Base64.getDecoder().decode(source.getBytes(CHARSET));

		byte[] target = encryptOrDecrypt(targetBase64, get3DESKeyBytes(key), Cipher.DECRYPT_MODE);
		return new String(target, CHARSET);
	}    

	/**
	 * 加密或解密。加密和解密用的同一个算法和密钥
	 *
	 * @param src    要加密或解密的数据
	 * @param key    密钥
	 * @param mode        加密或解密模式。值请选择Cipher.DECRYPT_MODE或Cipher.ENCRYPT_MODE
	 * @return         加密或解密后的数据
	 */
	private static byte[] encryptOrDecrypt(byte[] src, byte[] key, int mode) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_DESEDE); //负责加密/解密的Cipher工具类
		Key key3DES = new SecretKeySpec(key, ALGORITHM_DESEDE); //3DES密钥
		cipher.init(mode, key3DES); //加密模式
		return cipher.doFinal(src);//按单部分操作加密或解密数据，或者结束一个多部分操作。返回：包含结果的新缓冲区 
	}

	/**
	 * 根据字符串生成3DES的密钥字节数组<br>
	 * 注意java的3des为24位密钥(24*8=192bit)，c代码的话只要16位(16*8=128bit)
	 *
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	private static byte[] get3DESKeyBytes(String sKey) throws Exception {
		//获得指定摘要算法的 MessageDigest 对象
		MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
		//使用指定的字节更新摘要（继续多部分加密或解密操作，以处理其他数据部分）
		md.update(sKey.getBytes(CHARSET));
		//获得密文。注意：长度为16而不是32。一个字节(byte)占8位(bit)
		byte[] digestOfPassword = md.digest();
		//将16位消息摘要数组中的内容，拷贝到一个长度为【24】的数组中
		byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
		//再用前8位数据对应补全后8位
		for (int j = 0, k = 16; j < 8;) {
			keyBytes[k++] = keyBytes[j++];
		}
		return keyBytes;
	}
}
