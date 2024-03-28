package com.zhangjingqi.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP发送端
 */
public class UDPSenderB {
    public static void main(String[] args) throws IOException {
       //TODO 1.创建DatagramSocket对象，准备发送和接受数据
        DatagramSocket socket = new DatagramSocket(9998);

        //TODO 2.将需要发送的数据封装到DatagramPacket对象中
        byte[] bytes = "hello 明天吃火锅~".getBytes();
        //参数1：要发送的数据
        //参数2、3：发送哪一段的数据，0-bytes.length就是发送数组的全部数据
        //参数4：主机,客户端和接收端不在一台电脑，InetAddress.getByName(IP)即可
        //参数5：端口
        DatagramPacket packet = new DatagramPacket(bytes,0,bytes.length,InetAddress.getByName("127.0.0.1"),9999);

        //TODO 3.发送数据
        socket.send(packet);

        //TODO 4.接收 接收端 回复的消息
        byte[] buf = new byte[1024*64];
        DatagramPacket packetReturn = new DatagramPacket(buf,buf.length);
        System.out.println("发送端B 等待接收数据....");
        socket.receive(packetReturn);
        int length = packetReturn.getLength();
        //实际接收到的数据
        byte[] data = packetReturn.getData();
        String s = new String(data, 0, length);
        System.out.println(s);

        //TODO 关闭资源
        socket.close();
    }
}
