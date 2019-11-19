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
package com.lakala.dynamicreport.common.rsa;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakala.dynamicreport.common.utils.RSATools;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * 请求响应处理类
 * 对加了@Encrypt的方法的数据进行加密操作
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private Logger logger = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * RSA加密私钥
     */
    private String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKJCAqMWCOn5M84ldKFYThMkEPaRVpumgfbqssgDwlExAEPnyyFrxgYEIvC6wVPm1Azqi+QfC6eqYV2irtf+Fa4MYFoJSQZwdoHK9jKK24jsuZHPNd+aDrsqMLju5kZd9UKUjOdYAB3okUoykS2NhJFAXqZcEFdYDh9ipXIMc/hzAgMBAAECgYBO0BNp1fUkeOhB9I2PJU5fG8Clk3jnB9zO67dFUhYby9peIYPYSvoXyACUv5HSSCet0mt6td26Zhb/HVsiDQfICQ5qIwrtMqozUAo8+TdLYNFppratw3x9AW4R6P61uu+lpOfJfqikNpxpjX1FFRKJOj1IMhQG+ZXEqNhlyVuCeQJBAN1RSx2n7K1Xf/cHvdG8tMvTNpqYC8SobJ2Q5RTdTCQncNbbXkiPqUDFoeS0dHrmR7UtKxumo0cFYnDq/Jw80TUCQQC7r2O0BtBDCqEERn3ERcFJB+tv7nHCrQgw1znW5MpxebpRfk4JZg72dQD/8/nIoJGcD610aRVvC/wsXrPtKUAHAkBqJF3ZKDsrW0ak7BKOb2lqQ67xb7BdLcWBwAcN/aBRypSkhurt8/BhfydIr38Zni2P58y/4yh1nFB9BUYHjGdtAkBw0c5KMmpQUnxV2JXTPOciIluZTikeD+tDiKzhC2jR641fXWBFcA8AgXbXZO46dqLwv8/RkTVsKjUs6sSXhFtTAkBOhC05wZK8uqeJ5VdJFXr4xMPsJSXXRWxcgHWInTBGVFTUZsJeRyzPuBfgS4EO8GtuBf7Dd9SSN0B/Oh87ZxAd";

    private static ThreadLocal<Boolean> encryptLocal = new ThreadLocal<>();

    public static void setEncryptStatus(boolean status) {
        encryptLocal.set(status);
    }

    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 可以通过调用EncryptResponseBodyAdvice.setEncryptStatus(false);来动态设置不加密操作
        Boolean status = encryptLocal.get();
        if (status != null && status == false) {
            encryptLocal.remove();
            return body;
        }

        boolean encrypt = false;
        if (returnType.getMethod().isAnnotationPresent(Encrypt.class)) {
            encrypt = true;
        }
        if (encrypt) {
            String privateKey = this.privateKey;
            try {
                String content = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                if (StringUtils.isBlank(privateKey)) {
                    throw new NullPointerException("请配置spring.encrypt.privatekeyc参数");
                }
                if (StringUtils.isNotBlank(content)) {
                    Map<String, Object> contentMap = JSONObject.parseObject(content, Map.class);
                    String data = (String) contentMap.get("data");
                    if (StringUtils.isNotBlank(data)) {
                        String encryptData = Base64.encodeBase64String(RSATools.encryptByPrivateKey(data.getBytes(StandardCharsets.UTF_8), privateKey));
                        contentMap.put("data", encryptData);
                        JSONObject responseJson = RSATools.sortMap(contentMap);
                        String sign = RSATools.sign(responseJson.toString().getBytes(StandardCharsets.UTF_8), privateKey);
                        responseJson.put("sign", sign);
                        return responseJson;
                    }
                }
            } catch (Exception e) {
                logger.error("加密数据异常", e);
            }
        }

        return body;
    }

}
