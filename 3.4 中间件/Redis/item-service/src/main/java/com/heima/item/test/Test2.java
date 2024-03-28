package com.heima.item.test;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.xml.JSONXMLSerializer;

public class Test2 {
    public static void main(String[] args) {

//      如何将XML字符串转成对象
//        String s = "<TRANSCODE>ZZJ_FUNCTION</TRANSCODE><TRANSCODE_TWO>002</TRANSCODE_TWO>";
//      XML字符串转为JSONObject
//        JSONObject xml = JSONUtil.parseFromXml(s);
//        System.out.println("XML字符串转为JSONObject:"+xml);//{"TRANSCODE":"ZZJ_FUNCTION","TRANSCODE_TWO":"002"}
//      JSONObject转XML字符串工具
//        String s1 = JSONXMLSerializer.toXml(xml);
//        System.out.println("JSON转XML字符串工具"+s1);//<TRANSCODE>ZZJ_FUNCTION</TRANSCODE><TRANSCODE_TWO>002</TRANSCODE_TWO>

//      **********************************************************************************
        CommonInputDTO inputDTO = new CommonInputDTO();
        inputDTO.setTRANSCODE_TWO("002");
//      对象转JSON
        JSON json = JSONUtil.parse(inputDTO);
        System.out.println(json); //{"TRANSCODE":"ZZJ_FUNCTION","TRANSCODE_TWO":"002"}
//      参数是每层缩进的空格数
        String s = json.toJSONString(0);
        System.out.println(s);
//      JSON转XML
        JSONObject jsonObject = JSONUtil.parseObj(s);
        String xml = JSONXMLSerializer.toXml(jsonObject);
        System.out.println(xml); //<TRANSCODE>ZZJ_FUNCTION</TRANSCODE><TRANSCODE_TWO>002</TRANSCODE_TWO>
//        ****************************************
        System.out.println("*********************************************");

//      XML转JSON
        String xmlStr = "<TRANSCODE>ZZJ_FUNCTION</TRANSCODE><TRANSCODE_TWO>002</TRANSCODE_TWO>";
        JSONObject xmlJson = JSONUtil.parseFromXml(xmlStr);//{"TRANSCODE":"ZZJ_FUNCTION","TRANSCODE_TWO":"002"}
        CommonInputDTO commonInputDTO = JSONUtil.toBean(xmlJson, CommonInputDTO.class);
        System.out.println(commonInputDTO);
    }
}
