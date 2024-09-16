package com.swshenyun.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.swshenyun.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 神殒魔灭
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-06-07 11:20:43
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> orderedQueruByIds(List<Long> userIdList);
}




