package com.zhangjingqi.qqserver.service;

import lombok.Data;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 该类用于管理和客户端通信的线程
 */
@Data
public class ManagerServerConnectServerThread {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();


    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    /**
     * 添加线程对象到hm集合
     */
    public static void addClientThread(String userId, ServerConnectClientThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    /**
     * 从集合中获取对应线程对象
     */
    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }

    /**
     * 获取在线用户
     */
    public static String getOnlineUser() {
        //集合遍历，遍历hashMap的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList = "";

        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

    /**
     * 从集合中删除掉某个线程对象
     */
    public static void remove(String userId) {
       hm.remove(userId);
    }

}
