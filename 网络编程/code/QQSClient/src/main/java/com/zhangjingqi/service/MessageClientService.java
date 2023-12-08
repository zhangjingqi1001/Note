package com.zhangjingqi.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 该类提供和消息相关的服务方法
 */
public class MessageClientService {
    /**
     * @param content  内容
     * @param senderId 发送用户id
     * @param getterId 接收用户id
     */
    public void sendMessageToOne(String content, String senderId, String getterId) {
        //封装消息
        Message message = new Message();
        message.setContent(content);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_COMM_MES.getCode());//普通消息
        System.out.println("用户"+senderId+"和用户"+getterId+"说:"+content);
        //获取senderId对应的socket
        ClientConnectServerThread clientConnectServerThread = ManagerClientConnectServerThread.getClientConnectServerThread(senderId);
        Socket socket = clientConnectServerThread.getSocket();

        //输出消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 群发消息
     * @param userId 发送消息的用户id
     * @param content 需要发送的内容
     */
    public void sendMessageToOnlineUser(String userId, String content) {
        Message message = new Message();
        message.setContent(content);
        message.setSender(userId);
        message.setSendTime(new Date().toString());
        message.setMesType(MessageType.MESSAGE_TO_ALL_EXIT.getCode());//普通消息
        System.out.println("用户"+userId+"群发消息说:"+content);

        ClientConnectServerThread clientConnectServerThread = ManagerClientConnectServerThread.getClientConnectServerThread(userId);
        Socket socket = clientConnectServerThread.getSocket();

        //输出消息
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
