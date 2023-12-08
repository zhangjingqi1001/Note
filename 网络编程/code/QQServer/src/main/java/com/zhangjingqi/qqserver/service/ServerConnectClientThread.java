package com.zhangjingqi.qqserver.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;
import com.zhangjingqi.common.User;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 该类对应的对象和某个客户端保持通信
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerConnectClientThread extends Thread {

    /**
     * 可以区分此socket是和哪个用户进行关联的
     */
    private String userId;//连接到服务端的这个用户id

    private Socket socket;

    /**
     * 线程处于run状态，可以发送或者接收客户端的消息
     */
    @Override
    public void run() {
        //不断的从socket中读数据和写数据
        while (true) {
            System.out.println("服务端和客户端保持通信，读取数据.... userId:" + userId);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                //读取数据
                Message message = (Message) ois.readObject();

                //根据Message的类型，判断客户端想要执行什么操作
                if (MessageType.MESSAGE_GET_ONLINE_FRIEND.getCode().equals(message.getMesType())) {
                    System.out.println("用户" + userId + "获取在线用户");
                    //拉取在线用户（客户端要拉取在线用户列表）
                    Socket socket = ManagerServerConnectServerThread.getClientThread(userId).getSocket();

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    //构建Message发送给服务端
                    Message returnMessage = new Message();
                    returnMessage.setMesType(MessageType.MESSAGE_RETTURN_ONLINE_FRIEND.getCode());
                    returnMessage.setContent(ManagerServerConnectServerThread.getOnlineUser());
                    //说明要发送给谁
                    returnMessage.setGetter(message.getSender());
                    //返回给客户端
                    oos.writeObject(returnMessage);
                    oos.flush();
                } else if (MessageType.MESSAGE_CLIENT_EXIT.getCode().equals(message.getMesType())) {
                    //说明客户端想要退出，服务端要将socket关闭并退出线程就可以了
                    //将客户端对应的线程从集合中删除
                    ManagerServerConnectServerThread.remove(userId);
                    //关闭socket
                    socket.close();
                    System.out.println("用户" + userId + "退出系统");
                    //退出循环
                    return;
                } else if (MessageType.MESSAGE_COMM_MES.getCode().equals(message.getMesType())) {
                    //转发给指定客户端，假如说客户不在线的话，可以保存到数据库，这样就可以实现离线留言
                    Socket socket = ManagerServerConnectServerThread.getClientThread(message.getGetter()).getSocket();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                    oos.writeObject(message);
                    oos.flush();

                } else if (MessageType.MESSAGE_TO_ALL_EXIT.getCode().equals(message.getMesType())) {
                    //群发消息
                    //遍历线程集合取出所有线程对应的socket发送消息即可
                    HashMap<String, ServerConnectClientThread> hm = ManagerServerConnectServerThread.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线人的id
                        String onlineId = iterator.next();
                        if (!onlineId.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(
                                    hm.get(onlineId).getSocket().getOutputStream()
                            );
                            oos.writeObject(message);
                            oos.flush();
                        }

                    }

                } else {
                    System.out.println("其他类型暂时不处理");
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            //如果服务器端没有发送消息过来，这个地方会堵塞，此线程会一直等待
            //读取客户端发送的User对象

        }
    }
}
