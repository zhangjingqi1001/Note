package com.zhangjingqi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUser implements Serializable {

    private static final long serialVersionUID = -5352627792860514242L;

    private Integer userId;

    private String username;

    private String password;

    private String sex;

    private String address;

    private Integer enabled;

    private Integer accountNoExpired;

    private Integer credentialsNoExpired;

    private Integer accountNoLocked;

}
