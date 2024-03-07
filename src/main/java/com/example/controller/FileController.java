package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ReadData;
import com.example.service.UploadData1Listener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@RestController
public class FileController {

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ReadData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadData1Listener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ReadData.class, new UploadData1Listener(uploadDAO)).sheet().doRead();
        return "success";
    }
}
