package com.zhangjingqi.service.impl;

import com.zhangjingqi.dao.UserDao;
import com.zhangjingqi.dao.impl.UserDaoImpl;
import com.zhangjingqi.service.UserService;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class UserServiceImpl implements UserService  {

    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao =userDao;
    }

    private Properties properties;

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    private Map<String, UserDao> map;

    public void setMap(Map<String, UserDao> map) {
        this.map = map;
    }

    public Map<String, UserDao> getMap() {
        return map;
    }



    //   注入List
    private List<String> stringList;

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<String> getStringList() {
        return stringList;
    }


    private List<UserDao> userDaoList;

    public void setUserDaoList(List<UserDao> userDaoList) {
        this.userDaoList = userDaoList;
    }

    public List<UserDao> getUserDaoList() {
        return userDaoList;
    }


    private Set<String> strSet;

    public void setStrSet(Set<String> strSet) {
        this.strSet = strSet;
    }
    private Set<UserDao> userDaoSet;

    public void setUserDaoSet(Set<UserDao> userDaoSet) {
        this.userDaoSet = userDaoSet;
    }



    public Set<String> getStrSet() {
        return strSet;
    }

    public Set<UserDao> getUserDaoSet() {
        return userDaoSet;
    }

    public UserDao getUserDao() {
        return userDao;
    }
}
