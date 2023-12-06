package com.zhangjingqi.qqserver.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.User;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * 该类对应的对象和某个客户端保持通信
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerConnectClientThread extends Thread{

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
        while(true){
            System.out.println("服务端和客户端保持通信，读取数据.... userId:"+userId);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                //读取数据
                Message message = (Message) ois.readObject();

                //后面会使用Message

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            //如果服务器端没有发送消息过来，这个地方会堵塞，此线程会一直等待
            //读取客户端发送的User对象

        }
    }
}
