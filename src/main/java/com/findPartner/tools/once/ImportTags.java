package com.findPartner.tools.once;

import com.alibaba.excel.EasyExcel;
import com.findPartner.domain.model.TagExcelInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入星球用户到数据库
 */
public class ImportTags {

    public static void main(String[] args) {
        String fileName = "";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<TagExcelInfo> userInfoList = EasyExcel.read(fileName).head(TagExcelInfo.class).sheet().doReadSync();
        Map<String, List<TagExcelInfo>> listMap = userInfoList.stream()
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getTagName()))
                .collect(Collectors.groupingBy(TagExcelInfo::getTagName));

        for (Map.Entry<String, List<TagExcelInfo>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username = " + stringListEntry.getKey());
                System.out.println("1");
            }
        }
        System.out.println("不重复昵称数 = " + listMap.keySet().size());
    }
}
