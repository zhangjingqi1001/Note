package com.zhangjingqi.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTCP03Server {
    public static void main(String[] args) throws IOException {
        //TODO 1.在本机的9999端口监听，等待连接
        //一定要确认9999端口没有被其他服务在监听或占用
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务端，在9999端口监听，等待连接....");

        //TODO 2.当没有客户端连接9999端口时，程序会堵塞，等待连接
        //如果有客户端连接，则会返回一个socket对象，程序继续
        Socket socket = serverSocket.accept();
        System.out.println("服务器端 socket=" + socket.getClass());

        //TODO 3.获取socket.getInputStream()字节流，并利用转换流将其转换成字符流
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //TODO 4.读取客户端发送过来的数据
        //一次性最多读取1K的内容，因为char类型占用2个字节，一共有512*2=1024个byte，也就是1k
        char[] chars = new char[512];
        int readCount = 0;
        while ((readCount = bufferedReader.read(chars)) != -1) {
            System.out.println(new String(chars, 0, readCount));
        }

        //TODO 5.获取socket相关联输出流，并利用转换流将其转换成字符流
        OutputStream outputStream = socket.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        //BufferedWriter是字符缓冲流
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        //TODO 6.通过输出流，写入数据到数据通道
        bufferedWriter.write("hello,client，字符流");
        bufferedWriter.flush();
        //设置结束标记
        socket.shutdownOutput();



        //TODO 7.关闭流和socket
        bufferedWriter.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();//多了一个这个
    }
}
