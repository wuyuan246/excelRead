package com.example.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.entity.ReadData;
import com.example.entity.WriteData;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ExcelService {

    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ReadData}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>
     * 3. 直接读即可
     */
/*    @Test
    public void repeatedRead() {
        //String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        String fileName = "D:\\目录词频分析.xlsx";
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        //EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).doReadAll();

        // 读取部分sheet
        //fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";

        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(ReadData.class).registerReadListener(new DemoDataListener()).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(ReadData.class).registerReadListener(new DemoDataListener()).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }*/

/*    public List<WriteData> analyzeAndGenerateExcel(MultipartFile file) throws IOException {
        // 调用服务层提供的方法来处理文件并生成分析结果
        EasyExcel.read(file.getInputStream(), ReadData.class, new UploadData1Listener()).sheet(0).doRead();
        EasyExcel.read(file.getInputStream(), ReadData.class, new UploadData2Listener()).sheet(1).doRead();

    }*/

/*    public void simpleRead() {
        //String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        String fileName = "D:\\目录词频分析.xlsx";
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        //EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).doReadAll();

        // 读取部分sheet
        //fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";

        // 写法1
        try (ExcelReader excelReader = EasyExcel.read(fileName).build()) {
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(ReadData.class).registerReadListener(new DemoDataListener()).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(ReadData.class).registerReadListener(new DemoDataListener()).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        }
    }*/

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link WriteData}
     * <p>
     * 2. 直接写即可
     */
/*    @Test
    public void simpleWrite() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入

        // 写法1 JDK8+
        // since: 3.0.0-beta1
        String fileName = TestFileUtil.getPath() + "simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, WriteData.class)
                .sheet("模板")
                .doWrite(() -> {
                    // 分页查询数据
                    return data();
                });
    }*/
}
