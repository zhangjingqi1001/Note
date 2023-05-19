package com.zhangjingqi.controller;

import com.zhangjingqi.pojo.Result;
import com.zhangjingqi.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

//    @PostMapping("/upload")
//    public Result upload(String username, Integer age, MultipartFile image) throws IOException {
////      MultipartFile 介绍文件
////      TODO 文件的name（表单项中的名字），filename已经封装在MultipartFile中
//        String originalFilename = image.getOriginalFilename();  // 文件原始名
//
////      TODO 构造唯一文件名（不能重复） - 采用uuid  通用唯一识别码，长度固定字符串，是不会重复的
//        int index = originalFilename.lastIndexOf("."); //最后一个点的坐标
//
//        String imageType = originalFilename.substring(index); //ru .jpg
//
//        String imageName = UUID.randomUUID().toString()+imageType;
//        log.info("新文件名：{}",imageName);
////        将文件存储在磁盘目录当中  E:\Note\SpringBootWeb\Note\image
////        image.transferTo(new File("E:/Note/SpringBootWeb/Note/image/"+originalFilename));
////        但是我们一般不使用文件原始名进行存储，因为用户A存储照片的名称为1.jpg,用户B存储照片的名称为1.jpg，那这样用户B的图片将用户A的图片覆盖了
//
////        TODO 将文件存储在磁盘目录当中  E:\Note\SpringBootWeb\Note\image
//        image.transferTo(new File("E:/Note/SpringBootWeb/Note/image/"+imageName));
//        return Result.success();
//    }

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws IOException {

        log.info("文件上传，文件名:{}", image.getOriginalFilename());
//      调用阿里云OOS工具类进行上传
        String url = aliOSSUtils.upload(image);
        log.info("文件上传完成，url:{}",url);

        return Result.success(url);
    }
}
