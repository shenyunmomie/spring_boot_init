package com.swshenyun.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NotBlank
public class TeamCreateDTO implements Serializable {

    /**
     * 队伍名称
     */
    @NotBlank
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */

    private Integer maxNum;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    @NotBlank
    private Integer status;

    /**
     * 密码
     */
    private String password;
}
