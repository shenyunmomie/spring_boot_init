package com.swshenyun.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.swshenyun.context.BaseContext;
import com.swshenyun.pojo.entity.User;
import com.swshenyun.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PreCacheTask {

    @Autowired
    private UserService userService;

//    @Autowired
//    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    // 重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    /**
     * 预加载缓存(分布式)
     */
//    @Scheduled(cron = "0 31 0 * * *")
//    public void recommendUsersCache() {
//        // 分布式锁，限制执行一次
//        // 预热重点用户
//        // 预加载缓存
//        //1.创建锁对象
//        RLock lock = redissonClient.getLock("symm:preCacheTask:docache:lock");
//        try {
//            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
//                System.out.println("getLock: " + Thread.currentThread().getId());
//                for (Long userId : mainUserList) {
//                    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//                    Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
//                    String redisKey = String.format("symm:user:recommend:%s", userId);
//                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//                    // 写缓存
//                    try {
//                        valueOperations.set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
//                    } catch (Exception e) {
//                        log.error("redis set key error", e);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("redis set key error", e);
//        } finally {
//            // 释放自己的锁
//            if (lock.isHeldByCurrentThread()) {
//                System.out.println("unLock: " + Thread.currentThread().getId());
//                lock.unlock();
//            }
//        }
//    }
}
