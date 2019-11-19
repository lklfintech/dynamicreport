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
package com.lakala.dynamicreport.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lakala.dynamicreport.common.repository.specification.OperationEnum;
import com.lakala.dynamicreport.common.repository.specification.Predication;
import com.lakala.dynamicreport.common.repository.specification.SpecificationUtil;
import com.lakala.dynamicreport.common.constants.HttpConstants;
import com.lakala.dynamicreport.common.exception.BusinessException;
import com.lakala.dynamicreport.common.model.PageForList;
import com.lakala.dynamicreport.common.servicei.impl.BaseServiceImpl;
import com.lakala.dynamicreport.common.utils.RSATools;
import com.lakala.dynamicreport.system.model.CacheService;
import com.lakala.dynamicreport.system.model.ServerList;
import com.lakala.dynamicreport.system.query.CacheServiceQuery;
import com.lakala.dynamicreport.system.repository.CacheServiceRepository;
import com.lakala.dynamicreport.system.service.ICacheServiceService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色服务接口实现
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Service(value = "CacheServiceService")
public class CacheServiceServiceImpl extends BaseServiceImpl<CacheServiceRepository,CacheService,Long> implements ICacheServiceService{

    private final Logger log = LoggerFactory.getLogger(CacheServiceServiceImpl.class);

    @Autowired
    private CacheServiceRepository cacheServiceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<CacheService> findCacheService(CacheServiceQuery CacheServiceQuery) {
        return cacheServiceRepository.findAll(specification(CacheServiceQuery));
    }

    @Override
    public Page<CacheService> findCacheServiceCriteria(final CacheServiceQuery cacheServiceQuery) {
        return cacheServiceRepository.findAll(specification(cacheServiceQuery), PageForList.getPageable("id",cacheServiceQuery));
    }

    /**
     * 拼凑查询条件
     */
    private Specification<CacheService> specification(final CacheServiceQuery cacheServiceQuery) {
        List<Predication> list = Lists.newArrayList();
        if (null != cacheServiceQuery) {
            list.add(Predication.get(OperationEnum.LIKE,"serverList.ip",cacheServiceQuery.getSearchText(),String.class,OperationEnum.AND));
        }
        return SpecificationUtil.where(list);
    }

