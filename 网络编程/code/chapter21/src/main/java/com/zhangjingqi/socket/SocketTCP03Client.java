package com.zhangjingqi.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class SocketTCP03Client {
    public static void main(String[] args) throws IOException {
        //TODO 1.连接服务端(ip,端口)
        //TODO 2.连接上后，生成Socket
        //连接本机9999端口，如果连接成功的话会返回一个socket
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
        //Socket socket = new Socket("127.0.0.1",9999);
        System.out.println("客户端 socket返回=" + socket.getClass());

        //TODO 3.得到和socket关联的socket.getOutputStream输出流对象
        OutputStream outputStream = socket.getOutputStream();
        //TODO 4.利用转换流将字节流转换成字符流
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        //BufferedWriter是字符缓冲流
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        //TODO 5.通过输出流，写入数据到数据通道
        bufferedWriter.write("hello,server，字符流");
        bufferedWriter.flush();//如果使用的字符流，需要手动刷新一下，否则数据不会写入通道中
        //设置结束标记
        socket.shutdownOutput();


        //TODO 5.获取输入流，并将字节输入流转换成字符输入流
        //InputStream是字节流，InputStreamReader是字符流，BufferedReader是字符缓冲流
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        //TODO 6.读取客户端发送过来的数据
        //一次性最多读取1K的内容，因为char类型占用2个字节，一共有512*2=1024个byte，也就是1k
        char[] chars = new char[512];         int readCount = 0;
        while ((readCount = bufferedReader.read(chars)) != -1) {
            System.out.println(new String(chars, 0, readCount));
        }


        //TODO 7.关闭流对象和socket对象，避免资源浪费
        bufferedReader.close();
        bufferedWriter.close();
        socket.close();

        System.out.println("客户端退出了");
    }
}
