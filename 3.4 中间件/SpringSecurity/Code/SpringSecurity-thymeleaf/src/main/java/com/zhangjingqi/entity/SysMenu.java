package com.zhangjingqi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 597868207552115176L;

    private Integer id;
    private Integer pid;
    private Integer type;
    private String name;
    private String code;
}
