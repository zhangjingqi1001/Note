package com.hmdp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScrollResult {
//  查询到的元素
    private List<?> list;
//  最小时间
    private Long minTime;
//  偏移量
    private Integer offset;
}
