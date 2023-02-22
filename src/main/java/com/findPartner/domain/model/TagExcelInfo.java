package com.findPartner.domain.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author eddy
 * @createTime 2023/2/14
 */
@Data
public class TagExcelInfo {
    /**
     * 标签名
     */
    @ExcelProperty("标签名")
    private String tagName;
    /**
     * 标签 id
     */
    @ExcelProperty("标签 id")
    private Integer tagId;

}
