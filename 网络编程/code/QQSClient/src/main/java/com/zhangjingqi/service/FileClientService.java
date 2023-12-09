package com.zhangjingqi.service;

import com.zhangjingqi.common.Message;
import com.zhangjingqi.common.MessageType;

import java.io.*;

/**
 * 该类完成文件的传输
 */
public class FileClientService {

    public void sendFileToOne(String src, String dest, String sender, String getter) {
        //读取src文件
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES.getCode());
        message.setSender(sender);
        message.setGetter(getter);
        message.setSrc(src);
        message.setDest(dest);
        //需要将文件从客户端读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) new File(src).length()];
        // 二进制流

        try {
            //读取文件
            fileInputStream = new FileInputStream(src);
            //将src文件读入到程序的字节数组中
            fileInputStream.read(fileBytes);

            //将文件对应的字节数粗设置到message
            message.setFileBytes(fileBytes);


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println("用户" + sender + "向用户" + getter + "发送文件" + src + "并存储到对方电脑目录" + dest);

        //向服务端发送Message
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManagerClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("发送文件完毕");
    }

}
