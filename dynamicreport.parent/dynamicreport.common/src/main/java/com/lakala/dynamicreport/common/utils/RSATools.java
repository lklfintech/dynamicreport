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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * <p>
 * RSA工具类
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
public class RSATools {
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "LocatorPublicKey";

    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "LocatorPrivateKey";

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * <p>
     * 用公钥校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(RSA_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * <p>
     * 获取公钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    // 字典序排序
    public static JSONObject sortMap(Map map) {
        JSONObject json = new JSONObject(true);
        Collection<String> keyset = map.keySet();
        List list = new ArrayList<>(keyset);
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            json.put((String) list.get(i), map.get(list.get(i)));
        }
        return json;
    }

    private RSATools() {
        // init
    }

    public static void main(String[] args) throws Exception {
//        Map<String, Object> keyMap = genKeyPair();
//        System.out.println("getPublicKey:" + getPublicKey(keyMap));
//        System.out.println("getPrivateKey:" + getPrivateKey(keyMap));
//        byte[] encryptArray = encryptByPrivateKey("{\"identifier:\":\"123456789\"}".getBytes("UTF-8"), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKQsHIPdSpu/LU7m+8r62/y02N+0ocG9yvU607Rdz/D8wzvclbZ8tobm8XWF8ICQm1n1EnSlo69qPIpo3+6ZouHPQEFtbW1AS9WEczVytxp1V/DbdOvBbtvn35ypjeVH4g6pmxOT9ruMzA6nSuysu9JQj00K9J7pzrGSAka5zAglAgMBAAECgYEAn0PT3fDeIj3S3IqkloPKyls/UbZQfj17GXAaVQ31Xpo3ZGlYLBLMmA6PDC4F5RNkQl7HqrXOF6x+Ut/u7e/J3KxCK0yDd5tMChjuthTvAO2eq0yIjhPrdq6Qea3/kXZF/2dbue5CLSPqJrDHvZmat/5przmrJxu2erfXvdpyFHkCQQDUxX3tb5pnf2/A3lZqcH6X1l1iFu90oeaIxrGTQTwtyUNm+8oqpboh1cB5Y0AyQR//R6s/WSraGif3+JNkpb1zAkEAxYbq5qpaThPAG5lBji2rQJGnh46qAOLJu0rpNXW92OzfqU2AmW7qy9oRm7L/M9ghV04JIqTEhkGutYeB3JY+BwJAXhlZsACDkPJTQkEShtBprE0pz4/FJxfTFPqK3k0jiDxSiuEgb+wLfZtDHGxAn/kdZZEUs1eEFCEtPAW7ae/C8QJALuT8Ca0x6U1pE2RMGvwepyKtWlJ1a+ZrrEp5mVaNrt11sBUVTdf2aFUqTwOs8NsD910wcAgcHB+l4bT9EKieqwJAKb5SYgG3XZhrZ6s5ewdXe7VLPpdGFZfzKzTMUJYoZkt2OTBR2yGoKsDLhJL7do7XEZqH9Oyh30CcTJUWjwf50g==");
//        System.out.println(new String(encryptArray));
//        System.out.println(Base64.encodeBase64String(encryptArray));


        String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKJCAqMWCOn5M84ldKFYThMkEPaRVpumgfbqssgDwlExAEPnyyFrxgYEIvC6wVPm1Azqi+QfC6eqYV2irtf+Fa4MYFoJSQZwdoHK9jKK24jsuZHPNd+aDrsqMLju5kZd9UKUjOdYAB3okUoykS2NhJFAXqZcEFdYDh9ipXIMc/hzAgMBAAECgYBO0BNp1fUkeOhB9I2PJU5fG8Clk3jnB9zO67dFUhYby9peIYPYSvoXyACUv5HSSCet0mt6td26Zhb/HVsiDQfICQ5qIwrtMqozUAo8+TdLYNFppratw3x9AW4R6P61uu+lpOfJfqikNpxpjX1FFRKJOj1IMhQG+ZXEqNhlyVuCeQJBAN1RSx2n7K1Xf/cHvdG8tMvTNpqYC8SobJ2Q5RTdTCQncNbbXkiPqUDFoeS0dHrmR7UtKxumo0cFYnDq/Jw80TUCQQC7r2O0BtBDCqEERn3ERcFJB+tv7nHCrQgw1znW5MpxebpRfk4JZg72dQD/8/nIoJGcD610aRVvC/wsXrPtKUAHAkBqJF3ZKDsrW0ak7BKOb2lqQ67xb7BdLcWBwAcN/aBRypSkhurt8/BhfydIr38Zni2P58y/4yh1nFB9BUYHjGdtAkBw0c5KMmpQUnxV2JXTPOciIluZTikeD+tDiKzhC2jR641fXWBFcA8AgXbXZO46dqLwv8/RkTVsKjUs6sSXhFtTAkBOhC05wZK8uqeJ5VdJFXr4xMPsJSXXRWxcgHWInTBGVFTUZsJeRyzPuBfgS4EO8GtuBf7Dd9SSN0B/Oh87ZxAd";
        // 加密请求参数
        JSONObject requestData = new JSONObject();
        requestData.put("token", "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ8kLKCbMB1otZAkOZBi+LjQQ/9zrES71gVx+bq/+63zSzzoquo8M2H7KrcrfKKZ9pmILNGwlakwgDBOCIjkkOurhvMcjhduC64ahvQnQRmV+ujxywDuujietMOMI7Z27bZXTX6iT9idig8FCR318WqbBmR0PCHuLjakMtjUeHRTAgMBAAECgYAoyGFzazHCZ6IX5WCnIuXwnta+MUuVdbmcPaAs/qQl6PKAEnQygOj+FBrXRBq1pNUwdVhfCGziJd7EAktnoYONp2X7bg5mv3I1KXWHIo6j5sVJNJeQcozHjHmNFH/6d89z7Au2/dMTdNdzPvclkrAJVIHF2Fhrnq+0mwxbeIOmuQJBAP7733aZbFcqdglLax33gm4udTL5qDL9U+t8YAgnqEknV/0S41/uFDy2+55apl1Bfgwbxagwfh5F7zhLac8Y/X8CQQCfxoaP11wW0cjBZXlVx0YyEb7ZAhOjAseBriEtyV/GnSv7sx05meLdX5yMR3MCzA/Ke63W+Gth3aaCQBOwaJstAkA57K4PGztpcdPuuJtU621N6m/lbKGxM6tky5RlRd0NAUsTyr6shEFEqazvhFhWxxIDXivA0YNVDT4S487dpSexAkBDKvNgvvmAwlpyKxWSIP76h4lCs2PL8JwsgSaDmBeCaIqX+pl7oJzF0a7/Lf5Em8K7rbjMGY0TDFo2gXeFfsRVAkEA++w7K8r3Xcuj5uOYgmH4xA3gI/P40A6F9vD4cqj5Kl+Zqdtk2/y+njs+O35f7tw3J1t+My9RVjUUdIyXvTOWbw==");
        requestData.put("identifier", "123456789");
        String encryptRequestData = Base64.encodeBase64String(RSATools.encryptByPrivateKey(requestData.toJSONString().getBytes(StandardCharsets.UTF_8), privateKey));

        // 拼装风控接口请求参数
        Map<String, Object> requestMap = new HashMap<>();
//        requestMap.put("prdId", "prdId123");
        requestMap.put("timestamps", System.currentTimeMillis());
//        requestMap.put("customerId", "abc");
        requestMap.put("data", encryptRequestData);
//        requestMap.put("password", "pwd123");
        // 参数加签
        JSONObject requestMapJson = sortMap(requestMap);
        String sign = RSATools.sign(requestMapJson.toString().getBytes(StandardCharsets.UTF_8), privateKey);
        requestMapJson.put("sign", sign);
        System.out.println(requestMapJson);
    }
}
