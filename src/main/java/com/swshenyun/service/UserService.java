package com.swshenyun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.swshenyun.pojo.dto.UserLoginDTO;
import com.swshenyun.pojo.dto.UserRegisterDTO;
import com.swshenyun.pojo.entity.User;

/**
* @author 神殒魔灭
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-06-07 11:20:43
*/
public interface UserService extends IService<User> {

    User login(UserLoginDTO userLoginDTO);

    Long register(UserRegisterDTO userRegisterDTO);

    User getSafeUser(User user);

    void startOrStop(Integer status, Long id);
}
