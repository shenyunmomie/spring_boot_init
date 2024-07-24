package com.swshenyun.controller;

import com.swshenyun.common.BaseResponse;
import com.swshenyun.common.ErrorCode;
import com.swshenyun.constant.JwtClaimsConstant;
import com.swshenyun.context.BaseContext;
import com.swshenyun.exception.BaseException;
import com.swshenyun.pojo.dto.UserDTO;
import com.swshenyun.pojo.dto.UserLoginDTO;
import com.swshenyun.pojo.dto.UserRegisterDTO;
import com.swshenyun.pojo.entity.User;
import com.swshenyun.pojo.vo.UserLoginVO;
import com.swshenyun.properties.JwtProperties;
import com.swshenyun.service.UserService;
import com.swshenyun.utils.JwtUtils;
import com.swshenyun.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> login(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        //1.登录service
        User user = userService.login(userLoginDTO);
        //2.生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        claims.put(JwtClaimsConstant.USERNAME,user.getUsername());
        String token = JwtUtils.createJwt(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );

        //3.返回
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .build();

        return ResultUtils.success(userLoginVO);
    }

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {

        long result = userService.register(userRegisterDTO);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser() {
        log.info("获取当前用户数据");
        long userId = BaseContext.getCurrentId();

        User user = userService.getById(userId);
        User safetyUser = userService.getSafeUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse logout() { return ResultUtils.success(); }

    /**
     * 修改员工账户状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public BaseResponse startOrStop(@PathVariable Integer status,Long id) {
        log.info("启用禁用员工账户：{}，{}", status, id);
        userService.startOrStop(status,id);
        return ResultUtils.success();
    }

    /**
     * 修改员工信息
     * @return
     */
    @PutMapping
    public BaseResponse update(@RequestBody UserDTO userDTO) {
        log.info("编辑员工信息：{}", userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        //验证修改权限
        Long currentId = BaseContext.getCurrentId();
        if (!currentId.equals(userDTO.getId())) {
            throw new BaseException(ErrorCode.NO_AUTH_ERROR);
        }
        userService.updateById(user);
        return ResultUtils.success();
    }

    /**
     * 按标签查询用户
     * @param tagNameList
     * @return
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam("tagNameList") List<String> tagNameList) {
        log.info("按标签查询员工：{}", tagNameList);
        if (tagNameList.isEmpty()) {
            throw new BaseException(ErrorCode.PARAMS_NULL_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

}
