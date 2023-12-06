package com.zhangjingqi.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = -3567747187962510012L;

    /**
     * 消息类型:发送文件、纯文本、视频聊天....
     */
    private String mesType;

    /*
     *发送者
     */
    private String sender;

    /**
     *接收者
     */
    private String getter;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String sendTime;

}