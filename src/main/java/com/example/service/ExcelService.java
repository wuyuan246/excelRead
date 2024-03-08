package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.example.entity.ReadData;
import com.example.entity.WriteData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExcelService {

    public List<WriteData> analyzeAndGenerateExcel(MultipartFile file) throws IOException {
        // 实例化监听器
        UploadData1Listener listener1 = new UploadData1Listener();
        UploadData2Listener listener2 = new UploadData2Listener();

        // 读取第一个Sheet的数据
        EasyExcel.read(file.getInputStream(), ReadData.class, listener1).sheet(0).doRead();

        // 读取第二个Sheet的数据
        EasyExcel.read(file.getInputStream(), listener2).sheet(1).doRead();

        // 将两个Sheet的数据合并在一起，视需求而定如何合并
        List<WriteData> allDataList = new ArrayList<>();
        List<String> wordList = listener1.getDataList();
        Map<String, Long> wordMap = listener2.getDataList();
        convertToWriteData(wordList, wordMap, allDataList);
        return allDataList;
    }

    private void convertToWriteData(List<String> wordList, Map<String, Long> wordMap, List<WriteData> allDataList) {
        Map<String, Long> result = new ConcurrentHashMap<>(wordList.size());
        for (String word : wordList) {
            // 优化：初始化wordMap中不存在的键值
            result.computeIfAbsent(word, k -> 0L);
        }

        // 提取方法以处理“总数量”的特殊逻辑
        long totalCount = getTotalCount(wordMap);

        for (Map.Entry<String, Long> entry : result.entrySet()) {
            String word = entry.getKey();
            WriteData writeData = new WriteData();
            if(wordMap.containsKey(word)){
                Long count = wordMap.get(word);
                writeData.setWord(word);
                writeData.setCount(count);
                writeData.setTotalCount(totalCount);
                // 优化：增加空检查
                if (totalCount != 0) {
                    writeData.setFrequency((double) count / totalCount);
                }
            } else {
                writeData.setWord(word);
                writeData.setCount(0L);
                writeData.setTotalCount(totalCount);
                writeData.setFrequency(0.0);
            }
            allDataList.add(writeData);
        }
    }

    // 提取的方法用于获取“总数量”
    private long getTotalCount(Map<String, Long> wordMap) {
        long totalCount = 0;
        if (wordMap.containsKey("总数量")) {
            totalCount = wordMap.get("总数量");
            // 优化：避免修改原始wordMap，通过复制解决特殊处理与数据完整性之间的冲突
            wordMap = new ConcurrentHashMap<>(wordMap);
            wordMap.remove("总数量");
        }
        return totalCount;
    }


}
