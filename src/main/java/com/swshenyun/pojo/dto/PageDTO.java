package com.swshenyun.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class PageDTO implements Serializable {

    private Integer page;

    private Integer pageSize;
}
