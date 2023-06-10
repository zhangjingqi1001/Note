package com.zhangjingqi.service;

public interface AccountService {
    void transferMoney(String outAccount, String inAccount, Integer money);
}
