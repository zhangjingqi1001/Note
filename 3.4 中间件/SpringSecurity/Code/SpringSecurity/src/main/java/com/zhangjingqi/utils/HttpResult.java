package com.zhangjingqi.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpResult {
    private Integer code;
    private String msg;
    private Object data;

    public HttpResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
