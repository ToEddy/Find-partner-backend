package com.findPartner.tools;

import cn.hutool.core.bean.BeanUtil;
import com.findPartner.domain.dto.UserDTO;
import com.findPartner.domain.entity.User;
import org.springframework.stereotype.Component;


/**
 * @author eddy
 */
@Component
public class UserUtils {

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
}
