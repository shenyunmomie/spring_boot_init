package com.swshenyun.config;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissionConfigTest {

    @Test
    void testRedission() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setPassword("root");
        RedissonClient redissonClient = Redisson.create(config);
        redissonClient.getConfig();
    }

}
