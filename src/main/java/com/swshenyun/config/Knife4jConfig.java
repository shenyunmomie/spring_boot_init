package com.swshenyun.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    /**
     * 通过knife4j生成接口文档
     *
     * @return
     */
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("knife4j测试的接口文档")
                        .description("这是基于knife4j-openapi3-jakarta-spring-boot-starter的接口文档")
                        .version("1.0")
                        .contact(new Contact()
                                .name("lbw")
                                .email("18731548870@163.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("springboot基础框架")
                        .url("http://localhost:8080"));
    }

}