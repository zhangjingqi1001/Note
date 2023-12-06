package com.zhangjingqi.common;

import lombok.Getter;

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
    MESSAGE_LOGIN_FAIL("2");

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
