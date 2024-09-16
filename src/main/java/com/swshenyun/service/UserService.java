package com.swshenyun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.swshenyun.pojo.dto.*;
import com.swshenyun.pojo.entity.User;
import com.swshenyun.pojo.vo.UserVO;

import java.util.List;

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

    Page<User> searchUsersByTags(UserByTagsPageDTO userByTagsPageDTO);

    Page<User> searchUsersByName(UserByNamePageDTO userByNamePageDTO);

    Page<User> getRecommendUsers(PageDTO pageDTO);

    Boolean isAdmin(Long id);

    List<UserVO> matchUser(long num);
}
