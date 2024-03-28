package com.zhangjingqi.upload;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 文件上传的服务端
 */
public class TCPFileUploadServer {
    public static void main(String[] args) throws IOException {

        //TODO 1.服务端在本机监听8888端口
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务端在8888端口监听....");

        //TODO 2.等待连接
        Socket socket = serverSocket.accept();

        //TODO 3.读取客户端发送的数据
        //通过socket得到输入流
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        //读取客户端传输过来的二进制数据数组
        byte[] fileBytesArray = StreamUtils.streamToByteArray(bis);

        //TODO 4.将得到的bytes数组写入到指定的路径，就会得到一个文件
        String filePath = "src/qie2.jpg";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        bos.write(fileBytesArray);
        bos.flush();


        //TODO 5.服务端向客户端发送“收到图片”再退出
        //使用字符缓冲流BufferedWriter,OutputStreamWriter是转换流，将字节流转换成字符流
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("收到图片");

        bw.flush();
        //写出结束标记
        socket.shutdownOutput();

        //TODO 6.关闭流
        bw.close();
        bos.close();
        bis.close();
        socket.close();
        serverSocket.close();
    }
}
