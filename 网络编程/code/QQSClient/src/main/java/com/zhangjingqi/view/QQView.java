package com.zhangjingqi.view;

import com.zhangjingqi.service.UserClientService;
import com.zhangjingqi.utils.Utility;

/**
 * 菜单界面
 */
public class QQView {

    /**
     * 控制是否显示菜单
     */
    private boolean loop = true;
    /**
     * 接收用户的键盘输入
     */
    private String key = "";

    /**
     * 完成用户登录验证和用户注册等功能
     */
    public UserClientService userClientService = new UserClientService();


    public static void main(String[] args) {
        QQView qqView = new QQView();
        qqView.mainMenu();
        System.out.println("退出客户端系统");
    }

    /**
     * 显示主菜单
     */
    private void mainMenu() {
        while (loop) {
            System.out.println("***********欢迎登录网络通信系统*************");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入你的选择:");
            key = Utility.readString(1);

            //根据用户的输入来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.print("请输入用户号");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码");
                    String password = Utility.readString(50);

                    //TODO 到服务端验证用户是否合法
                    if (userClientService.checkUser(userId,password)) {
                        //进入二级菜单
                        System.out.println(String.format("网络通信系统二级菜单(用户%s)", userId));
                        while (loop) {
                            System.out.println(String.format("\n========网络通信系统二级菜单(用户%s)===========", userId));
                            System.out.println("\t\t 1.显示在线用户列表");
                            System.out.println("\t\t 2.群发消息");
                            System.out.println("\t\t 3.私聊消息");
                            System.out.println("\t\t 4.发送文件");
                            System.out.println("\t\t 9.退出系统");

                            System.out.print("请输入你的选择:");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //获取在线用户列表
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    break;
                                case "3":
                                    break;
                                case "4":
                                    break;
                                case "9":
                                    loop = false;
                                    //调用方法，给服务器发送一个退出系统的Message
                                    System.out.println("退出系统");
                                    userClientService.logout();
                                    break;
                            }
                        }
                    }else {
                        System.out.println("登录服务器失败，用户名或密码存在问题");
                    }
                    break;
                case "9":
                    loop = false;
                    System.out.println("退出系统");
            }
        }
    }
}
