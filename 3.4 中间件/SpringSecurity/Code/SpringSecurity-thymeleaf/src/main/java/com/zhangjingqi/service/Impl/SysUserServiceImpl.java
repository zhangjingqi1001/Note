package com.zhangjingqi.service.Impl;

import com.zhangjingqi.dto.SysUserDao;
import com.zhangjingqi.entity.SysUser;
import com.zhangjingqi.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public SysUser getByUserName(String userName) {
        return sysUserDao.getByUserName(userName);
    }
}
