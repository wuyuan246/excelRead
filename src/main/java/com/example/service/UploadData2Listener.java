package com.example.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.example.entity.ReadData;
import com.example.entity.WriteData;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UploadData2Listener implements ReadListener<ReadData> {

    /**
     * 数据列表用于收集数据
     */
    private List<WriteData> dataList = new ArrayList<>();
    HashMap<String, Integer> map = new HashMap<>();
    Integer total = 0;

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ReadData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        // 处理每一行数据，并将处理结果添加到数据列表中
        ToMap(data); // 假设有一个方法转换ReadData到WriteData
    }

    private void ToMap(ReadData data) {
        total += 1;
        String keyWord = data.getData();
        if(map.containsKey(keyWord)){
            map.put(keyWord, map.get(keyWord) + 1);
        }else {
            map.put(keyWord, 1);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 分析完全部的，转换成输出
        log.info("所有数据解析完成！");

    }

    /**
     * 获取处理后的数据
     */
    public HashMap<String, Integer> getDataList() {
        map.put("总数量", total);
        return map;
    }
}