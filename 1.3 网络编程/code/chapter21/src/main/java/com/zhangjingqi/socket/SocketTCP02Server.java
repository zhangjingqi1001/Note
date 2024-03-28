package com.zhangjingqi.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTCP02Server {
    public static void main(String[] args) throws IOException {
        //TODO 1.在本机的9999端口监听，等待连接
        //一定要确认9999端口没有被其他服务在监听或占用
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务端，在9999端口监听，等待连接....");

        //TODO 2.当没有客户端连接9999端口时，程序会堵塞，等待连接
        //如果有客户端连接，则会返回一个socket对象，程序继续
        Socket socket = serverSocket.accept();
        System.out.println("服务器端 socket=" + socket.getClass());

        //TODO 3.通过socket.getInputStream()读取客户端写入到数据端通道的数据
        InputStream inputStream = socket.getInputStream();

        //缓冲
        byte[] bytes = new byte[512 * 2];
        int readCount = 0;
        while ((readCount = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readCount));
        }

        //TODO 4.获取socket相关联输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello,client".getBytes());
        //设置结束标记
        socket.shutdownOutput();
        outputStream.flush();


        //TODO 5.关闭流和socket
        outputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();//多了一个这个
    }
}
