package com.github.wenql.stallserp.config;

import com.github.wenql.stallserp.common.config.BaseRedisConfig;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
