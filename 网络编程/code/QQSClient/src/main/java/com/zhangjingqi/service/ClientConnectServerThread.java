package com.zhangjingqi.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;
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

                //判断message的类型，然后做响应的业务处理
                if (message.getMesType().equals(MessageType.MESSAGE_RETTURN_ONLINE_FRIEND.getCode())){
                    //获取在线用户，取出在线列表信息并显示
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("当前在线用户列表如下");
                    for (int i=0;i<onlineUsers.length;i++){
                        System.out.println("用户："+onlineUsers[i]);
                    }
                }else if (MessageType.MESSAGE_COMM_MES.getCode().equals(message.getMesType())) {
                    //转发给指定客户端，假如说客户不在线的话，可以保存到数据库，这样就可以实现离线留言
                    System.out.println("\n用户"+message.getGetter()+"收到来自用户"+message.getSender()+"的消息:"+message.getContent());
                } else if (MessageType.MESSAGE_TO_ALL_EXIT.getCode().equals(message.getMesType())) {
                    //群发消息
                    System.out.println("\n用户收到来自用户"+message.getSender()+"的群发消息:"+message.getContent());

                }else{
                    System.out.println("其他类型的message，暂时不处理");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
