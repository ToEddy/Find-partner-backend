package com.findPartner.tools.once;

import com.alibaba.excel.EasyExcel;
import com.findPartner.domain.model.TagExcelInfo;

import java.util.List;

/**
 * 导入 Excel
 *
 * @author yupi
 */
public class ImportExcel {

    /**
     * 读取数据
     */
    public static void main(String[] args) {
        // 写法1：JDK8+
        // since: 3.0.0-beta1
        String fileName = "E:\\星球项目\\yupao-backend\\src\\main\\resources\\testExcel.xlsx";
//        readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 监听器读取
     *
     * @param fileName
     */
    public static void readByListener(String fileName) {
        EasyExcel.read(fileName, TagExcelInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读
     *
     * @param fileName
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<TagExcelInfo> totalDataList =
                EasyExcel.read(fileName).head(TagExcelInfo.class).sheet().doReadSync();
        for (TagExcelInfo tagExcelInfo : totalDataList) {
            System.out.println(tagExcelInfo);
        }
    }

}
