package com.zhangjingqi.exception;

import com.zhangjingqi.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *  全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
//  指定捕获哪一个类型的异常
    @ExceptionHandler(Exception.class)  //捕获所有异常
    public Result ex(Exception ex){
         ex.printStackTrace();// 打印堆栈信息
        return Result.error("对不起，操作失败");
    }

}
