package com.zhangjingqi.service;

import java.util.HashMap;

/**
 * 管理客户端连接到服务端线程的一个类
 */
public class ManagerClientConnectServerThread {
    //把多个线程放入一个HashMap中进行管理，key是用户id，value是客户端与服务端通信的线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    //将某个线程加入到集合中
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
