package com.zhangjingqi.pojo;

public class User {
    private Integer id; //id（主键）
    private String name; //姓名
    private Short age; //年龄
    private Short gender; //性别
    private String phone; //手机号
//省略GET, SET方法

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                '}';
    }
}
