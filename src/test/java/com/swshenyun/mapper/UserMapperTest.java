package com.swshenyun.mapper;

import com.swshenyun.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testOrderedQueruByIds() {
        List<Long> userIdList = Arrays.asList(2L,3L,1L);
        List<User> userList = userMapper.orderedQueruByIds(userIdList);
        System.out.println(userList);
    }
}