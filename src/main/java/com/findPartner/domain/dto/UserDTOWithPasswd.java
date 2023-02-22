package com.findPartner.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用于用户的注册与登录
 *
 * @author eddy
 */
@Data
public class UserDTOWithPasswd implements Serializable {

    private static final long serialVersionUID = 4792808481447005254L;
    private String username;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 校验密码（当用户是登录的时候，前端传递一个null给后端）
     */
    private String checkPassword;

}
