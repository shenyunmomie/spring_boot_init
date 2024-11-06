package com.swshenyun.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NotBlank
public class UserLoginDTO implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 20, message = "用户名长度必须在4到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, message = "密码长度不能少于8位")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "密码必须包含大小写字母")
    @Pattern(regexp = ".*\\d.*", message = "密码必须包含数字")
    private String password;
}
