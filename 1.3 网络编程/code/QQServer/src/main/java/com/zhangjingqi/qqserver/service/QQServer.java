package com.zhangjingqi.qqserver.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;
import com.zhangjingqi.common.User;
import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是服务器，在监听9999，等待客户端的连接，并保持通信
 */
@Data
public class QQServer {

    //创建一个集合存放多个用户，如果是此用户登录，便认为是合法的
    //也可以使用ConcurrentHashMap，可以在并发的环境下处理（没有线程安全问题）
    //HashMap是没有处理线程安全的，因此在多线程情况下是不安全的
    private static HashMap<String, User> validUser = new HashMap<>();

    private ServerSocket serverSocket = null;

    /**
     * 进行类加载的时候会执行下面这个代码
     */
    static {
        validUser.put("100", new User("100", "123456"));
        validUser.put("200", new User("200", "123456"));
        validUser.put("300", new User("300", "123456"));
        validUser.put("至尊宝", new User("至尊宝", "123456"));
        validUser.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUser.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    /**
     * 这是一个循环监听的过程
     * 并不是客户端A发送完信息服务器接收到后此服务器就关闭，而是一直监听，因为还有可能其他客户端发送过来信息
     */
    public QQServer() {
        System.out.println("服务端在9999端口监听....");
        //启动推送新闻的线程
        new Thread(new SendNewsAllService()).start();

        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            this.serverSocket = new ServerSocket(9999);

            //监听是一直进行，当和某个客户端连接后，会继续监听，因此使用while循环
            while (true) {
                //没有客户端连接9999端口时，程序会堵塞，等待连接
                Socket socket = serverSocket.accept();

                ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器端没有发送消息过来，这个地方会堵塞，此线程会一直等待
                //读取客户端发送的User对象
                User user = (User) ois.readObject();

                //创建Message对象，准备恢复客户端
                Message message = new Message();
                oos = new ObjectOutputStream(socket.getOutputStream());
                //验证用户是否合法
                User userValid = validUser.get(user.getUserId());
                if (userValid != null && userValid.getUserId().equals(user.getUserId()) && userValid.getPasswd().equals(user.getPasswd())) {
                    //合法用户
                    message.setMesType(MessageType.find(1));
                    //给客户端进行回复
//                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message);
                    oos.flush();

                    //创建一个线程，和客户端保持通信。
                    //该线程需要持有Socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(user.getUserId(), socket);
                    serverConnectClientThread.start();

                    //把该线程对象放入到一个集合中
                    ManagerServerConnectServerThread.addClientThread(user.getUserId(), serverConnectClientThread);

                } else {
                    //登录失败
                    message.setMesType(MessageType.find(2));
                    oos.writeObject(message);
                    oos.flush();

                    socket.close();
                }
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
//          如果服务端退出了while循环，说明服务器端不再监听了，因此需要关闭资源
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
