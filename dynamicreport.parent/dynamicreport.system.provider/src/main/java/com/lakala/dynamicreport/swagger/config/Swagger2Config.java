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
package com.lakala.dynamicreport.swagger.config;

import com.xzixi.swagger2.plus.annotation.EnableSwagger2Plus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * <p>
 * swagger配置
 * </p>
 *
 * @author lkl金融
 * @since 2019-09-16
 */
@Configuration
@EnableSwagger2Plus
public class Swagger2Config implements WebMvcConfigurer{

    @Value("${swagger.contact_name}")
    private String CONTACT_NAME;
    @Value("${swagger.contact_url}")
    private String CONTACT_URL;
    @Value("${swagger.contact_email}")
    private String CONTACT_EMAIL;
    @Value("${swagger.enable}")
    private boolean SWAGGER_ENABLE;

    /**
     * 系统配置管理
     */
    @Value("${swagger.system_group_name}")
    private String system_group_name;
    @Value("${swagger.system_description}")
    private String system_description;
    @Value("${swagger.system_version}")
    private String system_version;
    @Value("${swagger.system_scan_base_package}")
    private String system_scan_base_package;

    /**
     * 数据模型配置管理
     */
    @Value("${swagger.datamodel_group_name}")
    private String datamodel_group_name;
    @Value("${swagger.datamodel_description}")
    private String datamodel_description;
    @Value("${swagger.datamodel_version}")
    private String datamodel_version;
    @Value("${swagger.datamodel_scan_base_package}")
    private String datamodel_scan_base_package;

    /**
     * 报表配置管理
     */
    @Value("${swagger.dynamicreport_group_name}")
    private String dynamicreport_group_name;
    @Value("${swagger.dynamicreport_description}")
    private String dynamicreport_description;
    @Value("${swagger.dynamicreport_version}")
    private String dynamicreport_version;
    @Value("${swagger.dynamicreport_scan_base_package}")
    private String dynamicreport_scan_base_package;


    /**
     * api信息
     *
     * @param name        标题
     * @param description 描述
     * @param version     版本
     * @return
     */
    private ApiInfo apiInfo(String name, String description, String version) {
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .version(version)
                .contact(new Contact(CONTACT_NAME,CONTACT_URL,CONTACT_EMAIL))
                .build();
    }

    /**
     * 系统配置API
     *
     * @return
     */
    @Bean
    public Docket system_api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(system_group_name,system_description,system_version))
                .enable(SWAGGER_ENABLE)
                .select()
                .apis(RequestHandlerSelectors.basePackage(system_scan_base_package))
                .paths(PathSelectors.any())
                .build()
                .groupName(system_group_name);
    }

    /**
     * 数据模型配置API
     *
     * @return
     */
    @Bean
    public Docket dataModel_api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(datamodel_group_name,datamodel_description,datamodel_version))
                .enable(SWAGGER_ENABLE)
                .select()
                .apis(RequestHandlerSelectors.basePackage(datamodel_scan_base_package))
                .paths(PathSelectors.any())
                .build()
                .groupName(datamodel_group_name);
    }

    /**
     * 报表配置API
     *
     * @return
     */
    @Bean
    public Docket dynamicreport_api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(dynamicreport_group_name,dynamicreport_description,dynamicreport_version))
                .enable(SWAGGER_ENABLE)
                .select()
                .apis(RequestHandlerSelectors.basePackage(dynamicreport_scan_base_package))
                .paths(PathSelectors.any())
                .build()
                .groupName(dynamicreport_group_name);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
