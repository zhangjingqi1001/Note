package com.zhangjingqi.qqframe;

import com.zhangjingqi.qqserver.service.QQServer;

/**
 * 此类创建一个QQServer对象，启动后台的服务
 */
public class QQFrame {
    public static void main(String[] args) {
        //创建QQServer对象，会启动QQServer构造器
        QQServer qqServer = new QQServer();

    }
}
