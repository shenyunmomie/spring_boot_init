package com.swshenyun.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserByNamePageDTO extends PageDTO implements Serializable {

    private String username;
}
