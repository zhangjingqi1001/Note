package com.zhangjingqi.qqserver.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;
import com.zhangjingqi.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 发送新闻
 */
public class SendNewsAllService implements Runnable {


    @Override
    public void run() {
        //多次推送新闻，使用while循环
        while (true) {
            System.out.println("请输入服务器要推送的信息/消息【输入exit表示退出】");
            String content = Utility.readString(500);
            if ("exit".equals(content)) {
                break;
            }
            //构建消息类型
            Message message = new Message();
            message.setSender("服务器");
            message.setMesType(MessageType.MESSAGE_TO_ALL_EXIT.getCode());
            message.setContent(content);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人 说：" + content);

            //遍历当前所有的通信线程得到socket
            HashMap<String, ServerConnectClientThread> hm = ManagerServerConnectServerThread.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                ServerConnectClientThread serverConnectClientThread = hm.get(next);
                try {
                    //给每个用户发送消息
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
