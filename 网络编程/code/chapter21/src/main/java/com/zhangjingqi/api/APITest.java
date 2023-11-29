package com.zhangjingqi.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class APITest {
    public static void main(String[] args) throws UnknownHostException {
//        InetAddress
//      TODO 1.获取本机的InetAddress对象
        InetAddress localHost = InetAddress.getLocalHost();
//      会输出本机的主机名 、IP地址 zhangjingqi-PC/192.168.101.1
        System.out.println(localHost);

//      TODO 2.根据指定主机名获取InetAddress
        InetAddress byName = InetAddress.getByName("zhangjingqi-PC");
        System.out.println(byName);

//      TODO 3.根据指定域名获取ip地址对象
        InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
//      www.baidu.com/110.242.68.3
        System.out.println(inetAddress);


//      TODO 4.通过InetAddress对象，获取对应的主机名
        String hostName = inetAddress.getHostName();
//      www.baidu.com
        System.out.println(hostName);
//      TODO 5.通过InetAddress对象，获取对应的主机地址
        String hostAddress = inetAddress.getHostAddress();
//      110.242.68.3
        System.out.println(hostAddress);

    }
}
