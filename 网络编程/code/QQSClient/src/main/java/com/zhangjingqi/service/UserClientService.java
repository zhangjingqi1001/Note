package com.zhangjingqi.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;
import com.zhangjingqi.common.User;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 完成用户登录验证和用户注册等功能
 */
@Data
public class UserClientService {

    //其他地方也会使用user信息，所以将其作为一个属性
    private User user = new User();

    private Socket socket = null;

    /**
     *根据userId和pwd到服务器验证该用户是否合法
     */
    public boolean checkUser(String userId, String pwd) {
        //临时变量b，用户是否合法的标志
        boolean b = false;

        //TODO 创建User对象
        user.setUserId(userId);
        user.setPasswd(pwd);

        try {
            //TODO 连接到服务端，发送User对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            //得到ObjectOutputStream对象流(序列化流，也是字节流中一种)
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            oos.flush();

            //TODO 读取从服务器回复的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message msg = (Message) ois.readObject();

            if (MessageType.find(1).equals(msg.getMesType())) {

                //登录成功
                //一旦登录成功，我们需要启动一个线程维护或者持有此socket，保持此线程可以跟我们服务器端一直进行通信
                //不启动线程的话此Socket不好维护。如果我们有数据发送或者接收，我们可以从这个线程里面进行拉取
                //为什么将Socket放入一个线程中管理？
                // 1.如果不创建这个线程的话，一个客户端会有多个socket，socket管理起来就比较麻烦
                // 2.需要socket不断的从数据通道中读写数据，所以也必须做成一个线程
                ClientConnectServerThread ccst = new ClientConnectServerThread(socket);
                //启动客户端的线程
                ccst.start();
                //为了后面客户端的扩展，我们将线程放入到集合中管理
                ManagerClientConnectServerThread.addClientConnectServerThread(userId, ccst);

                b = true;
            } else {
                //登录失败
                //我们是有Socket的，但是没有线程，即登录失败，不能启动和服务器通信的线程
                //关闭socket
                socket.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return b;
    }

    /**
     * 向服务器端请求在线用户列表
     */
    public void onlineFriendList(){
        //发送一个message，并且消息的类型是MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND.getCode());
        message.setSender(user.getUserId());
        //发送给服务器
        //得到当前线程的Socket对应的ObjectOutputStream
        //clientConnectServerThread线程一直在运行过程中，监听从服务器传输过来的消息
        ClientConnectServerThread clientConnectServerThread = ManagerClientConnectServerThread.getClientConnectServerThread(user.getUserId());
        try {

            ObjectOutputStream oos = new ObjectOutputStream(clientConnectServerThread.getSocket().getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 编写方法退出客户端，并给服务端发送一个退出系统的Message对象
     */
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT.getCode());
        // 要退出这个用户
        message.setSender(user.getUserId());
        ClientConnectServerThread clientConnectServerThread = ManagerClientConnectServerThread.getClientConnectServerThread(user.getUserId());
        try {

            ObjectOutputStream oos = new ObjectOutputStream(clientConnectServerThread.getSocket().getOutputStream());
            oos.writeObject(message);
            oos.flush();
           System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
