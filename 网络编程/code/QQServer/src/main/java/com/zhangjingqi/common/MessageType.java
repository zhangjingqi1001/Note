package com.zhangjingqi.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 消息类型
 * 不同行亮的值表示不同的消息类型
 */
@Getter
public enum MessageType {
    /**
     * 登录成功
     */
    MESSAGE_LOGIN_SUCCEED("1"),
    /**
     * 登录失败
     */
    MESSAGE_LOGIN_FAIL("2"),
    /**
     * 普通信息对象
     */
    MESSAGE_COMM_MES("3"),

    /**
     * 获取在线用户
     * 要求服务器返回在线用户列表
     */
    MESSAGE_GET_ONLINE_FRIEND("4"),

    /**
     * 服务器返回在线用户列表
     */
    MESSAGE_RETTURN_ONLINE_FRIEND("5"),

    /**
     * 客户端请求退出
     */
    MESSAGE_CLIENT_EXIT("6"),
    /**
     * 群发消息
     */
    MESSAGE_TO_ALL_EXIT("7"),
    ;

    private final String code;


    MessageType(String code) {
        this.code = code;
    }


    public static String find(Integer code) {
        for (MessageType value : MessageType.values()) {
            if (code.toString().equals(value.getCode())) {
                return value.getCode();
            }
        }
        return null;
    }
}
