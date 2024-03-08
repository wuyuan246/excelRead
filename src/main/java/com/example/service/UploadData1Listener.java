package com.example.service;

import cn.hutool.core.util.StrUtil;
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
import java.util.stream.Collectors;

// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class UploadData1Listener implements ReadListener<ReadData> {

    /**
     * 数据列表用于收集数据
     */
    private List<String> dataList = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(ReadData data, AnalysisContext context) {
        // 处理每一行数据，并将处理结果添加到数据列表中
        String word = data.getData();
        if(dataList.contains(word)){
            log.info("词库中存在重复的关键词：" + word);
        } else {
            dataList.add(word);
        }

    }
    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("词库读取完毕，过滤空值前大小为：" + dataList.size());
        dataList = dataList.stream().filter(s -> StrUtil.isNotBlank(s)).collect(Collectors.toList());
        log.info("词库读取完毕，过滤空值后大小为：" + dataList.size());
    }

    /**
     * 获取处理后的数据
     */
    public List<String> getDataList() {
        return dataList;
    }
}