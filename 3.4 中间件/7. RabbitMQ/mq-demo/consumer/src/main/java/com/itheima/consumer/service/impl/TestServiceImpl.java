package com.itheima.consumer.service.impl;

import com.itheima.consumer.service.TestService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    private int a;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
