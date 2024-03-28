package com.zhangjingqi.service.impl;

import com.zhangjingqi.mapper.AccountMapper;
import com.zhangjingqi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    @Transactional
    public void transferMoney(String outAccount, String inAccount, Integer money) {
        accountMapper.decrMoney(outAccount,money);
//        int c = 3/0;
        accountMapper.incrMoney(inAccount,money);
    }
}
