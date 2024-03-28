package com.zhangjingqi.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketTCP02Client {
    public static void main(String[] args) throws IOException {
        //TODO 1.连接服务端(ip,端口)
        //TODO 2.连接上后，生成Socket
        //连接本机9999端口，如果连接成功的话会返回一个socket
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //Socket socket = new Socket("127.0.0.1",9999);
        System.out.println("客户端 socket返回=" + socket.getClass());

        //TODO 3.得到和socket关联的socket.getOutputStream输出流对象
        OutputStream outputStream = socket.getOutputStream();

        //TODO 4.通过输出流，写入数据到数据通道
        outputStream.write("hello,server".getBytes());
        //设置结束标记
        socket.shutdownOutput();
        outputStream.flush();

        //TODO 5.获取输入流，读取数据
        InputStream inputStream = socket.getInputStream();
        //缓冲
        byte[] bytes = new byte[512 * 2];
        int readCount = 0;
        while ((readCount = inputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, 0, readCount));
        }


        //TODO 6.关闭流对象和socket对象，避免资源浪费
        inputStream.close();
        outputStream.close();
        socket.close();

        System.out.println("客户端退出了");
    }
}
