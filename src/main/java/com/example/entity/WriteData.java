package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@EqualsAndHashCode
public class WriteData {
    @ExcelProperty("关键词")
    String word;
    @ExcelProperty("频数")
    Long count;
    @ExcelProperty("总数")
    Long totalCount;
    @ExcelProperty("频率")
    Double frequency;
}