    @Override
    public void refresh(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                if (StringUtils.isNotBlank(id)) {
                    StringBuilder strBul = new StringBuilder();
                    CacheService cacheService = cacheServiceRepository.getOne(Long.valueOf(id));
                    ServerList serverList = cacheService.getServerList();
                    strBul.append(HttpConstants.HTTP).append(HttpConstants.SIGN_THREE).append(HttpConstants.SIGN_ONE).append(serverList.getIp()).append(":").append(serverList.getPort());
                    if (!StringUtils.isBlank(cacheService.getContextPath())) {
                        strBul.append(cacheService.getContextPath()).append(HttpConstants.SIGN_TWO);
                    }
                    strBul.append(cacheService.getServicePath());
                    String url = strBul.toString();
                    try {
                        if (HttpConstants.POST.equalsIgnoreCase(cacheService.getHttpmethod())) {
                            String cachePrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKJCAqMWCOn5M84ldKFYThMkEPaRVpumgfbqssgDwlExAEPnyyFrxgYEIvC6wVPm1Azqi+QfC6eqYV2irtf+Fa4MYFoJSQZwdoHK9jKK24jsuZHPNd+aDrsqMLju5kZd9UKUjOdYAB3okUoykS2NhJFAXqZcEFdYDh9ipXIMc/hzAgMBAAECgYBO0BNp1fUkeOhB9I2PJU5fG8Clk3jnB9zO67dFUhYby9peIYPYSvoXyACUv5HSSCet0mt6td26Zhb/HVsiDQfICQ5qIwrtMqozUAo8+TdLYNFppratw3x9AW4R6P61uu+lpOfJfqikNpxpjX1FFRKJOj1IMhQG+ZXEqNhlyVuCeQJBAN1RSx2n7K1Xf/cHvdG8tMvTNpqYC8SobJ2Q5RTdTCQncNbbXkiPqUDFoeS0dHrmR7UtKxumo0cFYnDq/Jw80TUCQQC7r2O0BtBDCqEERn3ERcFJB+tv7nHCrQgw1znW5MpxebpRfk4JZg72dQD/8/nIoJGcD610aRVvC/wsXrPtKUAHAkBqJF3ZKDsrW0ak7BKOb2lqQ67xb7BdLcWBwAcN/aBRypSkhurt8/BhfydIr38Zni2P58y/4yh1nFB9BUYHjGdtAkBw0c5KMmpQUnxV2JXTPOciIluZTikeD+tDiKzhC2jR641fXWBFcA8AgXbXZO46dqLwv8/RkTVsKjUs6sSXhFtTAkBOhC05wZK8uqeJ5VdJFXr4xMPsJSXXRWxcgHWInTBGVFTUZsJeRyzPuBfgS4EO8GtuBf7Dd9SSN0B/Oh87ZxAd";
                            String cacheRefreshToken = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ8kLKCbMB1otZAkOZBi+LjQQ/9zrES71gVx+bq/+63zSzzoquo8M2H7KrcrfKKZ9pmILNGwlakwgDBOCIjkkOurhvMcjhduC64ahvQnQRmV+ujxywDuujietMOMI7Z27bZXTX6iT9idig8FCR318WqbBmR0PCHuLjakMtjUeHRTAgMBAAECgYAoyGFzazHCZ6IX5WCnIuXwnta+MUuVdbmcPaAs/qQl6PKAEnQygOj+FBrXRBq1pNUwdVhfCGziJd7EAktnoYONp2X7bg5mv3I1KXWHIo6j5sVJNJeQcozHjHmNFH/6d89z7Au2/dMTdNdzPvclkrAJVIHF2Fhrnq+0mwxbeIOmuQJBAP7733aZbFcqdglLax33gm4udTL5qDL9U+t8YAgnqEknV/0S41/uFDy2+55apl1Bfgwbxagwfh5F7zhLac8Y/X8CQQCfxoaP11wW0cjBZXlVx0YyEb7ZAhOjAseBriEtyV/GnSv7sx05meLdX5yMR3MCzA/Ke63W+Gth3aaCQBOwaJstAkA57K4PGztpcdPuuJtU621N6m/lbKGxM6tky5RlRd0NAUsTyr6shEFEqazvhFhWxxIDXivA0YNVDT4S487dpSexAkBDKvNgvvmAwlpyKxWSIP76h4lCs2PL8JwsgSaDmBeCaIqX+pl7oJzF0a7/Lf5Em8K7rbjMGY0TDFo2gXeFfsRVAkEA++w7K8r3Xcuj5uOYgmH4xA3gI/P40A6F9vD4cqj5Kl+Zqdtk2/y+njs+O35f7tw3J1t+My9RVjUUdIyXvTOWbw==";
                            JSONObject requestData = new JSONObject();
                            requestData.put("token", cacheRefreshToken);
                            String encryptRequestData = Base64.encodeBase64String(RSATools.encryptByPrivateKey(requestData.toJSONString().getBytes(StandardCharsets.UTF_8), cachePrivateKey));
                            Map<String, Object> requestMap = Maps.newHashMap();
                            requestMap.put("data", encryptRequestData);
                            requestData.put("timestamps", System.currentTimeMillis());
                            JSONObject requestMapJson = RSATools.sortMap(requestMap);
                            String sign = RSATools.sign(requestMapJson.toString().getBytes(StandardCharsets.UTF_8), cachePrivateKey);
                            requestMapJson.put("sign", sign);
                            restTemplate.postForEntity(url, requestMapJson, String.class);
                        } else {
                            restTemplate.getForEntity(url, String.class);
                        }
                    } catch (Exception e) {
                        log.error("[CacheServiceServiceImpl : refresh]{}{}", ids, e);
                        throw new BusinessException("刷新缓存失败" + e);
                    }

                }
            }
        }
    }

}
