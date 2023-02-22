package com.findPartner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.findPartner.domain.entity.User;
import com.findPartner.mapper.UserMapper;
import com.findPartner.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 47607
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-02-22 11:35:55
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}




