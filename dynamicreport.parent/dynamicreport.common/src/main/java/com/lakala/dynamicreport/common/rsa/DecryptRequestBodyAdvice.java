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
import com.lakala.dynamicreport.common.utils.RSATools;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * 请求请求处理类（目前仅仅对requestbody有效）
 * 对加了@Decrypt的方法的数据进行解密密操作
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@RestControllerAdvice
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    private Logger logger = LoggerFactory.getLogger(DecryptRequestBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (parameter.getMethod().isAnnotationPresent(Decrypt.class)) {
            try {
                return new DecryptHttpInputMessage(inputMessage);
            } catch (Exception e) {
                logger.error("数据解密失败", e);
                throw new IOException(e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}

class DecryptHttpInputMessage implements HttpInputMessage {
    private HttpHeaders headers;
    private InputStream body;
    private Logger logger = LoggerFactory.getLogger(DecryptHttpInputMessage.class);

    public DecryptHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
        //RSA加密公钥
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiQgKjFgjp+TPOJXShWE4TJBD2kVabpoH26rLIA8JRMQBD58sha8YGBCLwusFT5tQM6ovkHwunqmFdoq7X/hWuDGBaCUkGcHaByvYyituI7LmRzzXfmg67KjC47uZGXfVClIznWAAd6JFKMpEtjYSRQF6mXBBXWA4fYqVyDHP4cwIDAQAB";
        //token
        String cacheRefreshToken = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ8kLKCbMB1otZAkOZBi+LjQQ/9zrES71gVx+bq/+63zSzzoquo8M2H7KrcrfKKZ9pmILNGwlakwgDBOCIjkkOurhvMcjhduC64ahvQnQRmV+ujxywDuujietMOMI7Z27bZXTX6iT9idig8FCR318WqbBmR0PCHuLjakMtjUeHRTAgMBAAECgYAoyGFzazHCZ6IX5WCnIuXwnta+MUuVdbmcPaAs/qQl6PKAEnQygOj+FBrXRBq1pNUwdVhfCGziJd7EAktnoYONp2X7bg5mv3I1KXWHIo6j5sVJNJeQcozHjHmNFH/6d89z7Au2/dMTdNdzPvclkrAJVIHF2Fhrnq+0mwxbeIOmuQJBAP7733aZbFcqdglLax33gm4udTL5qDL9U+t8YAgnqEknV/0S41/uFDy2+55apl1Bfgwbxagwfh5F7zhLac8Y/X8CQQCfxoaP11wW0cjBZXlVx0YyEb7ZAhOjAseBriEtyV/GnSv7sx05meLdX5yMR3MCzA/Ke63W+Gth3aaCQBOwaJstAkA57K4PGztpcdPuuJtU621N6m/lbKGxM6tky5RlRd0NAUsTyr6shEFEqazvhFhWxxIDXivA0YNVDT4S487dpSexAkBDKvNgvvmAwlpyKxWSIP76h4lCs2PL8JwsgSaDmBeCaIqX+pl7oJzF0a7/Lf5Em8K7rbjMGY0TDFo2gXeFfsRVAkEA++w7K8r3Xcuj5uOYgmH4xA3gI/P40A6F9vD4cqj5Kl+Zqdtk2/y+njs+O35f7tw3J1t+My9RVjUUdIyXvTOWbw==";
        String charset = "UTF-8";

        //获取请求内容
        this.headers = inputMessage.getHeaders();
        String content = IOUtils.toString(inputMessage.getBody(), charset);
        // 验证参数
        Map<String, Object> contentMap = JSONObject.parseObject(content, Map.class);
        Object signObj = contentMap.remove("sign");
        // 验签名
        String errorMsg = null;
        if (RSATools.verify(RSATools.sortMap(contentMap).toString().getBytes(StandardCharsets.UTF_8), publicKey, signObj.toString())) {
            String encryptData = (String) contentMap.get("data");
            if (StringUtils.isNotBlank(encryptData)) {
                // 解密
                String data = new String(RSATools.decryptByPublicKey(Base64.decodeBase64(encryptData), publicKey), StandardCharsets.UTF_8);
                contentMap.put("data", data);
                // 验证token
                Map<String, Object> dataMap = JSONObject.parseObject(data, Map.class);
                String token = (String) dataMap.get("token");
                if (cacheRefreshToken.equals(token)) {
                	logger.info("解密请求参数:{}",data);
                    this.body = IOUtils.toInputStream(JSONObject.toJSONString(contentMap), charset);
                } else {
                    errorMsg = "token不正确！";
                }
            } else {
                errorMsg = "缺少请求token！";
            }
        } else {
            errorMsg = "参数验证失败！";
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            throw new Exception(errorMsg);
        }
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

}
