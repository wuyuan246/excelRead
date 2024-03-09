package com.example.service;

import cn.hutool.core.util.StrUtil;
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
        List<String> matchWord = listener2.getDataList();
        convertToWriteData(wordList, matchWord, allDataList);
        return allDataList;
    }

    private void convertToWriteData(List<String> wordList, List<String> matchWord, List<WriteData> allDataList) {
        Map<String, Long> result = new ConcurrentHashMap<>(wordList.size());
        wordList.forEach(w -> result.put(w, 0L));

        // 遍历 matchWord 中的每个元素，检查是否有匹配的 word 元素
        for (String match : matchWord) {
            for (String w : wordList) {
                // 如果 match 包含了 word 列表中的单词，则增加对应 key 的计数
                if (match.contains(w)) {
                    result.put(w, result.get(w) + 1);
                }
            }
        }
        // 提取方法以处理“总数量”的特殊逻辑
        long totalCount = matchWord.size();

        for (Map.Entry<String, Long> entry : result.entrySet()) {
            String word = entry.getKey();
            Long value = entry.getValue();
            WriteData writeData = new WriteData();
            writeData.setWord(word);
            writeData.setCount(value);
            writeData.setTotalCount(totalCount);
            if (value != 0){
                writeData.setFrequency(value / (double) totalCount);
            }else {
                writeData.setFrequency(0.0);
            }
            allDataList.add(writeData);
        }
        Collections.sort(allDataList, (o1, o2) -> {
            if (o1.getFrequency() > o2.getFrequency()) {
                return -1;
            } else if (o1.getFrequency() < o2.getFrequency()) {
                return 1;
            } else {
                return 0;
            }
        });
    }
}
