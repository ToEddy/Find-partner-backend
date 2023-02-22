package com.findPartner.common.constant;

/**
 * @author eddy
 * @createTime 2023/2/8
 */
public interface UserConstant {
    String SALT = "eddy";
    String SESSION_USER_STATE = "user_login";
    int LOGIN_TOKEN_TTL = 30;
    String REDIS_LOGIN_KEY = "user:login:";
}
