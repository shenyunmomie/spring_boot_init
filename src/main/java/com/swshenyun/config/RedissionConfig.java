package com.swshenyun.config;


import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedissionConfig {

    private String host;

    private String port;

    @Bean
    public RedissonClient redissonClient() {
        // 1. Create config object
        Config config = new Config();
        String redisAddress = String.format("redis://localhost:6379");
        config.useClusterServers()
                .addNodeAddress(redisAddress)
                .setPassword("root");
        // 2. Create Redisson instance
        return Redisson.create(config);
    }
}