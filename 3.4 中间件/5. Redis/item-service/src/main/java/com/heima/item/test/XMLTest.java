package com.heima.item.test;




import cn.hutool.json.JSONUtil;
import cn.hutool.json.xml.JSONXMLSerializer;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.json.XML;

import java.util.HashMap;

public class XMLTest {
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();
        CommonInputDTO inputDTO = new CommonInputDTO();
        inputDTO.setTRANSCODE_TWO("002");

        String json = JSON.toJSONString(inputDTO);
        System.out.println(json);

        JSONObject jsonObject = new JSONObject(json);
        String toString = XML.toString(jsonObject);//<TRANSCODE>ZZJ_FUNCTION</TRANSCODE><TRANSCODE_TWO>002</TRANSCODE_TWO>






    }
}
