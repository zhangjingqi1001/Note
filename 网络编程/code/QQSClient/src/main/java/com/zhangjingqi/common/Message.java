package com.zhangjingqi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装消息
 * 表示客户端和服务端通信时的消息对象
 * 发送消息流程：客户端A -》 服务端 -》 客户端B ，假如服务器瘫痪，聊天便不可以使用
 * （如果客户端A与客户端B在同一个局域网 客户端A -》客户端B）
 */
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
