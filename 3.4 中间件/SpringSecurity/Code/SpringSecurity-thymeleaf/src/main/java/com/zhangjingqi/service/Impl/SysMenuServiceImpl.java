package com.zhangjingqi.service.Impl;

import com.zhangjingqi.dto.SysMenuDao;
import com.zhangjingqi.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Override
    public List<String> queryPermissionByUserId(Integer userId) {
        return sysMenuDao.queryPermissionByUserId(userId);
    }
}
