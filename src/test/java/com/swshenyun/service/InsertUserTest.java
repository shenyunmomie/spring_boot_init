package com.swshenyun.service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import cn.hutool.core.date.StopWatch;
import com.swshenyun.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InsertUserTest {

    @Autowired
    private UserService userService;

    @Test
    public void testInsertUser() {
        // 计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 十万条
        int total = 100000;
        int tmp = 30000;
        List<User> list = new ArrayList<>();
        for (int i = tmp; i < total+tmp; i++) {
            User user = new User();
            user.setUsername("fakeUser"+i);
            user.setPassword("12345678sw");
            user.setPhone("18732566535");
            user.setEmail("18731548870@163.com");
            user.setSex(0);
            user.setAvatar("https://shenyunmomie.oss-cn-beijing.aliyuncs.com/imags/202307051931160.png");
            user.setProfile("假人");
            user.setStatus(0);
            user.setTags("java,python,测试,后端,前端");
            list.add(user);
        }
        userService.saveBatch(list);
        stopWatch.stop();
        System.out.println("插入1w条假人数据耗时: " + stopWatch.getTotalTimeMillis() + " ms");
    }

    @Test
    public void testMultipleInsertUser() {
        // 计时器
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 50万条
        int total = 100000;
        int tmp = 600000;
        int batch = 50;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int j = 0; j < batch; j++) {
            List<User> list = new ArrayList<>();
            for (int i = tmp; i < total/batch+tmp; i++) {
                User user = new User();
                user.setUsername("fakeUser"+i);
                user.setPassword("12345678sw");
                user.setPhone("18732566535");
                user.setEmail("18731548870@163.com");
                user.setSex(0);
                user.setAvatar("https://shenyunmomie.oss-cn-beijing.aliyuncs.com/imags/202307051931160.png");
                user.setProfile("假人");
                user.setStatus(0);
                user.setTags("JavaScript");
                list.add(user);
            }
            tmp =  tmp + total/batch;
            ExecutorService executorService = new ThreadPoolExecutor(40, 1000,
                    10000, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10000));
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(list);
            }, executorService);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println("插入10w条假人数据耗时: " + stopWatch.getTotalTimeMillis() + " ms");
    }
}
