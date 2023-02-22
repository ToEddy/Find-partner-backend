package com.findPartner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.findPartner.domain.entity.Tag;
import com.findPartner.mapper.TagMapper;
import com.findPartner.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @author 47607
 * @description 针对表【tag(标签表)】的数据库操作Service实现
 * @createDate 2023-02-22 11:35:54
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}




