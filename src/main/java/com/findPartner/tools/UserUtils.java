package com.findPartner.tools;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.findPartner.common.constant.UserConstant;
import com.findPartner.domain.dto.UserDTO;
import com.findPartner.domain.entity.User;
import com.findPartner.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * @author eddy
 */
@Component
public class UserUtils {
    private static UserUtils userUtils;
    @Resource
    private UserMapper userMapper;

    /**
     * 鉴权
     *
     * @param request
     * @return
     */
    public static boolean authentication(HttpServletRequest request) {
        //fixme 不能通过 session 去查询管理员的权限, 因为极有可能管理员突然被升级了，
        // 但是 session 没有被修改，只是数据库修改了
        Object attribute = request.getSession().getAttribute(UserConstant.SESSION_USER_STATE);
        if (attribute == null) {
            //没有登录信息
            return false;
        }
        UserDTO userDto = (UserDTO) attribute;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Long adminId = userDto.getId();
        queryWrapper.eq("id", adminId);
        User adminUser = userUtils.userMapper.selectById(queryWrapper);
        Integer userRole = adminUser.getUserRole();
        if (userRole != 1 && userRole != 2) {
            //鉴权失败，非管理员
            return false;
        }
        return true;
    }

    /**
     * 用户信息脱敏
     *
     * @param user
     * @return
     */
    public static UserDTO getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        UserDTO safetyUser = BeanUtil.copyProperties(user, UserDTO.class);
        return safetyUser;
    }

    @PostConstruct
    public void init() {
        userUtils = this;
        userUtils.userMapper = this.userMapper;
    }
}
