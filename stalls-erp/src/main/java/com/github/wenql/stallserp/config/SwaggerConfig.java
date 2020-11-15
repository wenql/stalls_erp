package com.github.wenql.stallserp.config;

import com.github.wenql.stallserp.common.config.BaseSwaggerConfig;
import com.github.wenql.stallserp.common.domain.SwaggerProperties;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.github.wenql.stallserp.modules")
                .title("stalls-erp项目骨架")
                .description("stalls-erp项目骨架相关接口文档")
                .contactName("wenql")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
