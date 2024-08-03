package com.swshenyun.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class UserByTagsPageDTO extends PageDTO implements Serializable {

    /**
     * 标签列表
     */
    private List<String> tagNameList;

}
