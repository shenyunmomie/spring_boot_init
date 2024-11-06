package com.swshenyun.pojo.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


@Data
public class UserDTO {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别 0 女 1 男
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String profile;

    /**
     * 用户标签
     */
    private String tags;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
