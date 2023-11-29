package com.zhangjingqi.upload;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 文件上传的客户端
 */
public class TCPFileUploadClient {
    public static void main(String[] args) throws IOException {

        //TODO 1.客户端连接服务端8888，得到Socket对象
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);

        //TODO 2.把磁盘上的图片读取到文件字节数组中
        //2.1 创建读取磁盘文件的输入流(字节流)
        String filePath = "C:\\Users\\jd\\Desktop\\jpg1.jpg";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));

        //fileBytesArray数组就是filePath所对应的字节数组
        byte[] fileBytesArray = StreamUtils.streamToByteArray(bis);


        //TODO 3.利用IO流将字节数组放入Socket通道中，使服务端可以读取到（将二进制数组数据发送给客户端）
        //借助socket获取字节流
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        // byte[] byteArray = new byte[1024];//一次性读取1024个byte，也就是1k,但是这里并不需要，直接写字节数组fileBytesArray即可
        bos.write(fileBytesArray);
        bos.flush();
        //设置写入数据的结束标记
        socket.shutdownOutput();

        //TODO 4.读取客户端返回过来的数据
        BufferedReader bw = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        char[] charArray = new char[512];  //一次性读取512个字符，一个字符是两个字节，也就是512*2=1024字节，1k
        int len = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ( (len = bw.read(charArray))!=-1){
            stringBuilder.append(new String(charArray, 0, len));
        }
        System.out.println(stringBuilder);

        //或者使用工具类中的方法,都是可以的
//        String resultFromServer = StreamUtils.streamToString(socket.getInputStream());
//        System.out.println(resultFromServer);

        //TODO 关闭流
        bw.close();
        bis.close();
        bos.close();
        socket.close();

    }
}
