package com.zhangjingqi.service;

import com.zhangjingqi.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientConnectServerThread extends Thread {
    //该线程需要持有Socket属性
    private Socket socket;


    /**
     *因为Thread需要在后台跟我们的服务器进行通信(保持一个联系)，因此我们使用while循环来控制
     */
    @Override
    public void run() {
        while(true){
            //一直读取从服务器端回收的消息
            System.out.println("客户端线程，等待读取从服务端发送的消息....");

            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器端没有发送消息过来，这个地方会堵塞，此线程会一直等待
                //这就是一个堵塞式网络编程，效率是相对比较低的
                Message message = (Message)ois.readObject();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
