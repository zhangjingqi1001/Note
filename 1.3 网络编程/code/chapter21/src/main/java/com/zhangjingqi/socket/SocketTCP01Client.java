package com.zhangjingqi.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketTCP01Client {
    public static void main(String[] args) throws IOException {
        //TODO 1.连接服务端(ip,端口)
        //TODO 2.连接上后，生成Socket
        //连接本机9999端口，如果连接成功的话会返回一个socket
        Socket socket = new Socket(InetAddress.getLocalHost(),9999);
        //Socket socket = new Socket("127.0.0.1",9999);
        System.out.println("客户端 socket返回="+socket.getClass());

        //TODO 3.得到和socket关联的socket.getOutputStream输出流对象
        OutputStream outputStream = socket.getOutputStream();

        //TODO 4.通过输出流，写入数据到数据通道
        outputStream.write("hello,server".getBytes());

        //TODO 关闭流对象和socket对象，避免资源浪费
        outputStream.close();
        socket.close();

        System.out.println("客户端退出了");
    }
}
