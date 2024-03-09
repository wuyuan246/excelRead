package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class UploadData2Listener implements ReadListener<Map<Integer, String>> {

    /**
     * 数据列表用于收集数据
     */
    private long total = 0;
    private Map<String, Long> map = new ConcurrentHashMap<>();
    List<String> words = new ArrayList<>();

    @Override
    public void invoke(Map<Integer, String> cloumnsData, AnalysisContext context) {
        // 处理每一行数据，并将处理结果添加到数据列表中
        if (cloumnsData == null || context == null) {
            throw new IllegalArgumentException("Data or Context cannot be null.");
        }
        for (Integer i : cloumnsData.keySet()) {
            String string = cloumnsData.get(i);
            if(StrUtil.isBlank(string)){
                continue;
            }
            words.add(string);
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
        log.info("文本读取完毕，大小为：" + words.size());
        // 使用Stream API进行数据处理，提高代码的可读性和效率
        words.stream()
                .filter(StrUtil::isNotBlank) // 筛选出非空关键词
                .forEach(keyWord -> {
                    total++;
                    map.compute(keyWord, (k, v) -> v == null ? 1L : v + 1L); // 自动处理键不存在的情况
                });

        log.info("文本合并完毕，map大小为：" + map.size());
    }

    /**
     * 获取处理后的数据
     */
    public Map<String, Long> getDataList() {
        map.put("总数量", total);
        return map;
    }
}