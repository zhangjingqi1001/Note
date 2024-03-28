package com.zhangjingqi.upload;

import java.io.*;

/**
 * IO流工具类
 */
public class StreamUtils {

    /**
     * 将输入流转换成byte[]
     */
    public static byte[] streamToByteArray(InputStream is) throws IOException {
        //1.创建输出流对象
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //2.创建字节数组,一次可以读取1024个字节，也就是1k
        byte[] b = new byte[1024];
        int len;
        //每次最多读取b单位长度的数据
        while ((len = is.read(b)) != -1) {
            //把读取到的数据写入bos流
            bos.write(b, 0, len);
        }
        bos.flush();
        //3.循环读取，此时array就是我们读取的文件的所有二进制的内容（将文件一次性转成二进制）
        byte[] array = bos.toByteArray();
        bos.close();

        //4.返回文件的二进制形式
        return array;
    }

    /**
     * 将输入流的数据直接转换成一个字符串
     * InputStream字节流
     */
    public static String streamToString(InputStream is) throws IOException {
        //BufferedReader字符缓冲流
        //InputStreamReader 转换流，可以将字节流转换成字符流
        //InputStream字节流
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        //一次性读一行
        while ( (line=reader.readLine())!=null){
            builder.append(line+"\r\n");
        }
        reader.close();
        return builder.toString();
    }

}
