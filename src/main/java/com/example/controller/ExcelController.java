package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ReadData;
import com.example.entity.WriteData;
import com.example.service.ExcelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<InputStreamResource> uploadAndAnalyzeExcel(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        try {
            // 如果文件为空，返回错误
            if (file.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<WriteData> analysisResult = excelService.analyzeAndGenerateExcel(file);

            // 设置响应头部信息
            String fileName = URLEncoder.encode("分析结果", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + fileName + ".xlsx");

            // 使用 EasyExcel.write 方法将生成的分析结果写入 HTTP 响应
            EasyExcel.write(response.getOutputStream(), WriteData.class)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet("模板")
                    .doWrite(analysisResult);
        } catch (Exception e) {
            // 处理异常情况
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}