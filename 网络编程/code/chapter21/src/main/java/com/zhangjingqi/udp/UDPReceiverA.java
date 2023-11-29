package com.zhangjingqi.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * UDP接收端
 */
public class UDPReceiverA {
    public static void main(String[] args) throws IOException {
        //TODO 1.创建一个DatagramSocket对象，准备在9999接收数据和发送数据
        DatagramSocket socket = new DatagramSocket(9999);
        //TODO 2.构建DatagramPacket对象，准备接收数据
        //UDP协议最大的包64k,不适合传输大量的数据
        byte[] buf = new byte[1024*64];
        //参数1：存储将要发送或接收的数据
        //参数2：指定要发送或接收的数据的长度
        //此时packet对象是空的，是什么也没有的
        DatagramPacket packet = new DatagramPacket(buf,buf.length);

        //TODO 3.调用接收方法接收数据,将通过网络传输的DatagramPacket对象填充到packet对象里，此时packet对象便不是空对象了，是有数据的
        //此方法表示会在9999端口等待，如果有一个数据报发送到了9999端口就会接收，没有发送到9999端口就会在此地方堵塞
        System.out.println("接收端A 等待接收数据....");
        socket.receive(packet);

        //TODO 4.对packet拆包，取出数据并显示
        //实际接收到的数据的长度，我们的byte数组大小是1024*64，但是不一定会有这么多的数据传输过来
        int length = packet.getLength();
        //实际接收到的数据
        byte[] data = packet.getData();
        String s = new String(data, 0, length);
        System.out.println(s);


        //TODO 5.给发送端回复消息
        byte[] bytes = "好的，明天见".getBytes();
        DatagramPacket packetReturn = new DatagramPacket(bytes,0,bytes.length, InetAddress.getByName("127.0.0.1"),9998);
        socket.send(packetReturn);

        //TODO 6.关闭资源
        socket.close();


    }
}
