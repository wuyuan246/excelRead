package com.example.controller;

import com.alibaba.excel.EasyExcel;
import com.example.entity.WriteData;
import com.example.service.ExcelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.InputStreamResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String originalFileName = file.getOriginalFilename();
            String baseFileName = originalFileName;
            if (originalFileName != null && originalFileName.contains(".")) {
                baseFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            }
            String fileName = URLEncoder.encode(baseFileName + "分析结果", "UTF-8").replaceAll("\\+", "%20");

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
            responseResetAndWriteJsonErrorMessage(response, e);
        }
        // 数据写入成功后返回OK状态码
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void responseResetAndWriteJsonErrorMessage(HttpServletResponse response, Exception e) {
        // 发生异常时重置响应
        response.reset();

        // 设置响应的内容类型为JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 创建并发送错误信息
        try {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "failure");
            errorResponse.put("message", "处理文件时发生错误: " + e.getMessage());
            response.getWriter().println(new ObjectMapper().writeValueAsString(errorResponse));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}