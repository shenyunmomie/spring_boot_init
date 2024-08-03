package com.swshenyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.swshenyun.common.ErrorCode;
import com.swshenyun.constant.StatusConstant;
import com.swshenyun.exception.BaseException;
import com.swshenyun.mapper.UserMapper;
import com.swshenyun.pojo.dto.*;
import com.swshenyun.pojo.entity.User;
import com.swshenyun.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 神殒魔灭
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-06-07 11:20:43
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 密码盐值
     */
    private static final String SALT = "symm";
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();

        String password = DigestUtils.md5DigestAsHex((SALT+userLoginDTO.getPassword()).getBytes());

        //1.验证数据库是否存在账户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BaseException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (!password.equals(user.getPassword())) {
            throw new BaseException(ErrorCode.PASSWORD_ERROR);
        }
        //判断账户status
        if (user.getStatus().equals(StatusConstant.DISABLE)) {
            throw new BaseException(ErrorCode.ACCOUNT_LOCKED);
        }

        //2.返回脱敏账户数据
        return getSafeUser(user);
    }

    /**
     * 账户脱敏
     * @param user
     * @return
     */
    public User getSafeUser(User user) {
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUnionId(user.getUnionId());
        safeUser.setOpenId(user.getOpenId());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setSex(user.getSex());
        safeUser.setAvatar(user.getAvatar());
        safeUser.setProfile(user.getProfile());
        safeUser.setStatus(user.getStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setUpdateTime(user.getUpdateTime());
        safeUser.setTags(user.getTags());
        return safeUser;
    }

    /**
     * 用户注册
     * @return id 用户
     */
    public Long register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        // 1.校验
        if (!password.equals(checkPassword)) {
            throw new BaseException(ErrorCode.PASSWORD_ERROR);
        }

        //username不能重复
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        long count = this.count(wrapper);
        if (count>0) {
            throw new BaseException(ErrorCode.ACCOUNT_EXISTS);
        }

        // 2.md5加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + password).getBytes());

        // 3.插入
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5Password);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BaseException(ErrorCode.REGISTER_ERROR);
        }
        return user.getId();
    }

    /**
     * 启用禁用账户
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        User user = User.builder()
                .id(id)
                .status(status)
                .build();

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, user.getId());
        boolean update = this.update(wrapper);
        if (!update) {
            throw new BaseException(ErrorCode.OPERATION_ERROR);
        }
    }

    /**
     * 按标签查询用户
     * @param
     * @return
     */
    public Page<User> searchUsersByTags(UserByTagsPageDTO userByTagsPageDTO) {
        //1.校验
        if (userByTagsPageDTO == null) {
            throw new BaseException(ErrorCode.PARAMS_NULL_ERROR);
        }
        List<String> tagNameList = userByTagsPageDTO.getTagNameList();
        //2.组合wrapper
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        tagNameList.forEach(tagName -> {
            wrapper.like(User::getTags, tagName);
        });
        //3.查询
        Page<User> page = this.page(new Page<>(userByTagsPageDTO.getPage(), userByTagsPageDTO.getPageSize()), wrapper);

        //4.脱敏
        return page.setRecords(page.getRecords().stream().map(this::getSafeUser).collect(Collectors.toList()));
    }

    /**
     * username条件查询，分页
     */
    public Page<User> searchUsersByName(UserByNamePageDTO userByNamePageDTO) {
        String username = userByNamePageDTO.getUsername();
        int page = userByNamePageDTO.getPage();
        int pageSize = userByNamePageDTO.getPageSize();
        //1.校验
        if (username == null || username.isEmpty()) {
            throw new BaseException(ErrorCode.PARAMS_NULL_ERROR);
        }
        //2.组合wrapper
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getUsername, username);
        //3.查询
        Page<User> userPage = this.page(new Page<>(page,pageSize), wrapper);

        //4.返回
        return userPage.setRecords(userPage.getRecords().stream().map(this::getSafeUser).collect(Collectors.toList()));
    }


    public Page<User> getRecommendUsers(PageDTO pageDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        return this.page(new Page<>(pageDTO.getPage(), pageDTO.getPageSize()), wrapper);
    }
}




