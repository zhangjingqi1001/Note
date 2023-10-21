package com.zhangjingqi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        String jsonString = "{\"pays\":[{\"ls_cpscode\":\"string\",\"payclass\":\"string\",\"paymoney\":\"string\",\"paydetail\":{ }}]}";

        JSONObject payDetail = null;

        // 解析JSON字符串为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        // 获取 "pays" 数组
        JSONArray paysArray = jsonObject.getJSONArray("pays");

        // 遍历数组，删除 "paydetail" 对象
        for (int i = 0; i < paysArray.size(); i++) {
            JSONObject payObject = paysArray.getJSONObject(i);

            payDetail = (JSONObject) payObject.remove("paydetail");
        }

        // 将修改后的JSON对象重新序列化为字符串
        jsonObject.getJSONArray("pays").add(payDetail);

        System.out.println(jsonObject);

    }
}
