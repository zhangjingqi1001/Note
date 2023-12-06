package com.zhangjingqi.qqserver.service;

import lombok.Data;

import java.util.HashMap;

/**
 * 该类用于管理和客户端通信的线程
 */
@Data
public class ManagerServerConnectServerThread {
    private static HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    /**
     *添加线程对象到hm集合
     */
    public static void addClientThread(String userId, ServerConnectClientThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    /**
     *从集合中获取对应线程对象
     */
    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }
}
