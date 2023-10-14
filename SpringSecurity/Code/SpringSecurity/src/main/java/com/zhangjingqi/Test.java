package com.zhangjingqi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().plusMinutes(6));//2023-10-11T18:03:57.550

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String data = dateTimeFormatter.format(LocalDateTime.now());
        System.out.println(data);//20231011180358
    }


    public static void main1(String[] args) {
        int a = 0b00000110;
        System.out.println(a);
        System.out.println(a>>>=1);
        System.out.println(Integer.toBinaryString(a));

        int b = (-1)>>2;
        int c = (-1)>>>2;
        System.out.println(b);
        System.out.println(c);
        System.out.println(Integer.toBinaryString(c));


        int x=0;
        System.out.println(x++);
        System.out.println(++x);

        System.out.println(Integer.toBinaryString(12));

        int i;
        i=-32>>>2;
        System.out.println(i);//8

        System.out.println(0b00111111111111111111111111111000);

        int y = 010;
        System.out.println(y*1000); //8000
        System.out.println(0xf);
    }
}
