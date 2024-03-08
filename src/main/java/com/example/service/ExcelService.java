package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.entity.ReadData;
import com.example.entity.WriteData;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ExcelService {

    public List<WriteData> analyzeAndGenerateExcel(MultipartFile file) throws IOException {
        // 实例化监听器
        UploadData1Listener listener1 = new UploadData1Listener();
        UploadData2Listener listener2 = new UploadData2Listener();

        // 读取第一个Sheet的数据
        EasyExcel.read(file.getInputStream(), ReadData.class, listener1).sheet(0).doRead();

        // 读取第二个Sheet的数据
        EasyExcel.read(file.getInputStream(), ReadData.class, listener2).sheet(1).doRead();

        // 将两个Sheet的数据合并在一起，视需求而定如何合并
        List<WriteData> allDataList = new ArrayList<>();
        List<String> wordList = listener1.getDataList();
        HashMap<String, Integer> wordMap = listener2.getDataList();
        convertToWriteData(wordList, wordMap, allDataList);
        return allDataList;
    }

    private void convertToWriteData(List<String> wordList, HashMap<String, Integer> wordMap, List<WriteData> allDataList) {
        for (String word : wordList) {
            // 只有当dataList中不包含当前word作为key时，才把它加入到dataList中
            if (!wordMap.containsKey(word)) {
                // 将word作为key，0作为value加入到dataList中
                wordMap.put(word, 0);
            }
        }
        int totalCount = 0;
        if(wordMap.containsKey("总数量")){
            totalCount = wordMap.get("总数量");
            wordMap.remove("总数量");
        }

        for (String word : wordMap.keySet()) {
            WriteData writeData = new WriteData();
            writeData.setWord(word);
            writeData.setCount(wordMap.get(word));
            writeData.setTotalCount(totalCount);
            writeData.setFrequency((double) wordMap.get(word) / totalCount);
            allDataList.add(writeData);
        }
    }


}
